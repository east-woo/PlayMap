package com.wavus.eastwoo.toyproject.playmap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequest {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private Long keywordId;
}