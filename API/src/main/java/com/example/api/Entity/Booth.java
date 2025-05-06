package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "collaborator", nullable = false, unique = true)
    private CollaboratorList collaborator;
}
