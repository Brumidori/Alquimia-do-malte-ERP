package com.les.erp_alquimia_do_malte.security;

import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import com.les.erp_alquimia_do_malte.domain.enums.TipoUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    public UUID getUsuarioId() {
        return usuario.getId();
    }

    public UUID getSetorId() {
        return usuario.getSetor() != null ? usuario.getSetor().getId() : null;
    }

    public TipoUsuario getTipo() {
        return usuario.getTipo();
    }

    public boolean isAdmin() {
        return TipoUsuario.ADMIN.equals(usuario.getTipo());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getAtivo()) && usuario.getExcludedAt() == null;
    }
}
