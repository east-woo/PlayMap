package com.wavus.eastwoo.toyproject.playmap.repository;

import com.wavus.eastwoo.toyproject.playmap.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

// KeywordRepository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}