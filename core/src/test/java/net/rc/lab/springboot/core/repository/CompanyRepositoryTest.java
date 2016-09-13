package net.rc.lab.springboot.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.CountryVo;

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
public class CompanyRepositoryTest {

  @Autowired
  private CountryRepository countryRepository;
  
  @Autowired
  private CompanyRepository repository;
  
  private CountryVo country;
  
  private CompanyVo instance;

  @Before
  public void setup() {
    country = CountryVo.builder().nameEn("Test Country").build();
    countryRepository.save(country);
  }
  
  /**
   * Delete test data created.
   */
  @After
  public void teardown() {
    if (instance != null) {
      repository.delete(instance);
    }
    countryRepository.delete(country);
  }

  @Test
  public void testCreateReadUpdateDelete() {
    instance = CompanyVo.builder().nameEn("Test Company").country(country).build();

    Long id = null;
    Create: {
      CompanyVo result = repository.save(instance);
      assertEquals("Test Company", result.getNameEn());
      assertNotNull(result.getCompanyId());
      assertNotNull(result.getCountry());
      assertNotNull(result.getUpdateTimestamp());
      assertNotNull(result.getVersion());
      id = result.getCompanyId();
      break Create;
    }

    Read: {
      CompanyVo result = repository.findOne(id);
      assertEquals(result.getCompanyId(), instance.getCompanyId());
      break Read;
    }

    Update: {
      instance.setNameTc("HELLO World");
      CompanyVo result = repository.save(instance);
      assertEquals("HELLO World", result.getNameTc());
      assertTrue(result.getVersion() == instance.getVersion() + 1);
      instance.setVersion(result.getVersion());
      break Update;
    }
    
    Delete: {
      repository.delete(instance);
      CompanyVo result = repository.findOne(id);
      assertNull(result);
      instance = null;
      break Delete;
    }
  }

  @Test
  public void testFindByNameEn() {
    instance = CompanyVo.builder().nameEn("Found me Company").country(country).build();
    repository.save(instance);
    
    Optional<CompanyVo> result = repository.findByNameEn("Not Found");
    assertFalse(result.isPresent());

    result = repository.findByNameEn("Found me Company");
    assertTrue(result.isPresent());
    assertEquals(instance.getNameEn(), result.get().getNameEn());
  }
}
