package net.rc.lab.springboot.importer.job;

import net.rc.lab.springboot.importer.common.JobStore;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractImportJob<T> {

  @Autowired
  protected JobStore jobStore;

  @Autowired
  protected PlatformTransactionManager transactionManager;

  protected abstract ItemReader<T> getReader();

  protected abstract ItemWriter<T> getWriter();

  protected abstract String getJobName();

  protected int getChunk() {
    return 10;
  }

  private PassThroughItemProcessor<T> processor = new PassThroughItemProcessor<>();

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  /**
   * This is a method to help create job.
   *
   * @param jobs
   *          JobBuilderFactory to create job.
   * @return Job instance based on the child class implemented method.
   */
  protected Job createJob(JobBuilderFactory jobs) {
    // @formatter:off
    Step s1 = stepBuilderFactory.get("step1")
        .<T, T>chunk(getChunk())
        .reader(getReader())
        .processor(processor)
        .writer(getWriter())
        .transactionManager(transactionManager)
        .build();
    Job job = jobs.get(getJobName())
        .incrementer(new RunIdIncrementer())
        .flow(s1)
        .end()
        .build();
    jobStore.addJob(job.getName(), job);
    return job;
    // @formatter:on
  }

}
