package com.mendittzo.report.command.infrastructure.repository;

import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.repository.ReportCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReportRepository extends ReportCommandRepository, JpaRepository<Report, Long> {
}
