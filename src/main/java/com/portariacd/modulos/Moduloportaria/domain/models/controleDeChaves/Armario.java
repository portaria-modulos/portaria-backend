package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.ArmarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "ARMARIO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Armario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long filial;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "armario")
    @OrderBy("numero ASC")
    private List<BlocoChaves> blocoChaves;

    public Armario(ArmarioDTO dto) {
        this.filial = dto.filial();
        this.tipo = Tipo.convertTipo(dto.tipo());
    }
}
