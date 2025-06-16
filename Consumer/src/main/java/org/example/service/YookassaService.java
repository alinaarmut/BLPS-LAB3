package org.example.service;

import jakarta.annotation.Resource;
import jakarta.resource.cci.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.dynomake.yookassa.Yookassa;
import me.dynomake.yookassa.model.Amount;
import me.dynomake.yookassa.model.Confirmation;
import org.example.dto.NewPaymentDto;
import org.example.entity.primary.Booking;
import me.dynomake.yookassa.model.Payment;
import org.example.entity.secondary.Offer;
import org.example.repository.secondary.OfferRepository;
import org.example.service.TransactionService.PaymentTransactionService;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import me.dynomake.yookassa.model.request.PaymentRequest;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class YookassaService {
    private final Scheduler scheduler;
    @Resource(name = "eis/YookassaConnectionFactory")
    private ConnectionFactory yookassaConnectionFactory;
    private final ApplicationContext applicationContext;

    @SneakyThrows
    public NewPaymentDto createNewPaymentForOffer(Booking booking ) {
        log.info("Создание платежа для бронирования {}", booking.getId());
        Yookassa yookassa = (Yookassa) yookassaConnectionFactory.getConnection();
        log.debug("Получено соединение с ЮKassa");
        double price = Optional.ofNullable(booking.getPricePerNight()).orElse(500.0);
        Amount amount = new Amount(Double.toString(price), "RUB");
        Payment payment = yookassa.createPayment(PaymentRequest.builder()

                .description("Платеж за бронирование #" + booking.getId())
                .amount(amount)
                .confirmation(Confirmation.builder()
                        .type("redirect")
                        .returnUrl("")
                        .build())
                .build());

        var newPayment = new NewPaymentDto(booking.getId(),
                price,
                payment.getId(),
                payment.getConfirmation().getConfirmationUrl());
        scheduler.getContext().put("applicationContext", applicationContext);
        startPaymentPolling(newPayment);
        log.info("ПЛАТЕЖ СОЗДАН В ЮКАССА");
        return newPayment;
    }

    @SneakyThrows
    public void startPaymentPolling(NewPaymentDto newPayment) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", newPayment);

        JobDetail jobDetail = JobBuilder.newJob(YookassaScheduled.class)
                .withIdentity("paymentPollingJob_" + newPayment.getPaymentId())
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("paymentPollingTrigger_" + newPayment.getPaymentId())
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
    @SneakyThrows
    public String checkPaymentStatus(UUID paymentId) {
        Yookassa yookassa = (Yookassa) yookassaConnectionFactory.getConnection();
        Payment payment = yookassa.getPayment(UUID.fromString(paymentId.toString()));
        return payment.getStatus();
    }


}
