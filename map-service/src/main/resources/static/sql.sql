CREATE TABLE place.keywords (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL UNIQUE
);



CREATE TABLE place.locations (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR(200) NOT NULL,
                                 description TEXT,
                                 keyword_id INTEGER NOT NULL REFERENCES place.keywords(id),
                                 latitude DOUBLE PRECISION NOT NULL,
                                 longitude DOUBLE PRECISION NOT NULL,
                                 address VARCHAR(300),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 geom geometry(Point, 4326)  -- PostGIS 지오메트리 컬럼
);

-- 키워드
INSERT INTO place.keywords (name) VALUES
                                      ('등산'), ('맛집'), ('관광지'), ('캠핑');

-- 장소 데이터 입력 (geom 포함)
INSERT INTO place.locations (name, description, keyword_id, latitude, longitude, address, geom)
VALUES
    ('북한산 둘레길', '서울의 대표적인 등산 코스', 1, 37.6584, 126.9931, '서울특별시 강북구', ST_SetSRID(ST_MakePoint(126.9931, 37.6584), 4326)),
    ('을지로 곱창골목', '곱창으로 유명한 서울 맛집 거리', 2, 37.5665, 127.0079, '서울특별시 중구', ST_SetSRID(ST_MakePoint(127.0079, 37.5665), 4326)),
    ('경복궁', '조선 시대의 궁궐로 대표 관광지', 3, 37.5796, 126.9770, '서울특별시 종로구', ST_SetSRID(ST_MakePoint(126.9770, 37.5796), 4326)),
    ('난지 캠핑장', '한강 근처의 도심형 캠핑장', 4, 37.5700, 126.8700, '서울특별시 마포구', ST_SetSRID(ST_MakePoint(126.8700, 37.5700), 4326));
