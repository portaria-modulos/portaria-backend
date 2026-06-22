package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ENTREGA_CHAVE_HISTORY")
public class EntregaChaveHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long blocoChaves;

    // Quem pegou a chave (pode ser o ID do funcionário/motorista ou o nome)
    // Se você tiver uma entidade Funcionario, mude para @ManyToOne
    private String nomeColaborador;
    private String matriculaColaborador;

    // Registro do momento da entrega
    @Column(nullable = false)
    private OffsetDateTime dataHoraRetirada;

    // Registro de quando a chave foi devolvida
    private LocalDateTime dataHoraDevolucao;

    // Quem realizou a entrega (o usuário da portaria logado no sistema)
    private String usuarioPortariaRetirada;
    private Long usuarioIdRetirada;
    private String usuarioPortariaDevolucao;
    private Long usuarioIdDevolucao;
    private Long filialId;
    private String status;

    // --- GETTERS E SETTERS ---
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBlocoChaves() {
        return blocoChaves;
    }

    public void setBlocoChaves(long blocoChaves) {
        this.blocoChaves = blocoChaves;
    }

    public String getNomeColaborador() {
        return nomeColaborador;
    }

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }

    public String getMatriculaColaborador() {
        return matriculaColaborador;
    }

    public void setMatriculaColaborador(String matriculaColaborador) {
        this.matriculaColaborador = matriculaColaborador;
    }

    public OffsetDateTime getDataHoraRetirada() {
        return dataHoraRetirada;
    }

    public void setDataHoraRetirada(OffsetDateTime dataHoraRetirada) {
        this.dataHoraRetirada = dataHoraRetirada;
    }

    public LocalDateTime getDataHoraDevolucao() {
        return dataHoraDevolucao;
    }

    public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) {
        this.dataHoraDevolucao = dataHoraDevolucao;
    }

    public String getUsuarioPortariaRetirada() {
        return usuarioPortariaRetirada;
    }

    public void setUsuarioPortariaRetirada(String usuarioPortariaRetirada) {
        this.usuarioPortariaRetirada = usuarioPortariaRetirada;
    }

    public String getUsuarioPortariaDevolucao() {
        return usuarioPortariaDevolucao;
    }

    public void setUsuarioPortariaDevolucao(String usuarioPortariaDevolucao) {
        this.usuarioPortariaDevolucao = usuarioPortariaDevolucao;
    }

    public Long getUsuarioIdRetirada() {
        return usuarioIdRetirada;
    }

    public void setUsuarioIdRetirada(Long usuarioIdRetirada) {
        this.usuarioIdRetirada = usuarioIdRetirada;
    }

    public Long getUsuarioIdDevolucao() {
        return usuarioIdDevolucao;
    }

    public void setUsuarioIdDevolucao(Long usuarioIdDevolucao) {
        this.usuarioIdDevolucao = usuarioIdDevolucao;
    }

    public void setFilialId(Long filialId) {
        this.filialId = filialId;
    }

    public Long getFilialId() {
        return filialId;
    }

    public void setBlocoChaves(Long blocoChaves) {
        this.blocoChaves = blocoChaves;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}