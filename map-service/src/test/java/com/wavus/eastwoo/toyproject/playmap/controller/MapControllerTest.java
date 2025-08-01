package com.wavus.eastwoo.toyproject.playmap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavus.eastwoo.toyproject.playmap.dto.KeywordResponse;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationRequest;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(MapController.class)
class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 키워드로_장소_검색() throws Exception {
        List<LocationResponse> dummyList = List.of(
                LocationResponse.builder()
                        .id(1L)
                        .name("북한산 둘레길")
                        .description("서울의 대표적인 등산 코스")
                        .latitude(37.6584)
                        .longitude(126.9931)
                        .address("서울특별시 강북구")
                        .build()
        );

        given(locationService.findByKeyword("등산")).willReturn(dummyList);

        mockMvc.perform(get("/api/map/locations")
                        .param("keyword", "등산"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("북한산 둘레길"));
    }

    @Test
    void 특정_장소_조회() throws Exception {
        LocationResponse location = LocationResponse.builder()
                .id(1L)
                .name("경복궁")
                .description("조선 시대의 궁궐로 대표 관광지")
                .latitude(37.5796)
                .longitude(126.9770)
                .address("서울특별시 종로구")
                .build();

        given(locationService.findById(1L)).willReturn(location);

        mockMvc.perform(get("/api/map/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("경복궁"));
    }

    @Test
    void 반경_내_장소_검색() throws Exception {
        List<LocationResponse> list = List.of(LocationResponse.builder()
                .id(1L)
                .name("난지 캠핑장")
                .description("한강 근처의 도심형 캠핑장")
                .latitude(37.57)
                .longitude(126.87)
                .address("서울특별시 마포구")
                .build());

        given(locationService.findWithinRadius(37.57, 126.87, 1000)).willReturn(list);

        mockMvc.perform(get("/api/map/locations/within")
                        .param("lat", "37.57")
                        .param("lng", "126.87")
                        .param("radius", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("난지 캠핑장"));
    }

    @Test
    void 장소_생성() throws Exception {
        LocationRequest request = LocationRequest.builder()
                .name("테스트 장소")
                .description("설명")
                .latitude(37.0)
                .longitude(127.0)
                .address("서울시 어딘가")
                .keywordId(1L)
                .build();

        mockMvc.perform(post("/api/map/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void 장소_수정() throws Exception {
        LocationRequest request = LocationRequest.builder()
                .name("수정된 장소")
                .description("수정 설명")
                .latitude(37.1)
                .longitude(127.1)
                .address("서울시 다른 곳")
                .keywordId(2L)
                .build();

        mockMvc.perform(put("/api/map/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void 장소_삭제() throws Exception {
        mockMvc.perform(delete("/api/map/locations/1"))
                .andExpect(status().isOk());
    }

    @Test
    void 키워드_조회() throws Exception {
        List<KeywordResponse> keywords = List.of(
                new KeywordResponse(1L, "산책", 3L),
                new KeywordResponse(2L, "맛집", 5L)
        );

        given(locationService.findAllKeywords()).willReturn(keywords);

        mockMvc.perform(get("/api/map/keywords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("산책"))
                .andExpect(jsonPath("$[1].locationCount").value(5));
    }

    @Test
    void 키워드별_장소_통계() throws Exception {
        List<KeywordResponse> stats = List.of(
                new KeywordResponse(1L, "산책", 3L),
                new KeywordResponse(2L, "맛집", 10L)
        );

        given(locationService.getKeywordStatistics()).willReturn(stats);

        mockMvc.perform(get("/api/map/keywords/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].locationCount").value(10));
    }

    @Test
    void 장소_전체_조회() throws Exception {
        List<LocationResponse> all = List.of(
                LocationResponse.builder()
                        .id(1L).name("경복궁").build(),
                LocationResponse.builder()
                        .id(2L).name("청와대").build()
        );

        given(locationService.findAll()).willReturn(all);

        mockMvc.perform(get("/api/map/locations/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void 지도_경계_내_장소_조회() throws Exception {
        List<LocationResponse> responseList = List.of(
                LocationResponse.builder()
                        .id(1L)
                        .name("한강공원")
                        .description("서울의 대표적인 공원")
                        .latitude(37.55)
                        .longitude(126.97)
                        .address("서울특별시 마포구")
                        .build(),
                LocationResponse.builder()
                        .id(2L)
                        .name("여의도 공원")
                        .description("서울 도심 속 공원")
                        .latitude(37.52)
                        .longitude(126.95)
                        .address("서울특별시 영등포구")
                        .build()
        );

        // given
        given(locationService.findByBoundary(37.50, 37.56, 126.90, 127.00))
                .willReturn(responseList);

        // when & then
        mockMvc.perform(get("/api/map/locations/in-boundary")
                        .param("latMin", "37.50")
                        .param("latMax", "37.56")
                        .param("lngMin", "126.90")
                        .param("lngMax", "127.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("한강공원"))
                .andExpect(jsonPath("$[1].name").value("여의도 공원"));
    }

    @Test
    void 좌표_범위와_키워드로_장소_검색() throws Exception {
        List<LocationResponse> list = List.of(
                LocationResponse.builder()
                        .id(1L)
                        .name("한강 산책길")
                        .description("한강변을 따라 걷기 좋은 코스")
                        .latitude(37.55)
                        .longitude(126.97)
                        .address("서울특별시")
                        .build()
        );

        given(locationService.findByBoundaryAndKeyword(37.0, 38.0, 126.0, 127.0, "산책")).willReturn(list);

        mockMvc.perform(get("/api/map/locations/in-boundary-with-keyword")
                        .param("latMin", "37.0")
                        .param("latMax", "38.0")
                        .param("lngMin", "126.0")
                        .param("lngMax", "127.0")
                        .param("keyword", "산책"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("한강 산책길"));
    }

    @Test
    void 거리_기반_장소_조회() throws Exception {
        // given
        List<LocationResponse> nearbyLocations = List.of(
                LocationResponse.builder()
                        .id(1L)
                        .name("한강공원")
                        .description("서울의 대표적인 공원")
                        .latitude(37.55)
                        .longitude(126.97)
                        .address("서울특별시 용산구")
                        .build(),
                LocationResponse.builder()
                        .id(2L)
                        .name("여의도 공원")
                        .description("도심 속 녹지 공간")
                        .latitude(37.53)
                        .longitude(126.92)
                        .address("서울특별시 영등포구")
                        .build()
        );

        given(locationService.findNearby(37.55, 126.97, 2000)).willReturn(nearbyLocations);

        // when + then
        mockMvc.perform(get("/api/map/locations/nearby")
                        .param("lat", "37.55")
                        .param("lng", "126.97")
                        .param("radius", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("한강공원"))
                .andExpect(jsonPath("$[1].name").value("여의도 공원"));
    }
}