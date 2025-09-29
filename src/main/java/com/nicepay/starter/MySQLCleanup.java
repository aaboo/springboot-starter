package com.nicepay.starter;

import org.springframework.stereotype.Component;

import com.mysql.jdbc.AbandonedConnecitonCleanupThread;

import jakarta.annotation.PreDestroy;

@Component
public class MySQLCleanup {

    @PreDestroy
    public void cleanUp() throws Exception {
        AbandonedConnecitonCleanupThread.shutdown();
        System.out.println("MySQL cleanup thread 종료 완료");
    }

}