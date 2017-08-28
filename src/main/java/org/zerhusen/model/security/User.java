package org.zerhusen.model.security;

import org.hibernate.annotations.Type;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "SEGURANCA_USUARIOS")
public class User {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CD_USUARIO_SSHD", unique = true)
    private String username;

    @Transient
    private String password;

    @Type(type = "numeric_boolean")
    @Column(name = "ST_ATIVO")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "ID_SEGURANCA_PERFIL")
    private Authority authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public List<Authority> getAuthorities() {
        return Arrays.asList(authority);
    }
}