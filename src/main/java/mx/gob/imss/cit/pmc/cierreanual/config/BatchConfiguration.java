package mx.gob.imss.cit.pmc.cierreanual.config;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;
import mx.gob.imss.cit.pmc.cierreanual.task.GeneraReporteAnualOOAD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = { "mx.gob.imss.cit.pmc.cierreanual" })
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BatchConfiguration extends DefaultBatchConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

   

    @Autowired
    private GeneraReporteAnualOOAD generaReporteAnualOOAD;


    @Bean
    @NonNull
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @SneakyThrows
    @NonNull
    @Bean
    public JobRepository getJobRepository() {
        return Objects.requireNonNull(new MapJobRepositoryFactoryBean(getTransactionManager()).getObject());
    }

    @Bean(name = "generateReportesOOAD")
    public Job chargeInitialRttMovements() {
        return jobBuilderFactory.get("generateReportesOOAD")
                .incrementer(new RunIdIncrementer())
                .start(stepReporteOOADNacional())
                .build();
    }


    @Bean
    public Step stepReporteOOADNacional() {
        return stepBuilderFactory.get("stepGeneraReporteAnualOOAD").tasklet(generaReporteAnualOOAD).build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CierreAnualBatchConstants.POOL_SIZE);
        taskExecutor.setMaxPoolSize(CierreAnualBatchConstants.POOL_SIZE);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }


}
