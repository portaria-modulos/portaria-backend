package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="saida_visitante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaidaVisitanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dataSaida;
    private Long ficalSaidaId;
    private String nomeFiscal;
    private String imagem;
    private Integer filial;



}
