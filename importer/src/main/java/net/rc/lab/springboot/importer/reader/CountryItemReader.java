package net.rc.lab.springboot.importer.reader;

import net.rc.lab.springboot.entities.CountryVo;
import net.rc.lab.springboot.importer.common.ImporterJobConstants;
import net.rc.lab.springboot.importer.mapper.AbstractFieldSetMapper;
import net.rc.lab.springboot.importer.mapper.CountryFieldSetMapper;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
@StepScope
public class CountryItemReader extends AbstractItemReader<CountryVo> {

  @Autowired
  private CountryFieldSetMapper mapper;

  @Autowired
  public CountryItemReader(
      @Value(ImporterJobConstants.JOB_PARAMTERS_FILE_PATH) final String filePath)
          throws MalformedURLException {
    setResource(new UrlResource(ImporterJobConstants.URL_FILE_PREFIX + filePath));
  }

  @Override
  protected String[] getCsvColumnNames() {
    // @formatter:off
    return new String[] {
        "Name English",
        "Name Traditional Chinese",
        "Name Simplified Chinese",
        "Flag URI"
        };
    // @:formatter:on
  }

  @Override
  protected AbstractFieldSetMapper<CountryVo> getMapper() {
    return mapper;
  }

}
