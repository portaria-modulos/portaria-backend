package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ENTREGA_CHAVE_HISTORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private String empresa;

    // --- GETTERS E SETTERS ---


}