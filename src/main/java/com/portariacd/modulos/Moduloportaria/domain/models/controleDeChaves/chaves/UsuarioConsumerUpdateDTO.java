package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves;

/**
 * Dados parciais para atualização de um usuário do controle de chaves.
 * Campos nulos ou vazios não alteram o valor já cadastrado.
 */
public class UsuarioConsumerUpdateDTO {
    private String matricula;
    private String gmId;
    private String cpf;
    private String nome;
    private String setor;
    private Integer filial;
    private String nomeEmpresa;
    private String tipoFuncionario;
    private Long usuarioInsert;

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getGmId() { return gmId; }
    public void setGmId(String gmId) { this.gmId = gmId; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }
    public Integer getFilial() { return filial; }
    public void setFilial(Integer filial) { this.filial = filial; }
    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }
    public String getTipoFuncionario() { return tipoFuncionario; }
    public void setTipoFuncionario(String tipoFuncionario) { this.tipoFuncionario = tipoFuncionario; }
    public Long getUsuarioInsert() { return usuarioInsert; }
    public void setUsuarioInsert(Long usuarioInsert) { this.usuarioInsert = usuarioInsert; }
}
