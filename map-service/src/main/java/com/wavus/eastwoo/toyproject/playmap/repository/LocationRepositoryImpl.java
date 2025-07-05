package com.wavus.eastwoo.toyproject.playmap.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wavus.eastwoo.toyproject.playmap.dto.LocationResponse;
import com.wavus.eastwoo.toyproject.playmap.entity.QKeyword;
import com.wavus.eastwoo.toyproject.playmap.entity.QLocation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom {

    private final JPAQueryFactory query;

    QLocation location = QLocation.location;
    QKeyword keyword = QKeyword.keyword;

    @Override
    public List<LocationResponse> findByKeywordName(String keywordName) {
        return query
            .select(Projections.constructor(LocationResponse.class,
                location.id,
                location.name,
                location.description,
                location.latitude,
                location.longitude,
                location.address
            ))
            .from(location)
            .join(location.keyword, keyword)
            .where(keyword.name.eq(keywordName))
            .fetch();
    }
}