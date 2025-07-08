package com.wavus.eastwoo.toyproject.playmap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Getter
@AllArgsConstructor
@Builder
@Setter
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
    private Point geom; // PostGIS의 POINT 정보
}