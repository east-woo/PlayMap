package com.wavus.eastwoo.toyproject.playmap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    private Double latitude;
    private Double longitude;
    private String address;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private String geom; // PostGIS의 POINT 정보
}