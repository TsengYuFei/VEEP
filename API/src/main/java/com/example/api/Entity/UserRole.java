package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`user_role`")
public class UserRole {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "userAccount", referencedColumnName = "userAccount", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "roleID", nullable = false)
    private Role role;
}
