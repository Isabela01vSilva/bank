package com.Isabela01vSilva.bank_isabela;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.Isabela01vSilva.bank_isabela")
@EntityScan(basePackages = "com.Isabela01vSilva.bank_isabela.domain")
public class BankIsabelaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankIsabelaApplication.class, args);
	}

}




