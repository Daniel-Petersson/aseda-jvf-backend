package se.leiden.asedajvf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "se.leiden.asedajvf")
public class AsedaJvfApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsedaJvfApplication.class, args);
    }

}
