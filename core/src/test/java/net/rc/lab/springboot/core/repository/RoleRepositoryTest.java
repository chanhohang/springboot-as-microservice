package net.rc.lab.springboot.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.rc.lab.springboot.entities.RoleVo;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@ContextConfiguration(locations = { "classpath:test-db-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class RoleRepositoryTest {

  @Autowired
  private RoleRepository repository;

  private RoleVo instance;

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
    instance = RoleVo.builder().name("Test Role").build();

    Long id = null;
    Create: {
      RoleVo result = repository.save(instance);
      assertEquals("Test Role", result.getName());
      assertNotNull(result.getRoleId());
      assertNotNull(result.getUpdateTimestamp());
      assertNotNull(result.getVersion());
      id = result.getRoleId();
      break Create;
    }

    Read: {
      RoleVo result = repository.findOne(id);
      assertEquals(result, instance);
      break Read;
    }

    Update: {
      instance.setDescriptionEn("HELLO World");
      RoleVo result = repository.save(instance);
      assertEquals("HELLO World", result.getDescriptionEn());
      assertTrue(result.getVersion() == instance.getVersion() + 1);
      instance.setVersion(result.getVersion());
      break Update;
    }
    
    Delete: {
      repository.delete(instance);
      RoleVo result = repository.findOne(id);
      assertNull(result);
      instance = null;
      break Delete;
    }
  }
  
  @Test
  public void testFindByName() {
    instance = RoleVo.builder().name("Found me Role").build();
    repository.save(instance);
    
    Optional<RoleVo> result = repository.findByName("Not Found");
    assertFalse(result.isPresent());

    result = repository.findByName("Found me Role");
    assertTrue(result.isPresent());
    assertEquals(instance.getName(), result.get().getName());
  }

}
