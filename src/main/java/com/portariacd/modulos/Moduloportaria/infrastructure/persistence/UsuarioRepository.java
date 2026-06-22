package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Long> {
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.perfil WHERE u.email = :email")
    Optional<UsuarioEntity> findOneByEmail(String email);

    @Query(""" 
            SELECT p FROM UsuarioEntity p 
            WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.email) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.ocupacaoOperacional) LIKE LOWER(CONCAT('%', :busca, '%'))""")
    Page<UsuarioEntity> findAllByUsuario( Pageable pageable, @Param("busca") String busca);
    @Query("""
     SELECT p FROM UsuarioEntity p
     ORDER BY p.id DESC""")
    Page<UsuarioEntity> findAllByUsuarioRegistrado(Pageable pageable);

      @Modifying
      @Query("""
              UPDATE UsuarioEntity p
              SET p.avatar=:nomeImagem WHERE p.id=:id
              """)
    void salvaImagem(@Param("nomeImagem") String nomeImagem,@Param("id") Long id);


    @Modifying
    @Query("""
              UPDATE UsuarioEntity p
              SET p.password=:password WHERE p.id=:usuarioId
              """)
    void salvaSenha(@Param("password") String password,@Param("usuarioId") Long usuarioId);
}
