package com.wavus.eastwoo.toyproject.playmap.dto;

import com.wavus.eastwoo.toyproject.playmap.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LocationResponse {
    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;

    public static LocationResponse fromEntity(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .build();
    }
}