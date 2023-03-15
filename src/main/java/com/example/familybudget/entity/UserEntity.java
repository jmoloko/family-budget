package com.example.familybudget.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity{

    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "token")
    private String activationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public UserEntity(String email, String name, Role role, Status status) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
    }
}
