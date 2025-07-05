package com.wavus.eastwoo.toyproject.playmap.repository;

import com.wavus.eastwoo.toyproject.playmap.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {
}