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
@Table(name = "expo")
public class Expo {

    @Id
    @Column(name = "expoID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expoID;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Lob
    @Column(name = "introduction")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "openMode", nullable = false)
    private OpenMode openMode;

    @Column(name = "openStatus")
    private Boolean openStatus;

    @Column(name = "openStart")
    private LocalDateTime openStart;

    @Column(name = "openEnd")
    private LocalDateTime openEnd;

    @Column(name = "accessCode", length = 20)
    private String accessCode;

    @Column(name = "maxParticipants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "display", nullable = false)
    private Boolean display;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "collaborator", nullable = false, unique = true)  // 主控方加JoinColumn
    private ExpoCollaboratorList collaborator;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "blacklist", nullable = false, unique = true)
    private Blacklist blacklist;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "whitelist", nullable = false, unique = true)
    private Whitelist whitelist;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "expo_tag",
            joinColumns = {@JoinColumn(name = "expoID")},
            inverseJoinColumns = {@JoinColumn(name = "tagID")}
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "expo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<Booth> boothList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;


    @PrePersist
    protected void onCreate() {
        if(name == null) name = "Exhibition";
        if(price == null) price = 0;
        if(openMode == null) openMode = OpenMode.MANUAL;
        if(openStatus == null) openStatus = false;
        if(maxParticipants == null) maxParticipants = 50;
        if(display == null) display = true;
    }
}
