package com.wavus.eastwoo.toyproject.playmap.repository;

import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;

import java.util.List;

public interface LocationRepositoryCustom {
    List<LocationResponse> findByKeywordName(String keywordName);
}