package com.wavus.eastwoo.toyproject.playmap.service.impl;

import com.wavus.eastwoo.toyproject.playmap.dto.KeywordResponse;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationRequest;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.domain.Keyword;
import com.wavus.eastwoo.toyproject.playmap.domain.Location;
import com.wavus.eastwoo.toyproject.playmap.repository.KeywordRepository;
import com.wavus.eastwoo.toyproject.playmap.repository.LocationRepository;
import com.wavus.eastwoo.toyproject.playmap.service.LocationService;
import com.wavus.eastwoo.toyproject.playmap.utill.GeometryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final KeywordRepository keywordRepository;

    @Override
    public List<LocationResponse> findByKeyword(String keyword) {
        return locationRepository.findByKeywordName(keyword);
    }

    @Override
    public LocationResponse findById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));
        return toResponse(location);
    }

    @Override
    public List<LocationResponse> findWithinRadius(double lat, double lng, double radius) {
        return locationRepository.findWithinRadius(lat, lng, radius);
    }

    @Override
    public List<KeywordResponse> findAllKeywords() {
        return keywordRepository.findAll().stream()
                .map(k -> KeywordResponse.builder()
                        .id(k.getId())
                        .name(k.getName())
                        .locationCount(null) // 통계가 아닌 경우 null
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(LocationRequest request) {
        Keyword keyword = keywordRepository.findById(request.getKeywordId())
                .orElseThrow(() -> new IllegalArgumentException("해당 키워드를 찾을 수 없습니다."));

        Location location = Location.builder()
                .name(request.getName())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .address(request.getAddress())
                .keyword(keyword)
                .geom(GeometryUtils.createPoint(request.getLatitude(), request.getLongitude()))
                .build();

        locationRepository.save(location);
    }

    @Override
    @Transactional
    public void update(Long id, LocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));

        Keyword keyword = keywordRepository.findById(request.getKeywordId())
                .orElseThrow(() -> new IllegalArgumentException("해당 키워드를 찾을 수 없습니다."));

        location.setName(request.getName());
        location.setDescription(request.getDescription());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setAddress(request.getAddress());
        location.setKeyword(keyword);
        location.setGeom(GeometryUtils.createPoint(request.getLatitude(), request.getLongitude()));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public List<KeywordResponse> getKeywordStatistics() {
        return keywordRepository.findAll().stream()
                .map(k -> KeywordResponse.builder()
                        .id(k.getId())
                        .name(k.getName())
                        .locationCount(
                                locationRepository.findByKeywordName(k.getName()).stream().count()
                        )
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationResponse> findAll() {
        return locationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private LocationResponse toResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .build();
    }

    @Override
    public List<LocationResponse> findByBoundary(double latMin, double latMax, double lngMin, double lngMax) {
        List<Location> locations = locationRepository.findByLatLngBoundary(latMin, latMax, lngMin, lngMax);

        return locations.stream()
                .map(LocationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationResponse> findByBoundaryAndKeyword(double latMin, double latMax, double lngMin, double lngMax, String keyword) {
        return locationRepository.findByBoundaryAndKeyword(latMin, latMax, lngMin, lngMax, keyword)
                .stream()
                .map(LocationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationResponse> findNearby(double lat, double lng, int radiusInMeters) {
        return locationRepository.findNearby(lat, lng, radiusInMeters);
    }


}