package com.habitask.utilitarian;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;


public class UserAuthentication implements Authentication {

    private String email;
    private Collection<SimpleGrantedAuthority> authorities;
    private boolean authenticated;

    public UserAuthentication(String email) {
        this.email = email;
        // Aquí, puedes agregar los roles/autoridades que necesita el usuario
        this.authorities = new ArrayList<SimpleGrantedAuthority>();
        this.authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Rol por defecto, puede ser dinámico
        this.authenticated = true;  // El usuario está autenticado si el token es válido
    }

    @Override
    public String getName() {
        return email;  // El nombre de usuario es el correo electrónico del usuario
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;  // Devolvemos las autoridades (roles) del usuario
    }

    @Override
    public Object getCredentials() {
        return null;  // No necesitamos las credenciales, ya que ya hemos validado el token JWT
    }

    @Override
    public Object getDetails() {
        return null;  // No se utilizan detalles adicionales en este caso
    }

    @Override
    public Object getPrincipal() {
        return email;  // El principal es el correo electrónico del usuario
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;  // El usuario está autenticado si el token es válido
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;  // Establecer la autenticación
    }
}
