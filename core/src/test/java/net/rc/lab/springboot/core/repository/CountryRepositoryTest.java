package net.rc.lab.springboot.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.rc.lab.springboot.entities.CountryVo;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@ContextConfiguration(locations = { "classpath:test-db-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CountryRepositoryTest {

  @Autowired
  private CountryRepository repository;

  private CountryVo instance;

  /**
   * Delete test data created.
   */
  @After
  public void teardown() {
    if (instance != null) {
      repository.delete(instance);
    }
  }

  @Test
  public void testCreateReadUpdateDelete() {
    instance = CountryVo.builder().nameEn("Test Country").build();

    Long id = null;
    Create: {
      CountryVo result = repository.save(instance);
      assertEquals("Test Country", result.getNameEn());
      assertNotNull(result.getCountryId());
      assertNotNull(result.getUpdateTimestamp());
      assertNotNull(result.getVersion());
      id = result.getCountryId();
      break Create;
    }

    Read: {
      CountryVo result = repository.findOne(id);
      assertEquals(result.getNameEn(), instance.getNameEn());
      break Read;
    }

    Update: {
      instance.setNameSc("HELLO World");
      CountryVo result = repository.save(instance);
      assertEquals("HELLO World", result.getNameSc());
      assertTrue(result.getVersion() == instance.getVersion() + 1);
      instance.setVersion(result.getVersion());
      break Update;
    }
    
    Delete: {
      repository.delete(instance);
      CountryVo result = repository.findOne(id);
      assertNull(result);
      instance = null;
      break Delete;
    }
  }

  @Test
  public void testFindByNameEn() {
    instance = CountryVo.builder().nameEn("Found me Country").build();
    repository.save(instance);
    
    Optional<CountryVo> result = repository.findByNameEn("Not Found");
    assertFalse(result.isPresent());

    result = repository.findByNameEn("Found me Country");
    assertTrue(result.isPresent());
    assertEquals(instance.getNameEn(), result.get().getNameEn());
  }
}
