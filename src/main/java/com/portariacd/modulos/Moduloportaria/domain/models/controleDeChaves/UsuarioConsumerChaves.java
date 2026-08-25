package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.TerceirizadoResponse;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.UsuarioConsumerRequestDTO;
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
    @Column(unique = true, nullable = true)
    private String matricula;
    @Column(name = "gmcore_id", unique = true, nullable = true)
    private String GmcoreId;
    private String nome;
    private String setor;
    private Integer filial;
    private Boolean ativo;
    @Column(name = "creat_at")
    private OffsetDateTime creatAt;
    private Long usuarioInsert;
    @Column(unique = true, nullable = true)
    private String cpf;
    private String empresa;
    private Boolean tipoColaboradorFuncionario;
    private String tipoFuncionario;
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "biometria_facial_id")
    private BiometriaFacial biometriaFacial;
    public UsuarioConsumerChaves(UsuarioConsumerRequestDTO usm) {
        this.nome = usm.getNome();
        this.setor = usm.getSetor();
        this.ativo = true;
        this.filial = usm.getFilial();
        this.creatAt = OffsetDateTime.now();
        this.usuarioInsert = usm.getUsuarioInsert();
            this.matricula = usm.getMatricula();
            this.GmcoreId = usm.getGmId();
            this.empresa = usm.getNomeEmpresa();
            this.tipoColaboradorFuncionario = true;
            this.tipoFuncionario = usm.getTipo();

    }

    public UsuarioConsumerChaves(TerceirizadoResponse usm) {
        this.nome = usm.getNome();
        this.setor = usm.getSetor();
        this.ativo = true;
        this.filial = usm.getFilial();
        this.creatAt = OffsetDateTime.now();
        this.usuarioInsert = usm.getUsuarioInsert();
        this.empresa = usm.getNomeEmpresa();
        this.cpf = usm.getCpf();
        this.empresa = usm.getNomeEmpresa();
        this.tipoColaboradorFuncionario = false;
        this.tipoFuncionario = usm.getTipo();
    }
}
