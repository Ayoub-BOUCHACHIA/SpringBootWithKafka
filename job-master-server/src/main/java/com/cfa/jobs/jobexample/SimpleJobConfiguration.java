package com.cfa.jobs.jobexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cfa.jobs.jobexample.RPW.*;
import java.util.List;
import com.cfa.objects.lettre.Lettre;
import com.cfa.objects.lettre.LettreRepository;

/**
 * Configuration example for creating a job & Step with tasklet
 */
@Configuration
public class SimpleJobConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;
  @Autowired
  public StepBuilderFactory stepBuilderFactory;
  @Autowired
  private Source sources;

  @Autowired
  private LettreRepository repository;

  @Bean
  public Job simpleJob() {
    return jobBuilderFactory
      .get("simpleJob")
      .start(simpleStep())
      .build();
  }

  @Bean
  public Step simpleStep() {
    return this.stepBuilderFactory
      .get("simpleStep")
      .tasklet(new SimpleTaskletSource(sources))
      .build();
  }

  @Bean
  public Job simpleJobExo() {
    return jobBuilderFactory
      .get("simpleJobExo")
      .start(simpleStepExo())
      .next(stepBatchExo())
      .next(simpleStep())
      .next(PrintStep())
      .build();
  }

  @Bean
  public Step simpleStepExo() {
    return this.stepBuilderFactory
      .get("simpleStepExo")
      .tasklet(new SimpleTasklet(sources))
      .build();
  }


  public Step stepBatchExo() {
    return this.stepBuilderFactory.get("stepBatchExo")
    .<String, Lettre>chunk(1)
    .reader(new ItemReaderSimple())
    .processor(new ItemProcessorSimple())
    .writer(new ItemWriterSimple(this.repository))
    .build();
  }

  @Bean
  public Step PrintStep() {
    return this.stepBuilderFactory
      .get("PrintStep")
      .tasklet(new PrintTasklet(sources, this.repository))
      .build();
  }


}
