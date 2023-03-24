package com.example.wallet.scheduler;


import com.example.wallet.entity.Payment;
import com.example.wallet.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class SelectFromDbTaskScheduler {

    private final PaymentServiceImpl paymentRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SelectFromDbTaskScheduler(PaymentServiceImpl paymentRepository, JdbcTemplate jdbcTemplate) {
        this.paymentRepository = paymentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "*/30 * * * * *")
    @SchedulerLock(name = "TaskSchedulerSelectFromDBTask")
    public void scheduledSelectFromDBTask() throws InterruptedException{
        List<Payment> payments = jdbcTemplate.query("SELECT id, status FROM wallet.payments",
                new BeanPropertyRowMapper<Payment>(Payment.class));
        System.out.println("Running ShedLock task");
        System.out.println(payments);

        Thread.sleep(500);
    }

}
