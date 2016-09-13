package net.rc.lab.springboot.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

@ContextConfiguration(locations = { "classpath:test-db-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CountryVoTest extends AbstractVoTest<CountryVo> {

  @Override
  protected Class<CountryVo> getEntityClass() {
    return CountryVo.class;
  }

  @Override
  protected CountryVo newInstance() {
    return new CountryVo();
  }

  @Test
  @Transactional
  public void testCreateVoSuccess() {
    CountryVo instance = newInstance();
    instance.setNameEn("test");
    save(instance);

    assertNotNull(instance.getCountryId());
  }

  @Test(expected = ConstraintViolationException.class)
  @Transactional
  public void testCreateVoFail() {
    CountryVo instance = newInstance();
    save(instance);
    fail("Exception is expected.");
  }

}
