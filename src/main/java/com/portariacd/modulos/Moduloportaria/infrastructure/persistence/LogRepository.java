package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.log.LogAcaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogAcaoEntity,Long> ,
        JpaSpecificationExecutor<LogAcaoEntity>
{
}
