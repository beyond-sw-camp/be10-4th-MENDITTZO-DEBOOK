package com.mendittzo.report.command.domain.repository;

import com.mendittzo.report.command.domain.aggregate.Report;
import org.reactivestreams.Publisher;

import java.util.List;

public interface ReportCommandRepository {

    Report save(Report report);

    void flush();

    List<Report> findAllByChatroomId(Long chatroomId);

    List<Report> findAllByChatId(String chatId);
}
