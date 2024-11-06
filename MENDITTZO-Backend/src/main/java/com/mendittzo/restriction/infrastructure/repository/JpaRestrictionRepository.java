package com.mendittzo.restriction.infrastructure.repository;

import com.mendittzo.restriction.domain.aggregate.Restriction;
import com.mendittzo.restriction.domain.repository.RestrictionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRestrictionRepository extends RestrictionRepository, JpaRepository<Restriction, Long> {
}
