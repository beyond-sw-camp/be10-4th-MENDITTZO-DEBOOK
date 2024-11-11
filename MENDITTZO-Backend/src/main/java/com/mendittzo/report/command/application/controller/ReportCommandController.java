package com.mendittzo.report.command.application.controller;

import com.mendittzo.common.exception.SuccessCode;
import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.application.service.ReportCommandService;
import com.mendittzo.security.util.UserUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "report", description = "신고 API")
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportCommandController {

    private final ReportCommandService reportCommandService;

    @PostMapping
    public ResponseEntity<String> requestReport(@RequestBody ReportRequestDTO reportRequestDTO) {

        reportRequestDTO.setReporterUserId(UserUtil.getCurrentUserLoginId());

        reportCommandService.requestReport(reportRequestDTO);

        return ResponseEntity.ok(SuccessCode.REPORT_CREATE_SUCCESS.getMessage());
    }
}
