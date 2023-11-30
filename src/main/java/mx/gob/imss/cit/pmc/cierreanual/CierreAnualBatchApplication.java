package mx.gob.imss.cit.pmc.cierreanual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class CierreAnualBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CierreAnualBatchApplication.class, args);
    }

}
