package com.wavus.eastwoo.toyproject.playmap.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.entity.QKeyword;
import com.wavus.eastwoo.toyproject.playmap.entity.QLocation;
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
}