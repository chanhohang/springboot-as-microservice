package net.rc.lab.springboot.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@ContextConfiguration(locations = { "classpath:test-db-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyVoTest extends AbstractVoTest<CompanyVo> {

  private CountryVo country;

  @Override
  protected Class<CompanyVo> getEntityClass() {
    return CompanyVo.class;
  }

  @Override
  protected CompanyVo newInstance() {
    CompanyVo instance = new CompanyVo();
    instance.setCountry(country);
    return instance;
  }

  /**
   * Create Foreign Key object.
   */
  @Before
  public void setup() {
    super.setup();

    country = new CountryVo();
    country.setNameEn("Australia");
    save(country);
    assertNotNull(country.getCountryId());
  }

  @Test
  @Transactional
  public void testCreateVoSuccess() {
    CompanyVo instance = newInstance();
    instance.setNameEn("test");
    save(instance);

    assertNotNull(instance.getCompanyId());
  }

  @Test(expected = ConstraintViolationException.class)
  @Transactional
  public void testCreateVoFail() {
    CompanyVo instance = newInstance();
    save(instance);
    fail("Exception is expected.");
  }

}
