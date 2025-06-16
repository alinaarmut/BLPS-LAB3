package org.example.service;

import jakarta.annotation.Resource;
import jakarta.resource.cci.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.dynomake.yookassa.Yookassa;
import me.dynomake.yookassa.model.Payment;
import org.example.dto.NewPaymentDto;
import org.example.dto.PaymentDTO;
import org.example.service.TransactionService.PaymentTransactionService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class YookassaScheduled implements Job {
    private final PaymentTransactionService paymentTransactionService;
    @Resource(name = "eis/YookassaConnectionFactory")
    private ConnectionFactory yookassaConnectionFactory;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {

                ApplicationContext appContext = (ApplicationContext) context.getScheduler()
                        .getContext().get("applicationContext");


                YookassaService yookassaService = appContext.getBean(YookassaService.class);
                PaymentTransactionService paymentService = appContext.getBean(PaymentTransactionService.class);


                NewPaymentDto paymentDto = (NewPaymentDto) context.getJobDetail()
                        .getJobDataMap().get("data");
                UUID paymentId = paymentDto.getPaymentId();


                String status = yookassaService.checkPaymentStatus(paymentId);
                log.info("Статус платежа {}: {}", paymentId, status);


                if ("succeeded".equals(status)) {
                    paymentService.updatePaymentStatus(paymentId);
                    context.getScheduler().deleteJob(context.getJobDetail().getKey());
                    log.info("Платеж {} подтвержден, задача удалена", paymentId);
                }
                else if ("canceled".equals(status) || "failed".equals(status)) {
                    paymentService.cancelPayment(paymentId);
                    context.getScheduler().deleteJob(context.getJobDetail().getKey());
                    log.info("Платеж {} отменен, задача удалена", paymentId);
                }

            } catch (Exception e) {
                log.error("Ошибка в задаче Quartz", e);
                throw new JobExecutionException(e, true);
            }
        }
    }



