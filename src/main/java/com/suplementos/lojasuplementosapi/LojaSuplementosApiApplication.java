package com.suplementos.lojasuplementosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class LojaSuplementosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaSuplementosApiApplication.class, args);
	}

}
