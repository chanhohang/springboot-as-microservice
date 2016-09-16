package net.rc.lab.springboot.importer.console;

import net.rc.lab.springboot.importer.common.ImporterJobConstants;
import net.rc.lab.springboot.importer.common.JobStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ImporterConsole {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private JobStore jobStore;

  private Logger logger = LoggerFactory.getLogger(ImporterConsole.class);

  /**
   * Main Program for Importer Jar.
   */
  public static void main(String[] args) throws IOException {

    String[] properties =
        new String[] { "db.url", "db.user", "db.password", "db.driver" };
    for (String property : properties) {
      setSystemPropertyFromEnv(property);
    }

    try (ConfigurableApplicationContext context =
        new ClassPathXmlApplicationContext("classpath*:**/default-mysql-context.xml")) {
      ImporterConsole importerConsole = context.getBean(ImporterConsole.class);
      importerConsole.execute(args);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static void setSystemPropertyFromEnv(String property) {
    if (System.getProperties().get(property) == null) {
      System.getProperties().put(property, System.getenv().get(property));
    }
  }

  /**
   * Execute Jobs with parameter. the list of the parameters are
   * <li>jobName</li>
   * <li>owner</li>
   *
   * @param parameters
   *          input to the Job
   * @return JobExecution
   */
  public JobExecution execute(String[] parameters) {
    logger.info(
        "Start executing command " + (parameters == null ? "null" : Arrays.asList(parameters)));
    if ((parameters == null) || (parameters.length < 2)) {
      StringBuilder sb = new StringBuilder();
      sb.append("Please run the program with at least two argument "
          + "such as `importCompanyJob c:\\temp\\company.csv`");
      logger.error(sb.toString());
      throw new ImporterConsoleException(sb.toString());
    }

    String jobName = parameters[0];
    String filePath = parameters[1];
    String owner = "";
    if (parameters.length > 2) {
      owner = parameters[2];
    }

    logger.info("JobName:{}, FilePath:{}, Owner:{}", new Object[] { jobName, filePath, owner });

    Job job = jobStore.getJob(jobName);
    if (job == null) {
      String msg = jobName + " is not a valid job name.";
      logger.warn(msg);
      throw new ImporterConsoleException(msg);
    }

    Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
    confMap.put("time", new JobParameter(System.currentTimeMillis()));
    confMap.put(ImporterJobConstants.FILE_PATH, new JobParameter(filePath));
    confMap.put(ImporterJobConstants.OWNER, new JobParameter(owner));
    JobParameters jobParameters = new JobParameters(confMap);

    JobExecution jobExecution = runJob(jobParameters, job);
    logger.info("Job Completed : " + jobExecution);

    return jobExecution;
  }

  private JobExecution runJob(JobParameters jobParameters, Job job) {
    JobExecution jobExecution = null;

    try {
      jobExecution = jobLauncher.run(job, jobParameters);
    } catch (JobExecutionAlreadyRunningException | JobRestartException
        | JobInstanceAlreadyCompleteException | JobParametersInvalidException exception) {
      logger.error("{} runs with Error.", new Object[] { job.getName() }, exception);
      throw new ImporterConsoleException(exception);
    }
    return jobExecution;
  }
}
