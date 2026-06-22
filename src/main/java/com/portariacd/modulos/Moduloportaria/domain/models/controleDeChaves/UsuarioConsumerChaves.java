package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.UsuarioConsumerRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "USUARIO_CONSUMER_CHAVES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioConsumerChaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String matricula;
    private String GmcoreId;
    private String nome;
    private String setor;
    private Integer filial;
    private Boolean ativo;
    private OffsetDateTime creatAt;
    private Long usuarioInsert;

    public UsuarioConsumerChaves(UsuarioConsumerRequestDTO usm) {
        this.matricula = usm.matricula();
        this.GmcoreId = usm.GmcoreId();
        this.nome = usm.nome();
        this.setor = usm.setor();
        this.ativo = true;
        this.filial = usm.filial();
        this.creatAt = OffsetDateTime.now();
        this.usuarioInsert = usm.usuarioInsert();
    }
}
