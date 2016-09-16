package net.rc.lab.springboot.importer.mapper;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.mapping.FieldSetMapper;

public abstract class AbstractFieldSetMapper<T> implements FieldSetMapper<T> {

  private StepExecution stepExecution;

  public void setStepExecution(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  public StepExecution getStepExecution() {
    return stepExecution;
  }
}
