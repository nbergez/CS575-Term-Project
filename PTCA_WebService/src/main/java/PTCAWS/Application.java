package main.java.PTCAWS;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {
    
    static final String DB_Connection_String = "jdbc:sqlserver://TARKIR\\DEV;databaseName=PTCA;integratedSecurity=false;user=ptca_service;password=password";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
