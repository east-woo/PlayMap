package com.wavus.eastwoo.toyproject.playmap.repository;

import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.domain.Location;

import java.util.List;

// LocationRepositoryCustom
public interface LocationRepositoryCustom {
    List<LocationResponse> findByKeywordName(String keywordName);
    List<LocationResponse> findWithinRadius(double lat, double lng, double radius);
    List<Location> findByLatLngBoundary(double latMin, double latMax, double lngMin, double lngMax);
}