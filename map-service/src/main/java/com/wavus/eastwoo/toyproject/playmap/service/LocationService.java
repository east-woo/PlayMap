package com.wavus.eastwoo.toyproject.playmap.service;

import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationResponse> getLocationsByKeyword(String keyword) {
        return locationRepository.findByKeywordName(keyword);
    }
}