package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("permissaoService")
public class PermissaoService {
    public boolean hasPermission(Authentication authentication, String... permissoes) {
        return Arrays.stream(permissoes)
                .anyMatch(p -> authentication.getAuthorities()
                        .contains(new SimpleGrantedAuthority(p)));
    }
}
