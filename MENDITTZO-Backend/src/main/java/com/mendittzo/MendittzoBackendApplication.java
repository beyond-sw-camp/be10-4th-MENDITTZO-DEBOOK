package com.mendittzo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MendittzoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MendittzoBackendApplication.class, args);
    }

}
