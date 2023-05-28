package com.cfa.jobs.jobexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.expression.common.LiteralExpression;



import com.cfa.jobs.jobexample.RPW.*;
import java.util.List;
import com.cfa.objects.lettre.Lettre;
import com.cfa.objects.lettre.LettreRepository;

/**
 * Configuration example for creating a job & Step with tasklet
 */
@EnableBatchIntegration
@EnableBatchProcessing
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


  // Job Exo 1
  @Bean
  public Job simpleJobExo() {
    return jobBuilderFactory
      .get("simpleJobExo")
      // .start(simpleStepExo()) // parite 1 convert string to lettre and send it to worker
      .start(stepBatchExo())
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

  @Bean
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



  //Exo 2 

  @Bean
  public Job simpleJobExo2() {
    return jobBuilderFactory
      .get("simpleJobExo2")
      .start(stepBatchExo2())
      .next(PrintStep())
      .build();
  }

  @Bean
  public Step stepBatchExo2() {
    return this.stepBuilderFactory.get("stepBatchExo2")
    .<String, Lettre>chunk(10)
    .reader(new ItemFileReaderSimple("job-master-server/src/main/resources/data.txt"))
    .processor(new ItemProcessorSimple())
    .writer(new ItemWriterSimple(this.repository))
    .build();
  }

  // ----------------------------------------------------------------
  // chunking configuration
  @Autowired
  private RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;

  public static String requestTopic = "request-topic";
  public static String responsTopic = "respons-topic";
  
  public static String idGroup = "grp1";

  @Autowired
  private KafkaTemplate kafkaTemplate;

  @Autowired
  private ConsumerFactory kafkaFactory;

  @Bean
  public DirectChannel requests(){ return new DirectChannel(); }
  @Bean
  public IntegrationFlow inboundFlow () {
  
      final ContainerProperties containerProps = new ContainerProperties(SimpleJobConfiguration.requestTopic);
      containerProps.setGroupId(SimpleJobConfiguration.idGroup);
      final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
      final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

      return IntegrationFlows.from(kafkaMessageChannel).channel(requests()).get();
  }


  @Bean
  public QueueChannel replies() { return new QueueChannel(); }
  @Bean
  public IntegrationFlow outboundFlow() {

      final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
      kafkaMessageHandler.setTopicExpression(new LiteralExpression(SimpleJobConfiguration.requestTopic));
      return IntegrationFlows
      .from(replies())
      .handle(kafkaMessageHandler)
      .get();

  }

  @Bean
  public Job simpleJobExo2Chunk() {
    return jobBuilderFactory
      .get("simpleJobExo2Chunk")
      .start(stepBatchExo2Chunk())
      .build();
  }

  @Bean
  public Step stepBatchExo2Chunk() {
    return this.managerStepBuilderFactory.get("stepBatchExo2Chunk")
    .chunk(10)
    .reader(new ItemFileReaderSimple("job-master-server/src/main/resources/data.txt"))
    .outputChannel(requests())
    .inputChannel(replies())
    .build();
  }

}
