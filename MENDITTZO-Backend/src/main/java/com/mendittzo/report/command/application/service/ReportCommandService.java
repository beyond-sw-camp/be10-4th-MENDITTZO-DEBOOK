package com.mendittzo.report.command.application.service;

import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.repository.ReportCommandRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportCommandService {

    private final ReportCommandRepository reportCommandRepository;

    @Transactional
    public void requestReport(ReportRequestDTO reportRequestDTO) {

        Report newReport = reportRequestDTO.toEntity();

        reportCommandRepository.save(newReport);
    }
}
