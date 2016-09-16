package net.rc.lab.springboot.importer.reader;

import net.rc.lab.springboot.importer.common.ImporterJobConstants;
import net.rc.lab.springboot.importer.mapper.AbstractFieldSetMapper;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public abstract class AbstractItemReader<T> extends FlatFileItemReader<T> {

  protected abstract String[] getCsvColumnNames();

  protected abstract AbstractFieldSetMapper<T> getMapper();

  private StepExecution stepExecution;

  /**
   * Set Default encoding to UTF-8 and skip first row as header.
   */
  public AbstractItemReader() {
    setEncoding(ImporterJobConstants.UTF_8);
    setLinesToSkip(1);
  }

  @BeforeStep
  public void saveStepExecution(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
    getMapper().setStepExecution(stepExecution);
  }

  protected StepExecution getStepExecution() {
    return stepExecution;
  }

  /**
   * Set LineMapper Before Run.
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    setLineMapper(new DefaultLineMapper<T>() {
      {
        setLineTokenizer(new DelimitedLineTokenizer() {
          {
            // @formatter:off
            setNames(getCsvColumnNames());
            // @:formatter:on
          }

        });
        setFieldSetMapper(getMapper());
      }
    });
    super.afterPropertiesSet();
  }

}
