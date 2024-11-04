package com.mendittzo.report.command.domain.repository;

import com.mendittzo.report.command.domain.aggregate.Report;

public interface ReportCommandRepository {
    Report save(Report report);
}
