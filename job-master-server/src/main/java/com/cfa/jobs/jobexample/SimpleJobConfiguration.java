package com.cfa.jobs.jobexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
      .build();
  }

  @Bean
  public Step simpleStepExo() {
    return this.stepBuilderFactory
      .get("simpleStepExo")
      .tasklet(new SimpleTasklet(sources))
      .build();
  }

  // public Step stepBatchExo() {
    // return this.stepBuilderFactory.get("stepBatchExo")
    // .reader(new SimpleReader())
    // .processor(new SimpleProcessor())
    // .writer(new SimpleWriter())
    // .build();
  // }

}
