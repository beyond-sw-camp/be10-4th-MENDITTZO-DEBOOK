package com.mendittzo.report.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "report", description = "신고 API")
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportCommandController {

}
