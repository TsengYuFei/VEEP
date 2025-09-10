package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booth_staff_list")
public class BoothStaffList {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "staff")  // 被控方加mappingBy
    @JsonIgnore
    private Booth booth;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "booth_staff_list_user",  // 中間關聯表名稱
            joinColumns = {@JoinColumn(name = "listID")},  //this table's foreigner key in database
            inverseJoinColumns = {@JoinColumn(name = "userAccount")}  //User table's foreigner key in database
    )
    @JsonIgnore
    private Set<User> staffs = new HashSet<>();



    @PrePersist
    protected void onCreate() {
        staffs = new HashSet<>();
    }
}
