package com.mhp.patient_service.aop;

import billing.BillingResponse;
import com.mhp.patient_service.model.Patient;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut for all exception methods in GlobalExceptionHandler
    @Pointcut("execution(* com.mhp.patient_service.exception.GlobalExceptionHandler..*(..)) && args(ex,..)")
    public void handleGlobalExceptionPointcut(Exception ex) {}

    // Pointcut for all methods in grpc service
    @Pointcut("execution(* com.mhp.patient_service.grpc..*(..))")
    public void GrpcServicePointcut() {}

    // Pointcut for all methods in KafkaProducer
//    @Pointcut("execution(* com.mhp.patient_service.kafka.KafkaProducer..*(..))")
//    public void kafkaProducerPointcut() {}

    // Log error message after global custom exception
    @After("handleGlobalExceptionPointcut(ex)")
    public void logCustomException(JoinPoint joinPoint, Exception ex) {
        log.warn("Exception handled by: {}, error message: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }

    // Log message after grpc methods execution
//    @After("GrpcServicePointcut()")
//    public void logGrpc(JoinPoint joinPoint) {
//        log.info("Connecting to: {} service at {}:{}", joinPoint.getSignature().getName(), serverAddress, serverPort);
//    }

    // Log message after grpc methods returnning
    @AfterReturning(pointcut = "GrpcServicePointcut()",  returning = "response")
    public void logGrpcReturn(JoinPoint joinPoint, BillingResponse response) {
        log.info("Received response from: {} via GRPC: {}", joinPoint.getSignature().getName(), response);
    }

    // log exception message after an exception been throw in methods of KafkaProducer
    // terminate the controller logic, not able to return response, will cause 500 error

//    @AfterThrowing(pointcut = "kafkaProducerPointcut()", throwing = "ex")
//    public void logExceptionInKafkaProducer(JoinPoint joinPoint, Exception ex) {
//        log.error("Error sending event: {}, error message: {}", joinPoint.getArgs(), ex.getMessage());
//    }
}
