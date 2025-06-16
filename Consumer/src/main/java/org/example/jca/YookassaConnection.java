package org.example.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.*;
import lombok.NonNull;
import me.dynomake.yookassa.Yookassa;
import me.dynomake.yookassa.exception.BadRequestException;
import me.dynomake.yookassa.exception.UnspecifiedShopInformation;
import me.dynomake.yookassa.model.Payment;
import me.dynomake.yookassa.model.Refund;
import me.dynomake.yookassa.model.collecting.PaymentList;
import me.dynomake.yookassa.model.collecting.RefundList;
import me.dynomake.yookassa.model.request.PaymentRequest;
import me.dynomake.yookassa.model.request.RefundRequest;

import java.io.IOException;
import java.util.UUID;

public class YookassaConnection implements Connection, Yookassa {

    private final Yookassa yookassa;

    public YookassaConnection(int hostId, String hostKey) {
        yookassa = Yookassa.initialize(hostId, hostKey);
    }

    // Yookassa
    @Override
    public Payment createPayment(@NonNull PaymentRequest paymentRequest) throws UnspecifiedShopInformation, BadRequestException, IOException {
        return yookassa.createPayment(paymentRequest);
    }

    @Override
    public Payment getPayment(@NonNull UUID uuid) throws UnspecifiedShopInformation, BadRequestException, IOException {
        return yookassa.getPayment(uuid);
    }

    @Override
    public PaymentList getPayments() throws UnspecifiedShopInformation, BadRequestException, IOException {
        return yookassa.getPayments();
    }

    @Override
    public Refund createRefund(@NonNull RefundRequest refundRequest) throws UnspecifiedShopInformation, BadRequestException, IOException {
        return yookassa.createRefund(refundRequest);
    }

    @Override
    public Refund getRefund(@NonNull UUID uuid) throws UnspecifiedShopInformation, BadRequestException, IOException {
        return yookassa.getRefund(uuid);
    }

    @Override
    public RefundList getRefunds() throws UnspecifiedShopInformation, BadRequestException, IOException {
        return yookassa.getRefunds();
    }

    // Connection
    @Override
    public Interaction createInteraction() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public ResultSetInfo getResultSetInfo() throws ResourceException {
        return null;
    }

    @Override
    public void close() throws ResourceException {
    }
}
