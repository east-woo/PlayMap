package com.wavus.eastwoo.toyproject.playmap.controller;

import com.wavus.eastwoo.toyproject.playmap.dto.KeywordResponse;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationRequest;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final LocationService locationService;

    // 키워드로 장소 검색
    @GetMapping("/locations")
    public ResponseEntity<List<LocationResponse>> getLocationsByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(locationService.findByKeyword(keyword));
    }

    // 특정 장소 조회
    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    // 반경 내 장소 검색
    @GetMapping("/locations/within")
    public ResponseEntity<List<LocationResponse>> getLocationsWithinRadius(@RequestParam double lat,
                                                                           @RequestParam double lng,
                                                                           @RequestParam double radius) {
        return ResponseEntity.ok(locationService.findWithinRadius(lat, lng, radius));
    }

    // 키워드 전체 조회
    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordResponse>> getKeywords() {
        return ResponseEntity.ok(locationService.findAllKeywords());
    }

    // 장소 생성
    @PostMapping("/locations")
    public ResponseEntity<Void> createLocation(@RequestBody LocationRequest request) {
        locationService.save(request);
        return ResponseEntity.ok().build();
    }

    // 장소 수정
    @PutMapping("/locations/{id}")
    public ResponseEntity<Void> updateLocation(@PathVariable Long id, @RequestBody LocationRequest request) {
        locationService.update(id, request);
        return ResponseEntity.ok().build();
    }

    // 장소 삭제
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.ok().build();
    }

    // 키워드별 장소 수 통계
    @GetMapping("/keywords/statistics")
    public ResponseEntity<List<KeywordResponse>> getKeywordStatistics() {
        return ResponseEntity.ok(locationService.getKeywordStatistics());
    }

    // 장소 전체 조회
    @GetMapping("/locations/all")
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(locationService.findAll());
    }
}
