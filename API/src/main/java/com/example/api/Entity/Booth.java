package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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
    private String name;

    @Column(name = "avatar")
    private String avatar;

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

    @Column(name = "maxParticipants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "display", nullable = false)
    private Boolean display;

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
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "expoID")
    @JsonBackReference
    private Expo expo;

    // 攤位的位置編號(搭配UI地圖用)要加在這裡


    @PrePersist
    protected void onCreate() {
        if(name == null) name = "Booth";
        if(openMode == null) openMode = OpenMode.MANUAL;
        if(maxParticipants == null) maxParticipants = 1;
        if(openStatus == null) openStatus = false;
        if(display == null) display = true;
    }
}
