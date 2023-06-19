package com.chaticat.authservice.persistence.entity;

import com.chaticat.authservice.persistence.entity.base.AbstractVersional;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends AbstractVersional {

    @Column(name = "username", nullable = false, length = 40)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "private")
    private boolean isPrivate = false;
}


