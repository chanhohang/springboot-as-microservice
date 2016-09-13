package net.rc.lab.springboot.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.CountryVo;
import net.rc.lab.springboot.entities.RoleVo;
import net.rc.lab.springboot.entities.UserVo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@ContextConfiguration(locations = { "classpath:test-db-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryTest {

  @Autowired
  private UserRepository repository;

  @Autowired
  private RoleRepository roleRepository;
  
  @Autowired
  private CompanyRepository companyRepository;
  
  @Autowired
  private CountryRepository countryRepository;
  
  private RoleVo role;
  
  private CompanyVo company;
  
  private CountryVo country;
  
  private UserVo instance;

  /**
   * Setup test data.
   */
  @Before
  public void setup() {
    role = RoleVo.builder().name("Test Role").build();
    roleRepository.save(role);
    country = CountryVo.builder().nameEn("Test Country").build();
    countryRepository.save(country);
    company = CompanyVo.builder().nameEn("Test Company").country(country).build();
    companyRepository.save(company);
  }
  
  /**
   * Delete test data created.
   */
  @After
  public void teardown() {
    if (instance != null) {
      repository.delete(instance);
    }
    roleRepository.delete(role);
    companyRepository.delete(company);
    countryRepository.delete(country);
  }

  @Test
  public void testCreateReadUpdateDelete() {
    instance = UserVo.builder().loginId("Test Role").role(role).company(company).build();

    Long id = null;
    Create: {
      UserVo result = repository.save(instance);
      assertEquals("Test Role", result.getLoginId());
      assertNotNull(result.getUserId());
      assertNotNull(result.getRole());
      assertNotNull(result.getUpdateTimestamp());
      assertNotNull(result.getVersion());
      id = result.getUserId();
      break Create;
    }

    Read: {
      UserVo result = repository.findOne(id);
      assertEquals(result.getUserId(), instance.getUserId());
      break Read;
    }

    Update: {
      instance.setLoginId("HELLO World");
      UserVo result = repository.save(instance);
      assertEquals("HELLO World", result.getLoginId());
      assertTrue(result.getVersion() == instance.getVersion() + 1);
      instance.setVersion(result.getVersion());
      break Update;
    }
    
    Delete: {
      repository.delete(instance);
      UserVo result = repository.findOne(id);
      assertNull(result);
      instance = null;
      break Delete;
    }
  }

  @Test
  public void testFindByLoginId() {
    instance = UserVo.builder().loginId("Found me loginid").role(role).company(company).build();
    repository.save(instance);
    
    Optional<UserVo> result = repository.findByLoginId("Not Found");
    assertFalse(result.isPresent());

    result = repository.findByLoginId("Found me loginid");
    assertTrue(result.isPresent());
    assertEquals(instance.getLoginId(), result.get().getLoginId());
  }

}
