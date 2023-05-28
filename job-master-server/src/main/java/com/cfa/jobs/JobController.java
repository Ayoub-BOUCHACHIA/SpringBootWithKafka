package com.cfa.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cfa.objects.lettre.LettreRepository;
import com.cfa.objects.lettre.Lettre;
import java.util.Collection;
/**
 * Controller to launch jobs from an API call
 */
@RestController
@Slf4j
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/v1/jobcontroller")
@RequiredArgsConstructor
public class JobController {

  private final JobLauncher jobLauncher;
  private final Job simpleJob;
  private final Job simpleJobExo;
  private final Job simpleJobExo2;
  private final Job simpleJobExo2Chunk; 
  private final LettreRepository repository;

  @RequestMapping("/example")
  public void simpleJob(@RequestParam(value = "label") final String label) {
    runJobB(this.simpleJob, label);
  }

  private void runJobB(final Job parJob, final String label) {
    final JobParameters locParamJobParameters = new JobParametersBuilder()
      .addParameter("value", new JobParameter(label))
      .addParameter("time", new JobParameter(System.currentTimeMillis()))
      .toJobParameters();

    try {
      log.info("[Job] running . . .");
      jobLauncher.run(parJob, locParamJobParameters);
    } catch (Exception ex) {
      log.error("[RUN JOB ERROR] : " + ex.getMessage());
    }
  }


  @RequestMapping("/exampleExo") // http://127.0.0.1:7777/v1/jobcontroller/exampleExo?label=example
  public Collection<Lettre> simpleJobExo(@RequestParam(value = "label") final String label) {
    runJobBExo(this.simpleJobExo, label);
    return repository.findAll();
  }

  private void runJobBExo(final Job parJob, final String label) {
    final JobParameters locParamJobParametersExo = new JobParametersBuilder()
      .addParameter("value", new JobParameter(label))
      .addParameter("time", new JobParameter(System.currentTimeMillis()))
      .toJobParameters();

    try {
      log.info("[JobExo] running . . .");
      jobLauncher.run(parJob, locParamJobParametersExo);
    } catch (Exception ex) {
      log.error("[RUN JOBExo ERROR] : " + ex.getMessage());
    }
  }


  @RequestMapping("/exampleExo2") // http://127.0.0.1:7777/v1/jobcontroller/exampleExo2
  public Collection<Lettre> simpleJobExo2() {
    runJobBExo2(this.simpleJobExo2);
    return repository.findAll();
  }

  private void runJobBExo2(final Job parJob) {
    final JobParameters locParamJobParametersExo2 = new JobParametersBuilder()
      .addParameter("time", new JobParameter(System.currentTimeMillis()))
      .toJobParameters();

    try {
      log.info("[JobExo2] running . . .");
      jobLauncher.run(parJob, locParamJobParametersExo2);
    } catch (Exception ex) {
      log.error("[RUN JOBExo2 ERROR] : " + ex.getMessage());
    }
  }


  @RequestMapping("/exampleExo2Chunk") // http://127.0.0.1:7777/v1/jobcontroller/exampleExo2Chunk
  public Collection<Lettre> simpleJobExo2Chunk() {
    simpleJobExo2Chunk(this.simpleJobExo2Chunk);
    return repository.findAll();
  }

  private void simpleJobExo2Chunk(final Job parJob) {
    final JobParameters locParamJobParametersExo2Chunk = new JobParametersBuilder()
      .addParameter("time", new JobParameter(System.currentTimeMillis()))
      .toJobParameters();

    try {
      log.info("[JobExo2Chunk] running . . .");
      jobLauncher.run(parJob, locParamJobParametersExo2Chunk);
    } catch (Exception ex) {
      log.error("[RUN JobExo2Chunk ERROR] : " + ex.getMessage());
    }
  }
}