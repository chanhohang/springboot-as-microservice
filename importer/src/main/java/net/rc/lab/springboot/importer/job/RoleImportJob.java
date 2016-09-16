package net.rc.lab.springboot.importer.job;

import net.rc.lab.springboot.entities.RoleVo;
import net.rc.lab.springboot.importer.reader.RoleItemReader;
import net.rc.lab.springboot.importer.writer.RoleItemWriter;

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
public class RoleImportJob extends AbstractImportJob<RoleVo> {

  public static final String JOB_NAME = "importRoleJob";

  @Autowired
  private RoleItemReader reader;

  @Autowired
  private RoleItemWriter writer;

  @Bean
  public Job importRoleJob(JobBuilderFactory jobs) {
    return createJob(jobs);
  }

  @Override
  protected ItemReader<RoleVo> getReader() {
    return reader;
  }

  @Override
  protected ItemWriter<RoleVo> getWriter() {
    return writer;
  }

  @Override
  protected String getJobName() {
    return JOB_NAME;
  }

}