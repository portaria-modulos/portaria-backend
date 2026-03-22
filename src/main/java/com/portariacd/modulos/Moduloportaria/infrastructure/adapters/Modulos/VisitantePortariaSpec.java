package com.portariacd.modulos.Moduloportaria.infrastructure.adapters.Modulos;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.EntradaVisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.SaidaVisitanteEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VisitantePortariaSpec {
    public static Specification<VisitanteEntity> filial(Integer filial){

        return (root, query, cb) ->

                 filial == null ? null:cb.equal(root.get("filial"),filial
                );
    }
    public static Specification<VisitanteEntity> busca(String busca){
        return (root, query, cb) -> {
            if (busca != null && !busca.isEmpty()) {

                String link = "%" + busca.toLowerCase().trim() + "%";

                query.distinct(true);

                Join<VisitanteEntity, Recorrencia> recorrenciaJoin =
                        root.join("recorrencia", JoinType.LEFT);

                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.like(cb.lower(root.get("nomeCompleto")), link));
                predicates.add(cb.like(cb.lower(root.get("placaCarro")), link));
                predicates.add(cb.like(cb.lower(root.get("numeroTelefone")), link)); // ✅ corrigido
                predicates.add(cb.like(cb.lower(recorrenciaJoin.get("nome")), link));

                // 👇 trata filial como número (melhor prática)
                if (link.matches("\\d+")) {
                    Integer numero = Integer.valueOf(link);
                    predicates.add(cb.equal(root.get("filial"), numero));
                }

                return cb.or(predicates.toArray(new Predicate[0]));
            }

            return cb.conjunction();
        };
    }
    public static Specification<VisitanteEntity> ativoRegistro(Boolean ativo){
        return (root, query, cb) ->  {
            if (ativo == null) {
                return null; // não aplica filtro
            }
            return cb.equal(root.get("ativo"), ativo);
        };
    }

    public static Specification<VisitanteEntity> criacao(LocalDate data) {
        return (root, query, cb) ->  {
            if (data == null) {
                return null; // não aplica filtro
            }

            return cb.equal(cb.function("DATE", LocalDate.class, root.get("dataCriacao")), data);
        };
    }

}
