package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "Entrada_visitante")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntradaVisitanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dataEntrada;
    private String nomeFiscal;
    private Long fiscalEntradaId;
    private String imagem;
    private Integer filial;
}
