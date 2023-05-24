package com.cfa.jobs.jobexample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.cfa.objects.lettre.Lettre;
import com.cfa.objects.lettre.LettreRepository;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * creation of the tasklet
 *
 */
@Slf4j
@RequiredArgsConstructor
@EnableBinding({Source.class})
public class PrintTasklet implements Tasklet, StepExecutionListener {
    private final Source sourceExo;
    private final LettreRepository repository;

    @Override
    public void beforeStep(final StepExecution parStepExecution) {
        log.debug("BeforeStep Asset tasket initialized for Print");
    }

    @Override
    public RepeatStatus execute(final StepContribution parStepContribution, final ChunkContext parChunkContext) {

        
        // get all data
        Collection<Lettre> lettres =  repository.findAll();

        final Message<Collection<Lettre>> locPartitionKey = MessageBuilder.withPayload(lettres)
                                                             .setHeader("custom_info", "startPrint")
                                                             .build();
        log.info("Print lettres : " + lettres);
        sourceExo.output().send(locPartitionKey);
        
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution parStepExecution) {
        log.debug("AfterStep Print executing tasklet");
        return ExitStatus.UNKNOWN;
    }
}
