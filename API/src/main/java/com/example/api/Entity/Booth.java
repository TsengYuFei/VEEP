package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booth")
public class Booth {

    @Id
    @Column(name = "boothID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boothID;

    @Column(name = "name", length = 30)
    private String name = "Booth";

    @Column(name = "avatar")
    private String avatar;

    @Lob
    @Column(name = "introduction")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "openMode", nullable = false)
    private OpenMode openMode = OpenMode.MANUAL;

    @Column(name = "openStatus")
    private Boolean openStatus = false;

    @Column(name = "openStart")
    private LocalDateTime openStart;

    @Column(name = "openEnd")
    private LocalDateTime openEnd;

    @Column(name = "maxParticipants", nullable = false)
    private Integer maxParticipants = 1;

    @Column(name = "display", nullable = false)
    private Boolean display = true;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "collaborator", nullable = false, unique = true)  // 主控方加JoinColumn
    @JsonManagedReference
    private BoothCollaboratorList collaborator;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "staff", nullable = false, unique = true)  // 主控方加JoinColumn
    @JsonManagedReference
    private BoothStaffList staff;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "booth_tag",
            joinColumns = {@JoinColumn(name = "boothID")},
            inverseJoinColumns = {@JoinColumn(name = "tagID")}
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "booth", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Content> contentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private User owner;
}
