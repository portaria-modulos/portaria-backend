package com.portariacd.modulos.Moduloportaria.infrastructure.validation;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ValidaNomeImagem {
    public static String validaImagem(String pasta, String nomeImagem) throws IOException {
        Path pathImagem = Paths.get(pasta, nomeImagem);
        if (!Files.exists(pathImagem)) {
            return nomeImagem;
        }
        String nome = nomeImagem;
        String ext = "";
        int idExt = nomeImagem.lastIndexOf(".");
        if (idExt > 0) {
            nome = nomeImagem.substring(0, idExt);
            ext = nomeImagem.substring(idExt);
        }
        int contador = 1;
        String novoNome;
        do {
            novoNome = String.format("%s(%d)%s", nome, contador, ext);
            pathImagem = Paths.get(pasta, novoNome);
            contador++;
        } while (Files.exists(pathImagem));
        return novoNome;

    }


    public static String criarDiretorio(MultipartFile file, String diretorio,String endpoint){
        byte[] by = null;
        try {
            by = file.getBytes();
            File pastaCriada = new File(diretorio);
//            String filename = file.getOriginalFilename().replaceAll("\\s+", "_");
            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            // cria nome único
            String nomeArquivo = UUID.randomUUID().toString() + ext;
            String pah = diretorio+ "/"+ ValidaNomeImagem.validaImagem(diretorio, nomeArquivo);
            if (!pastaCriada.exists()) {
                pastaCriada.mkdirs();
            }
            Files.write(Paths.get(pah), by);
            String nameImagem = endpoint +"/" + pah ;
            return nameImagem;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
