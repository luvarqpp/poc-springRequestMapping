package sk.qpp.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PocMain {
    public static void main(String[] args) {
        SpringApplication.run(sk.qpp.poc.PocMain.class, args);
    }
}