package com.example.wallet.scheduler;


import com.example.wallet.entity.Payment;
import com.example.wallet.entity.PaymentStatus;
import com.example.wallet.repository.PaymentRepository;
import com.example.wallet.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class SelectFromDbTaskScheduler {

    private final PaymentRepository paymentRepository;
    private JdbcTemplate jdbcTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public SelectFromDbTaskScheduler(PaymentRepository paymentRepository, JdbcTemplate jdbcTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "*/30 * * * * *")
    @SchedulerLock(name = "TaskSchedulerSelectFromDBTask")
    public void scheduledSelectFromDBTask() throws InterruptedException {

        log.info("Checking status of payment!");

        List<Payment> payments = paymentRepository.findAll();

        payments.forEach(
                payment -> {
                    if(payment.getStatus().equals(PaymentStatus.STATUS_PROCESS)) {
                        kafkaTemplate.send("payment", "Payment id: " + payment.getId() +
                                " and his payment status: " + payment.getStatus());
                    }
                }
        );
    }
}
