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
public class UserVoTest extends AbstractVoTest<UserVo> {

  private CountryVo country;
  private CompanyVo company;
  private RoleVo role;

  @Override
  public Class<UserVo> getEntityClass() {
    return UserVo.class;
  }

  @Override
  protected UserVo newInstance() {
    UserVo instance = new UserVo();
    instance.setCompany(company);
    instance.setRole(role);
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

    company = new CompanyVo();
    company.setCountry(country);
    company.setNameEn("ACTP");
    save(company);
    assertNotNull(company.getCompanyId());

    role = new RoleVo();
    role.setName("Administrator");
    save(role);
    assertNotNull(role.getRoleId());
  }

  @Test
  @Transactional
  public void testCreateSuccess() {
    UserVo instnace = newInstance();
    instnace.setEmail("test@actp.com");
    instnace.setMobileNo("30624700");
    instnace.setPhoneNo("30624700");
    save(instnace);
    assertNotNull(instnace.getUserId());
  }

  @Test(expected = ConstraintViolationException.class)
  @Transactional
  public void testCreateFail() {
    UserVo instance = new UserVo();
    instance.setRole(role);
    instance.setCompany(company);
    instance.setEmail("abc");
    instance.setMobileNo("abc123");
    instance.setPhoneNo("hello123");
    save(instance);
    fail("Exception is expected.");
  }

}
