package com.wavus.eastwoo.toyproject.playmap.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LocationResponse {
    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
}