package com.wavus.eastwoo.toyproject.playmap.service;

import com.wavus.eastwoo.toyproject.playmap.dto.KeywordResponse;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationRequest;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;

import java.util.List;

public interface LocationService {
    List<LocationResponse> findByKeyword(String keyword);
    LocationResponse findById(Long id);
    List<LocationResponse> findWithinRadius(double lat, double lng, double radius);
    List<KeywordResponse> findAllKeywords();
    void save(LocationRequest request);
    void update(Long id, LocationRequest request);
    void delete(Long id);
    List<KeywordResponse> getKeywordStatistics();
    List<LocationResponse> findAll();
}