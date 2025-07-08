package com.wavus.eastwoo.toyproject.playmap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordResponse {
    private Long id;
    private String name;
    private Long locationCount; // keyword별 장소 수
}
