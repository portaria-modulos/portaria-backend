package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.RegistroVisitanteRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TarefasServices {
   private final RegistroVisitanteRepository repository;
    TarefasServices(RegistroVisitanteRepository repository){
        this.repository = repository;
    }
    @EventListener(ApplicationReadyEvent.class)
    public void executarAoIniciar() {
        System.out.println("Servidor iniciado. Executando tarefa de limpeza inicial...");
        Rodaservice();
    }
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    protected void Rodaservice(){
        System.out.println("Execultando tarefa");

        var registroEncontrados = repository.findAllByAtivoTrue();
        LocalDateTime horaAtual = LocalDateTime.now();
        List<RegistroVisitantePortariaEntity> listaDeRegistro = new ArrayList<>();

        for(var registro:registroEncontrados){
            boolean alterado = false;
            var visitante = registro.getVisitante();
            if(visitante==null){
                continue;
            }
            var recorrencia = visitante.getRecorrencia();
            var status = registro.getStatus();
            if(recorrencia==null ){
                continue;
            }
            if(recorrencia.getNome().trim().equals("UNICO") && !status.equals(StatusPortaria.AGUARDANDO_SAIDA) ){
                LocalDate dataHoje = LocalDate.now();
                LocalDate dataRegistro = registro.getDataCriacao().toLocalDate();
                if (dataRegistro.isBefore(dataHoje)){
                    registro.setAtivo(false);
                    registro.setStatus(StatusPortaria.FECHADO_AUTOMATICO);
//                    String msg = """
//                            protocolo: %s
//                            Fechado: %s
//                            Data criacao: %s
//                            Nome: %s
//                            """.formatted(registro.getProtocolo(),LocalDateTime.now(),registro.getDataCriacao(),registro.getNomeCompleto());
//                    System.out.println("Registro finalizado:\n"+msg);
                     alterado = true;
                }
            }
                if(recorrencia.getNome().trim().equals("RECORRENTE TEMPORARIO")){
                    if(registro.getVisitante().getDataRestritoAcesso()!=null){
                    LocalDate expiredDate = registro.getVisitante().getDataRestritoAcesso();
                    LocalDate hoje = LocalDate.now();
                    boolean expirouHoje = expiredDate.isEqual(hoje);
                    boolean expirouAntes = expiredDate.isBefore(hoje);
                    if(expirouHoje ||expirouAntes){
                        registro.getVisitante().setAtivo(false);
                        registro.getVisitante().setBloqueioAcesso(true);
                        registro.getVisitante().setDataRestritoAcesso(null);
                        registro.setAtivo(false);
                        alterado = true;

                    }
                    }
            }
            if (registro.getEntradaVisitante() == null) {
                // Não tem saída lançada, pula
                continue;
            }
            if(registro.getEntradaVisitante().getDataEntrada()!=null) {
                LocalDateTime entrada = registro.getEntradaVisitante().getDataEntrada();
                    var horas = Duration.between(entrada, horaAtual).toHours();
                    if (horas >= 10 && horas < 12) {
                        registro.setPrioridadeAviso("ATENÇÃO: MOTORISTA COM SAÍDA PENDENTE");registro.setPrioridadeAtrasoAtivo(true);
                        alterado = true;

                    }
                    // ⛔ Passou de 12 horas → PRIORIDADE ATRASO + inativa
                    else if (horas >= 12) {
                        registro.setPrioridadeAtraso("ATENÇÃO: MOTORISTA COM ATRASO CRITICO DE SAIDA");
                        registro.setPrioridadeAtrasoAtivo(true);
                        alterado = true;
                    }
                    if(alterado){
                        System.out.println("Registro adiconado");
                    listaDeRegistro.add(registro);
                }

            }
        }
       if(!listaDeRegistro.isEmpty()){
           System.out.println("salvou");
           repository.saveAll(listaDeRegistro);
       }
    }
}
