package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Mantém o mapeamento automático do Jackson para o Spring deserializar direto no Controller
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UsuarioConsumerRequestDTO.class, name = "funcionario"),
        @JsonSubTypes.Type(value = TerceirizadoResponse.class, name = "terceirizado")
})
public abstract class FactoryResponseChaves {
    @NotBlank
    private String tipo;
    @NotBlank
    private String nome;
    @NotBlank
    private String setor;
    @NotNull
    private Integer filial;
    @NotBlank
    private String nomeEmpresa;
    @NotNull
    Long usuarioInsert;
    // Getters e Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }
    public Integer getFilial() { return filial; }
    public void setFilial(Integer filial) { this.filial = filial; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public Long getUsuarioInsert() {
        return usuarioInsert;
    }

    public void setUsuarioInsert(Long usuarioInsert) {
        this.usuarioInsert = usuarioInsert;
    }
}