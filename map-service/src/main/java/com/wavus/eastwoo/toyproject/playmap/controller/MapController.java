package com.wavus.eastwoo.toyproject.playmap.controller;

import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final LocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<LocationResponse>> getLocations(@RequestParam String keyword) {
        List<LocationResponse> result = locationService.getLocationsByKeyword(keyword);
        return ResponseEntity.ok(result);
    }
}