package com.mhp.patient_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class PatientServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(PatientServiceApplication.class, args);
	}

}
