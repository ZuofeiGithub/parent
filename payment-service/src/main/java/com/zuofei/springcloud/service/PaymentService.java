package com.zuofei.springcloud.service;

import com.zuofei.springcloud.entities.Payment;

public interface PaymentService {
    public int create(Payment payment);

    public Payment getPaymentById(Long id);
}
