package com.mendittzo.restriction.infrastructure.repository;

import com.mendittzo.restriction.domain.aggregate.RestrictionHistory;
import com.mendittzo.restriction.domain.repository.RestrictionHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRestrictionHistoryRepository extends RestrictionHistoryRepository, JpaRepository<RestrictionHistory, Long> {
}
