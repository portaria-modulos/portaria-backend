package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "logs_auditoria_chaves")
@Getter
@Setter
public class LogAuditoriaChavesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private OffsetDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private AcaoAuditoriaChaves acao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private ModuloAuditoriaChaves modulo;

    @Column(nullable = false, length = 120)
    private String entidade;

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(name = "usuario_responsavel_id")
    private Long usuarioResponsavelId;

    @Column(name = "usuario_responsavel_nome", nullable = false, length = 255)
    private String usuarioResponsavelNome;

    private Long filial;

    @Column(name = "bloco_id")
    private Long blocoId;

    @Column(name = "armario_id")
    private Long armarioId;

    @Column(name = "chave_id")
    private Long chaveId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valor_anterior", columnDefinition = "jsonb")
    private Map<String, Object> valorAnterior;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valor_novo", columnDefinition = "jsonb")
    private Map<String, Object> valorNovo;

    @Column(name = "quantidade_anterior")
    private Integer quantidadeAnterior;

    @Column(name = "quantidade_nova")
    private Integer quantidadeNova;

    @Column(name = "endereco_ip", length = 80)
    private String enderecoIp;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;
}
