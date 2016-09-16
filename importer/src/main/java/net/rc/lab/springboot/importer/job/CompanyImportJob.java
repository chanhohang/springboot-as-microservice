package net.rc.lab.springboot.importer.job;

import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.importer.reader.CompanyItemReader;
import net.rc.lab.springboot.importer.writer.CompanyItemWriter;

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
public class CompanyImportJob extends AbstractImportJob<CompanyVo> {

  public static final String JOB_NAME = "importCompanyJob";

  @Autowired
  private CompanyItemReader reader;

  @Autowired
  private CompanyItemWriter writer;

  @Override
  protected ItemReader<CompanyVo> getReader() {
    return reader;
  }

  @Override
  protected ItemWriter<CompanyVo> getWriter() {
    return writer;
  }

  @Override
  protected String getJobName() {
    return JOB_NAME;
  }

  @Bean
  public Job importCompanyJob(JobBuilderFactory jobs) {
    return createJob(jobs);
  }
}
