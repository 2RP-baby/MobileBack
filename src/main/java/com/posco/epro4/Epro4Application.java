package com.posco.epro4;

// import java.util.Date;
// import java.util.TimeZone;

// import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;



@EnableEurekaClient
@SpringBootApplication
public class Epro4Application {

	// @PostConstruct
	// public void started(){
	// 	TimeZone.setDefault(TimeZone.getTimeZone("Asiz/Seoul"));
	// 	//TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	// 	System.out.println(new Date());
	// }

	public static void main(String[] args) {
		SpringApplication.run(Epro4Application.class, args);
	}

}