package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.EntradaVisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.SaidaVisitanteEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroPortariaSpec {
    public static Specification<RegistroVisitantePortariaEntity> filial(Integer filial){

        return (root, query, cb) ->
                 filial == null ? null:cb.equal(root.get("filialSolicitado"),filial
                );
    }
    public static Specification<RegistroVisitantePortariaEntity> busca(String busca){
        return (root, query, cb) -> {
            if(busca!=null && !busca.isEmpty()){
                String link = "%"+busca.toLowerCase().trim()+"%";
                Join<RegistroVisitantePortariaEntity, VisitanteEntity> visitanteJoin =
                        root.join("visitante", JoinType.LEFT);
                Join<VisitanteEntity, Recorrencia> recorrenciaJoin =
                        visitanteJoin.join("recorrencia", JoinType.LEFT);


                Join<RegistroVisitantePortariaEntity, UsuarioEntity> usuarioJoin =
                        root.join("criador", JoinType.LEFT);
                return cb.or(
                        cb.like(cb.lower(root.get("nomeCompleto")),link),
                        cb.like(cb.lower(root.get("placaVeiculo")),link),
                        cb.like(root.get("protocolo"),link),
                        cb.like(cb.lower(root.get("bloco")),link),
                        cb.like(cb.lower(recorrenciaJoin.get("nome")), link),
                        cb.like(cb.lower(usuarioJoin.get("nome")), link)
                );
            };
            return  null;
        };
    }
    public static Specification<RegistroVisitantePortariaEntity> ativoRegistro(Boolean ativo){
        return (root, query, cb) ->  {
            if (ativo == null) {
                return null; // não aplica filtro
            }
            return cb.equal(root.get("ativo"), ativo);
        };
    }
    public static Specification<RegistroVisitantePortariaEntity> ativoRegistroStatus(String status){
        return (root, query, cb) ->  {
            if (status == null) {
                return null; // não aplica filtro
            }
            try {
                // Converte a String que veio do Front para o Tipo exato do seu Enum
                // Substitua 'SeuEnumStatus' pelo nome real da sua classe Enum (ex: StatusRegistro)
                StatusPortaria statusEnum = StatusPortaria.valueOf(status.toUpperCase());

                return cb.equal(root.get("status"), statusEnum);
            } catch (IllegalArgumentException e) {
                // Se o Front mandar uma string que não existe no Enum (como "acesso"),
                // ele ignora o filtro e não quebra o sistema
                return null;
            }
        };
    }

    public static Specification<RegistroVisitantePortariaEntity> criacao(LocalDate data) {
        return (root, query, cb) ->  {
            if (data == null) {
                return null; // não aplica filtro
            }

            return cb.equal(cb.function("DATE", LocalDate.class, root.get("dataCriacao")), data);
        };
    }


    public static Specification<RegistroVisitantePortariaEntity> buscaId(
            Long id,
            LocalDate dataEntrada,
            LocalDate dataSaida
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // JOIN visitante
            Join<RegistroVisitantePortariaEntity, VisitanteEntity> visitanteJoin =
                    root.join("visitante", JoinType.LEFT);

            // JOIN entrada
            Join<RegistroVisitantePortariaEntity, EntradaVisitanteEntity> entradaJoin =
                    root.join("entradaVisitante", JoinType.LEFT);

            // JOIN saída
            Join<RegistroVisitantePortariaEntity, SaidaVisitanteEntity> saidaJoin =
                    root.join("saidaVisitante", JoinType.LEFT);

            // Filtro por ID
            if (id != null) {
                predicates.add(criteriaBuilder.equal(visitanteJoin.get("id"), id));
            }

            // Filtro data de entrada
            if (dataEntrada != null) {
                LocalDateTime inicio = dataEntrada.atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        entradaJoin.get("dataEntrada"), inicio));
            }

            // Filtro data de saída
            if (dataSaida != null) {
                LocalDateTime fim = dataSaida.atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        saidaJoin.get("dataSaida"), fim));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
