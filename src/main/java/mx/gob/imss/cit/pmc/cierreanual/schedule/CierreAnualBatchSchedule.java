package mx.gob.imss.cit.pmc.cierreanual.schedule;

import mx.gob.imss.cit.pmc.cierreanual.service.RestartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CierreAnualBatchSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CierreAnualBatchSchedule.class);

    @Autowired
    @Qualifier("jobLauncherSchedule")
    private SimpleJobLauncher jobLauncherSchedule;

    @Autowired
    @Qualifier("generateReportesOOAD")
    private Job cierreAnualJob;


    @Bean
    public SimpleJobLauncher jobLauncherSchedule(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Scheduled(cron = "${cron.expression.repcierreanualBatchCleaner}")
    public void cleanOldRegisters() {

    }

    @Scheduled(cron = "${cron.expression.repcierreanualBatchRestart}")
    public void restartService() {

    }

    @Scheduled(cron = "${cron.expression.repcierreanualBatch}")
    public void startJob() {
        logger.info("Se inicia la ejecucion del proceso");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
