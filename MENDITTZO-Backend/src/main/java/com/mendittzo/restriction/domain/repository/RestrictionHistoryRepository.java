package com.mendittzo.restriction.domain.repository;

import com.mendittzo.restriction.domain.aggregate.RestrictionHistory;
import com.mendittzo.restriction.domain.aggregate.RestrictionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RestrictionHistoryRepository {
    List<RestrictionHistory> findAllByEndDatetimeBeforeAndRestrictionStatus(LocalDateTime now, RestrictionStatus restrictionStatus);

    RestrictionHistory save(RestrictionHistory restrictionHistory);
}
