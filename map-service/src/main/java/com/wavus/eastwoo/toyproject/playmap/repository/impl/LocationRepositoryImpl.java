package com.wavus.eastwoo.toyproject.playmap.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.domain.Location;
import com.wavus.eastwoo.toyproject.playmap.domain.QKeyword;
import com.wavus.eastwoo.toyproject.playmap.domain.QLocation;
import com.wavus.eastwoo.toyproject.playmap.repository.LocationRepositoryCustom;
import com.wavus.eastwoo.toyproject.playmap.utill.GeometryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom {

    private final JPAQueryFactory query;
    QLocation location = QLocation.location;
    QKeyword keyword = QKeyword.keyword;

    @Override
    public List<LocationResponse> findByKeywordName(String keywordName) {
        return query.select(Projections.constructor(LocationResponse.class,
                        location.id, location.name, location.description,
                        location.latitude, location.longitude, location.address))
                .from(location)
                .join(location.keyword, keyword)
                .where(keyword.name.eq(keywordName))
                .fetch();
    }

    @Override
    public List<LocationResponse> findWithinRadius(double lat, double lng, double radius) {
        Point point = GeometryUtils.createPoint(lat, lng);
        return query.select(Projections.constructor(LocationResponse.class,
                        location.id, location.name, location.description,
                        location.latitude, location.longitude, location.address))
                .from(location)
                .where(Expressions.booleanTemplate(
                        "ST_DWithin({0}, {1}, {2})",
                        location.geom, point, radius
                ))
                .fetch();
    }

    @Override
    public List<Location> findByLatLngBoundary(double latMin, double latMax, double lngMin, double lngMax) {
        QLocation location = QLocation.location;

        return query
                .selectFrom(location)
                .where(
                        location.latitude.between(latMin, latMax),
                        location.longitude.between(lngMin, lngMax)
                )
                .fetch();
    }
}