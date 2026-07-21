package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "BIOMETRIA_FACIAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BiometriaFacial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "usuario_id",
            nullable = false,
            unique = true
    )
    private UsuarioConsumerChaves usuario;

    // O embedding é o array de floats que representa o rosto
    // Deve ser armazenado como um tipo que suporte grandes textos (BLOB ou TEXT)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "embedding", columnDefinition = "vector(512)") // O tamanho (512) depende do seu modelo AI
    private float[] embedding;

    private OffsetDateTime dataCadastro;

    public BiometriaFacial(UsuarioConsumerChaves usuario, float[] embeddingCriptografado, OffsetDateTime dataCadastro) {
        this.usuario = usuario;
        this.embedding = embeddingCriptografado;
        this.dataCadastro = dataCadastro;
    }
}




