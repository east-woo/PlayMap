package com.wavus.eastwoo.toyproject.playmap.repository.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wavus.eastwoo.toyproject.playmap.domain.Location;
import com.wavus.eastwoo.toyproject.playmap.domain.QKeyword;
import com.wavus.eastwoo.toyproject.playmap.domain.QLocation;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.repository.LocationRepositoryCustom;
import com.wavus.eastwoo.toyproject.playmap.utill.GeometryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;

import java.util.List;
import com.querydsl.core.types.dsl.NumberTemplate;
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

    @Override
    public List<Location> findByBoundaryAndKeyword(double latMin, double latMax, double lngMin, double lngMax, String keyword) {
        QLocation location = QLocation.location;
        QKeyword keywordEntity = QKeyword.keyword;

        return query
                .selectFrom(location)
                .join(location.keyword, keywordEntity)
                .where(
                        location.latitude.between(latMin, latMax),
                        location.longitude.between(lngMin, lngMax),
                        keywordEntity.name.eq(keyword)
                )
                .fetch();
    }

    @Override
    public List<LocationResponse> findNearby(double lat, double lng, int radiusInMeters) {
        QLocation location = QLocation.location;

        return query
                .select(Projections.constructor(
                        LocationResponse.class,
                        location.id,
                        location.name,
                        location.description,
                        location.latitude,
                        location.longitude,
                        location.address
                ))
                .from(location)
                .where(haversineInRadius(location.latitude, location.longitude, lat, lng, radiusInMeters))
                .orderBy(distanceOrder(location.latitude, location.longitude, lat, lng))
                .fetch();
    }

    private BooleanExpression haversineInRadius(NumberPath<Double> latCol, NumberPath<Double> lngCol, double lat, double lng, int radius) {
        // 지구 반지름: 6371km, 반경은 미터 단위 → km 변환
        double radiusInKm = radius / 1000.0;
        NumberExpression<Double> distance = haversineExpression(latCol, lngCol, lat, lng);
        return distance.loe(radiusInKm);
    }

    private OrderSpecifier<Double> distanceOrder(NumberPath<Double> latCol, NumberPath<Double> lngCol, double lat, double lng) {
        Expression<Double> distance = haversineExpression(latCol, lngCol, lat, lng);
        return new OrderSpecifier<>(Order.ASC, distance);
    }

    private NumberExpression<Double> haversineExpression(
            NumberPath<Double> latCol, NumberPath<Double> lngCol,
            double lat, double lng
    ) {
        Expression<Double> latDiff = Expressions.numberTemplate(Double.class,
                "radians({0} - {1})", latCol, lat);
        Expression<Double> lngDiff = Expressions.numberTemplate(Double.class,
                "radians({0} - {1})", lngCol, lng);

        Expression<Double> a = Expressions.numberTemplate(Double.class,
                "pow(sin({0} / 2), 2) + cos(radians({1})) * cos(radians({2})) * pow(sin({3} / 2), 2)",
                latDiff, latCol, lat, lngDiff);

        Expression<Double> c = Expressions.numberTemplate(Double.class,
                "2 * atan2(sqrt({0}), sqrt(1 - {0}))", a);

        // 반환 타입을 NumberExpression으로 명시
        return Expressions.numberTemplate(Double.class, "6371 * {0}", c);
    }
}