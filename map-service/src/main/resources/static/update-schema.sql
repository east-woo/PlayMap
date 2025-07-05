ALTER TABLE locations
DROP
CONSTRAINT locations_keyword_id_fkey;

DROP TABLE keywords CASCADE;

DROP TABLE locations CASCADE;

DROP TABLE spatial_ref_sys CASCADE;