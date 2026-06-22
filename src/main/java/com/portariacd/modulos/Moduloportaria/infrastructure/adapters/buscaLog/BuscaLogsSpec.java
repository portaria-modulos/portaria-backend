package com.portariacd.modulos.Moduloportaria.infrastructure.adapters.buscaLog;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.log.LogAcaoEntity;
import org.springframework.data.jpa.domain.Specification;

public class BuscaLogsSpec {
    public static Specification<LogAcaoEntity> busca(String busca){
        return (root, query, cb) -> {
            if(busca!=null && !busca.isEmpty()){
                String link = "%" + busca.toLowerCase() + "%";
//                var usuarioJoin = root.join("usuarioId", JoinType.LEFT);
                                return cb.or(
                        cb.like(cb.lower(root.get("usuario")), link),
                        cb.like(cb.lower(root.get("descricao")), link),
                        cb.like(cb.lower(root.get("acao")), link)
                );
            };
            return  null;
        };
    }
}

