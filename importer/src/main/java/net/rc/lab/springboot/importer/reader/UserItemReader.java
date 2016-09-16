package net.rc.lab.springboot.importer.reader;

import net.rc.lab.springboot.entities.UserVo;
import net.rc.lab.springboot.importer.common.ImporterJobConstants;
import net.rc.lab.springboot.importer.mapper.AbstractFieldSetMapper;
import net.rc.lab.springboot.importer.mapper.UserFieldSetMapper;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
@StepScope
public class UserItemReader extends AbstractItemReader<UserVo> {

  @Autowired
  private UserFieldSetMapper mapper;

  @Autowired
  public UserItemReader(@Value(ImporterJobConstants.JOB_PARAMTERS_FILE_PATH) final String filePath)
      throws MalformedURLException {
    setResource(new UrlResource(ImporterJobConstants.URL_FILE_PREFIX + filePath));
  }

  @Override
  protected String[] getCsvColumnNames() {
    // @formatter:off
    return new String[] {
        "Id",
        "Password",
        "Role",
        "Company",
        "Email",
        "Phone No",
        "Mobile No"
        };
    // @:formatter:on
  }

  @Override
  protected AbstractFieldSetMapper<UserVo> getMapper() {
    return mapper;
  }

}
