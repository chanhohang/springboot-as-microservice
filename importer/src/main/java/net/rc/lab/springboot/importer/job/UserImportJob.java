package net.rc.lab.springboot.importer.job;

import net.rc.lab.springboot.entities.UserVo;
import net.rc.lab.springboot.importer.reader.UserItemReader;
import net.rc.lab.springboot.importer.writer.UserItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class UserImportJob extends AbstractImportJob<UserVo> {

  public static final String JOB_NAME = "importUserJob";

  @Autowired
  private UserItemReader reader;

  @Autowired
  private UserItemWriter writer;

  @Bean
  public Job importUserJob(JobBuilderFactory jobs) {
    return createJob(jobs);
  }

  @Override
  protected ItemReader<UserVo> getReader() {
    return reader;
  }

  @Override
  protected ItemWriter<UserVo> getWriter() {
    return writer;
  }

  @Override
  protected String getJobName() {
    return JOB_NAME;
  }

}