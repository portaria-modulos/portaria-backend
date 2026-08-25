package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.ImportacaoUsuarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.ResultadoLinhaImportacaoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.FactoryResponseChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.TerceirizadoResponse;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.UsuarioConsumerRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.Normalizer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UsuarioConsumerImportService {
    private final UsuarioConsumerService usuarioService;
    private final Validator validator;

    public UsuarioConsumerImportService(UsuarioConsumerService usuarioService, Validator validator) {
        this.usuarioService = usuarioService;
        this.validator = validator;
    }

    public ImportacaoUsuarioResponseDTO importar(MultipartFile planilha) throws IOException {
        if (planilha == null || planilha.isEmpty()) {
            throw new IllegalArgumentException("A planilha não pode estar vazia");
        }

        String nome = planilha.getOriginalFilename() == null
                ? ""
                : planilha.getOriginalFilename().toLowerCase(Locale.ROOT);
        List<Map<String, String>> linhas;
        if (nome.endsWith(".csv")) {
            linhas = lerCsv(planilha);
        } else if (nome.endsWith(".xls") || nome.endsWith(".xlsx")) {
            linhas = lerExcel(planilha);
        } else {
            throw new IllegalArgumentException("Formato inválido. Use CSV, XLS ou XLSX");
        }

        List<Callable<ResultadoLinhaImportacaoDTO>> tarefas = new ArrayList<>();
        for (int i = 0; i < linhas.size(); i++) {
            final int linha = i + 2;
            final Map<String, String> dados = linhas.get(i);
            tarefas.add(() -> processarLinha(linha, dados));
        }

        int quantidadeThreads = Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors()));
        ExecutorService executor = Executors.newFixedThreadPool(quantidadeThreads);
        List<ResultadoLinhaImportacaoDTO> detalhes;
        try {
            detalhes = executor.invokeAll(tarefas).stream().map(futuro -> {
                try {
                    return futuro.get();
                } catch (Exception e) {
                    return new ResultadoLinhaImportacaoDTO(0, "ERRO", mensagem(e));
                }
            }).sorted(Comparator.comparingInt(ResultadoLinhaImportacaoDTO::linha)).toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Importação interrompida", e);
        } finally {
            executor.shutdown();
        }

        int cadastrados = (int) detalhes.stream().filter(r -> "CADASTRADO".equals(r.status())).count();
        return new ImportacaoUsuarioResponseDTO(
                linhas.size(), cadastrados, linhas.size() - cadastrados, detalhes);
    }

    private ResultadoLinhaImportacaoDTO processarLinha(int linha, Map<String, String> dados) {
        try {
            FactoryResponseChaves usuario = criarUsuario(dados);
            SetValidation(usuario);
            usuarioService.cadastroDeUsuarioSemBiometria(usuario);
            return new ResultadoLinhaImportacaoDTO(linha, "CADASTRADO", "Usuário criado com sucesso");
        } catch (Exception e) {
            return new ResultadoLinhaImportacaoDTO(linha, "ERRO", mensagem(e));
        }
    }

    private void SetValidation(FactoryResponseChaves usuario) {
        Set<ConstraintViolation<FactoryResponseChaves>> violations = validator.validate(usuario);
        if (!violations.isEmpty()) {
            String erros = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .sorted()
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Dados inválidos");
            throw new IllegalArgumentException("Erro de validação: " + erros);
        }
    }

    private FactoryResponseChaves criarUsuario(Map<String, String> dados) {
        String tipo = valor(dados, "tipo").toLowerCase(Locale.ROOT);
        FactoryResponseChaves usuario;
        if (tipo.equals("funcionario") || tipo.equals("funcionário")) {
            var funcionario = new UsuarioConsumerRequestDTO();
            funcionario.setMatricula(valor(dados, "matricula"));
            funcionario.setGmId(valor(dados, "gmid", "gmcoreid", "gmcore_id"));
            usuario = funcionario;
        } else if (tipo.equals("terceirizado")) {
            var terceirizado = new TerceirizadoResponse();
            terceirizado.setCpf(valor(dados, "cpf"));
            usuario = terceirizado;
        } else {
            throw new IllegalArgumentException("Tipo deve ser funcionario ou terceirizado");
        }

        usuario.setTipo(tipo);
        usuario.setNome(valor(dados, "nome"));
        usuario.setSetor(valor(dados, "setor"));
        usuario.setFilial(numero(valor(dados, "filial")));
        usuario.setNomeEmpresa(valor(dados, "nomeempresa", "empresa", "nome_empresa"));
        usuario.setUsuarioInsert(numeroLong(valor(dados, "usuarioinsert", "usuario_insert")));
        return usuario;
    }

    private List<Map<String, String>> lerCsv(MultipartFile planilha) throws IOException {
        String conteudo;
        try (var reader = new BufferedReader(new InputStreamReader(
                planilha.getInputStream(), StandardCharsets.UTF_8))) {
            conteudo = reader.lines().reduce((a, b) -> a + "\n" + b).orElse("");
        }
        char delimitador = conteudo.lines().findFirst().map(linha ->
                linha.indexOf(';') >= 0 && linha.indexOf(';') > linha.indexOf(',') ? ';' : ',').orElse(',');

        try (var parser = CSVParser.parse(new StringReader(conteudo), CSVFormat.DEFAULT.builder()
                     .setDelimiter(delimitador)
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .setTrim(true)
                     .build())) {
            List<Map<String, String>> linhas = new ArrayList<>();
            for (var record : parser) {
                Map<String, String> linha = new HashMap<>();
                for (String header : parser.getHeaderMap().keySet()) {
                    linha.put(normalizar(header), record.get(header));
                }
                if (linha.values().stream().anyMatch(this::temValor)) linhas.add(linha);
            }
            return linhas;
        }
    }

    private List<Map<String, String>> lerExcel(MultipartFile planilha) throws IOException {
        try (var workbook = WorkbookFactory.create(planilha.getInputStream())) {
            var sheet = workbook.getSheetAt(0);
            var header = sheet.getRow(sheet.getFirstRowNum());
            if (header == null) throw new IllegalArgumentException("A planilha não possui cabeçalho");
            DataFormatter formatter = new DataFormatter();
            Map<Integer, String> colunas = new HashMap<>();
            for (var celula : header) colunas.put(celula.getColumnIndex(), normalizar(formatter.formatCellValue(celula)));

            List<Map<String, String>> linhas = new ArrayList<>();
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> linha = new HashMap<>();
                for (var entry : colunas.entrySet()) {
                    var cell = row.getCell(entry.getKey());
                    linha.put(entry.getValue(), cell == null ? "" : formatter.formatCellValue(cell).trim());
                }
                if (linha.values().stream().anyMatch(this::temValor)) linhas.add(linha);
            }
            return linhas;
        }
    }

    private String valor(Map<String, String> dados, String... nomes) {
        for (String nome : nomes) {
            String valor = dados.get(normalizar(nome));
            if (temValor(valor)) return valor.trim();
        }
        return "";
    }

    private Integer numero(String valor) {
        return valor.isBlank() ? null : Integer.valueOf(valor.replace(",", ".").split("\\.")[0]);
    }

    private Long numeroLong(String valor) {
        return valor.isBlank() ? null : Long.valueOf(valor.replace(",", ".").split("\\.")[0]);
    }

    private String normalizar(String valor) {
        return Normalizer.normalize(valor == null ? "" : valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
    }

    private boolean temValor(String valor) { return valor != null && !valor.isBlank(); }

    private String mensagem(Exception e) {
        Throwable causa = e;
        while (causa.getCause() != null && (causa.getMessage() == null || causa.getMessage().isBlank())) causa = causa.getCause();
        return causa.getMessage() == null ? causa.getClass().getSimpleName() : causa.getMessage();
    }
}
