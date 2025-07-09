package com.mhp.billing_service.kafka;

import com.google.api.Billing;
import com.mhp.billing_service.model.BillingAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendStringEvent(BillingAccount billingAccount, String topic) {
        try {
            kafkaTemplate.send(topic, billingAccount);
        } catch (Exception e) {
            log.error("Error sending Patient Created event: {}", billingAccount.toString());
        }
    }
}
