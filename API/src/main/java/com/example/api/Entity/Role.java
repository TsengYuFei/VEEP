package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`role`")
public class Role {

    @Id
    @Column(name = "roleID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleID;

    @Column(name = "name", nullable = false, length = 40, unique = true)
    private String name;
}
