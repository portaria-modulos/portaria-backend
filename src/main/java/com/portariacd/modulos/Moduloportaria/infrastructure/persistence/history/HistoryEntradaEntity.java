package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.history;

import com.portariacd.modulos.Moduloportaria.domain.models.history.HistoryEntrada;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name="history")
@AllArgsConstructor
@NoArgsConstructor
public class HistoryEntradaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long registroPortariaId;
    private String nomeCompleto;
    @ManyToOne
    @JoinColumn(name = "visitante_id", foreignKey = @ForeignKey(name = "fk_history_visitante"))
    private VisitanteEntity visitante;
    private String placaVeiculo;
    private LocalDateTime dataCriacao;
    @Lob
    @Column(name = "descricao",columnDefinition = "TEXT")
    @Basic(fetch = FetchType.EAGER)
    private String descricao;
    @Enumerated(EnumType.STRING)
    private StatusPortaria status;
    @ManyToOne
    @JoinColumn(name = "criador_id", foreignKey = @ForeignKey(name = "fk_history_criador"))
    private UsuarioEntity criador;
    private Integer filialSolicitado;
    private Boolean ativo;
    private String protocolo;
    private String bloco;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Long ficalIdEntrada;
    private Long ficalIdSaida;
    private String nomeFiscaEntrada;
    private String nomeFiscaSaida;
    private String imagemEntrada;
    private String imagemSaida;

    public HistoryEntradaEntity(HistoryEntrada historyEntrada) {
        this.placaVeiculo = historyEntrada.getPlacaVeiculo();
        this.nomeCompleto = historyEntrada.getNomeCompleto();
        this.visitante = historyEntrada.getVisitante();
        this.dataCriacao = historyEntrada.getDataCriacao();
        this.descricao = historyEntrada.getDescricao();
        this.status = historyEntrada.getStatus();
        this.criador = historyEntrada.getCriador();
        this.filialSolicitado = historyEntrada.getFilialSolicitado();
        this.ativo = historyEntrada.getAtivo();
        this.protocolo = historyEntrada.getProtocolo();
        this.bloco = historyEntrada.getBloco();
        this.dataEntrada = historyEntrada.getDataEntrada();
        this.ficalIdSaida = historyEntrada.getFicalIdSaida();
        this.ficalIdEntrada = historyEntrada.getFicalIdEntrada();
        this.nomeFiscaEntrada = historyEntrada.getNomeFiscaEntrada();
        this.imagemEntrada = historyEntrada.getImagemEntrada();
        this.registroPortariaId = historyEntrada.getRegistroPortariaId();
    }

    public void UpdateHistoryInput(RegistroVisitantePortariaEntity r) {
        this.ficalIdEntrada = r.getEntradaVisitante().getFiscalEntradaId();
        this.nomeFiscaEntrada= r.getEntradaVisitante().getNomeFiscal();
        this.imagemEntrada = r.getEntradaVisitante().getImagem();
        this.dataEntrada = r.getEntradaVisitante().getDataEntrada();
        this.status = r.getStatus();
    }
    public void UpdateHistoryOuput(RegistroVisitantePortariaEntity r) {
        this.ficalIdSaida = r.getSaidaVisitante().getFicalSaidaId();
        this.nomeFiscaSaida = r.getSaidaVisitante().getNomeFiscal();
        this.dataSaida = r.getSaidaVisitante().getDataSaida();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegistroPortariaId() {
        return registroPortariaId;
    }

    public void setRegistroPortariaId(long registroPortariaId) {
        this.registroPortariaId = registroPortariaId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public VisitanteEntity getVisitante() {
        return visitante;
    }

    public void setVisitante(VisitanteEntity visitante) {
        this.visitante = visitante;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusPortaria getStatus() {
        return status;
    }

    public void setStatus(StatusPortaria status) {
        this.status = status;
    }

    public UsuarioEntity getCriador() {
        return criador;
    }

    public void setCriador(UsuarioEntity criador) {
        this.criador = criador;
    }

    public Integer getFilialSolicitado() {
        return filialSolicitado;
    }

    public void setFilialSolicitado(Integer filialSolicitado) {
        this.filialSolicitado = filialSolicitado;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Long getFicalIdEntrada() {
        return ficalIdEntrada;
    }

    public void setFicalIdEntrada(Long ficalIdEntrada) {
        this.ficalIdEntrada = ficalIdEntrada;
    }

    public Long getFicalIdSaida() {
        return ficalIdSaida;
    }

    public void setFicalIdSaida(Long ficalIdSaida) {
        this.ficalIdSaida = ficalIdSaida;
    }

    public String getNomeFiscaEntrada() {
        return nomeFiscaEntrada;
    }

    public void setNomeFiscaEntrada(String nomeFiscaEntrada) {
        this.nomeFiscaEntrada = nomeFiscaEntrada;
    }

    public String getNomeFiscaSaida() {
        return nomeFiscaSaida;
    }

    public void setNomeFiscaSaida(String nomeFiscaSaida) {
        this.nomeFiscaSaida = nomeFiscaSaida;
    }

    public String getImagemEntrada() {
        return imagemEntrada;
    }

    public void setImagemEntrada(String imagemEntrada) {
        this.imagemEntrada = imagemEntrada;
    }

    public String getImagemSaida() {
        return imagemSaida;
    }

    public void setImagemSaida(String imagemSaida) {
        this.imagemSaida = imagemSaida;
    }
}
