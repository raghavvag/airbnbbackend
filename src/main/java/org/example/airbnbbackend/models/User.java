package org.example.airbnbbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.airbnbbackend.models.enums.Role;

import java.util.Set;

@Entity
@Getter
@Setter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
