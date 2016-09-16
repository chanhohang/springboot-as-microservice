package net.rc.lab.springboot.importer.job;

import net.rc.lab.springboot.entities.CountryVo;
import net.rc.lab.springboot.importer.reader.CountryItemReader;
import net.rc.lab.springboot.importer.writer.CountryItemWriter;

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
public class CountryImportJob extends AbstractImportJob<CountryVo> {

  private static final String JOB_NAME = "importCountryJob";

  @Autowired
  private CountryItemWriter writer;

  @Autowired
  private CountryItemReader reader;

  @Bean
  public Job importCountryJob(JobBuilderFactory jobs) {
    return createJob(jobs);
  }

  @Override
  protected ItemReader<CountryVo> getReader() {
    return reader;
  }

  @Override
  protected ItemWriter<CountryVo> getWriter() {
    return writer;
  }

  @Override
  protected String getJobName() {
    return JOB_NAME;
  }

}
