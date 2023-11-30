package mx.gob.imss.cit.pmc.cierreanual.controller.impl;

import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;
import mx.gob.imss.cit.pmc.cierreanual.controller.CierreAnualBatchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Date;

@RestController
@RequestMapping("/msBatchCierreAnual")
public class CierreAnualBatchControllerImpl implements CierreAnualBatchController {

    Logger logger = LoggerFactory.getLogger("CierreAnualBatchControllerImpl");



    @Autowired
    @Qualifier("jobLauncherController")
    private SimpleJobLauncher jobLauncherController;

    @Autowired
    @Qualifier("generateReportesOOAD")
    private Job rtt;

    @Bean
    public SimpleJobLauncher jobLauncherController(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Override
    @PostMapping(value = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> execute() {
        logger.info("Se inicia la ejecucion del proceso");
        ResponseEntity<?> response;
        try {

            logger.info(MessageFormat.format("Se creo el header con el id {0}",
                    new Date().getTime()));
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong(CierreAnualBatchConstants.HEADER_ID, new Date().getTime())
                    .toJobParameters();
            JobExecution execution = jobLauncherController.run(rtt, jobParameters);
            response = new ResponseEntity<>(MessageFormat.format("El resultado de la ejecucion es: {0}",
                    execution.getStatus()), HttpStatus.OK);
            logger.info(MessageFormat.format("La ejecucion fue exitosa, fecha de inicio: {0}, fecha de fin: {1}",
                    execution.getStartTime(), execution.getEndTime()));
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity<>("Error al ejecutar la tarea", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    @PostMapping("/restart")
    public void restart() {


    }

    @Override
    @PostMapping("/cleanRegisters")
    public ResponseEntity<?> deleteOldRegisters() {
        return ResponseEntity.ok().build();
    }

}
