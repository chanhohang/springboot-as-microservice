package net.rc.lab.springboot.importer.common;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static Logger logger = Logger.getLogger(JobCompletionNotificationListener.class);

  @Override
  public void afterJob(JobExecution jobExecution) {
    logger.info(jobExecution.getJobConfigurationName() + ", status=" + jobExecution.getStatus());
  }
}
