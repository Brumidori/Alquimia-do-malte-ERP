package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmailAndExcludedAtIsNull(String email);
    List<Usuario> findAllByExcludedAtIsNull();
    boolean existsByEmailAndExcludedAtIsNull(String email);
    boolean existsByCpfAndExcludedAtIsNull(String cpf);
}
