package br.com.itstoony.github.api.attornatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AttornatusApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttornatusApplication.class, args);
    }

}
