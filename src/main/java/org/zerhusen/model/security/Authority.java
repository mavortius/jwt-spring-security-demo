package org.zerhusen.model.security;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="SEGURANCA_PERFIS")
public class Authority {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name="NM_SEGURANCA_PERFIL")
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }
}