package com.mendittzo.report.command.domain.repository;

import com.mendittzo.report.command.domain.aggregate.Report;

import java.util.List;

public interface ReportCommandRepository {

    Report save(Report report);

    void flush();
}
