package com.mhp.billing_service.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import com.mhp.billing_service.BillingServiceApplication;
import com.mhp.billing_service.kafka.KafkaProducer;
import com.mhp.billing_service.model.BillingAccount;
import com.mhp.billing_service.repository.BillingAccountRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private final BillingAccountRepository billingAccountRepository;
    private final KafkaProducer kafkaProducer;

    public BillingGrpcService(BillingAccountRepository billingAccountRepository, KafkaProducer kafkaProducer) {
        this.billingAccountRepository = billingAccountRepository;
        this.kafkaProducer = kafkaProducer;
    }
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                 StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", billingRequest.toString());

        // Business logic e.g save to database or perform calculation...

        BillingAccount newBillingAccount = new BillingAccount();
        newBillingAccount.setEamil(billingRequest.getEmail());
        newBillingAccount.setPatientId(UUID.fromString(billingRequest.getPatientId()));
        newBillingAccount.setName(billingRequest.getName());
        newBillingAccount.setStatus("ACTIVE");
        try {
            billingAccountRepository.save(newBillingAccount);
            BillingResponse response = BillingResponse.newBuilder()
                    .setAccountId(newBillingAccount.getId().toString())
                    .setStatus("ACTIVE")
                    .build();
            responseObserver.onNext(response);
        } catch (Exception e) {
            newBillingAccount.setStatus("INACTIVE");
            kafkaProducer.sendStringEvent(newBillingAccount, "patient-create-compensation");
            log.error("billing account create failed :{}" + "sent event to compensation topic", newBillingAccount);
        }

        responseObserver.onCompleted();
    }
}
