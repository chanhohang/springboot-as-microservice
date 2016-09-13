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
public class RoleVoTest extends AbstractVoTest<RoleVo> {

  @Override
  public Class<RoleVo> getEntityClass() {
    return RoleVo.class;
  }

  @Override
  protected RoleVo newInstance() {
    return new RoleVo();
  }

  @Test
  @Transactional
  public void testCreateVoSuccess() {
    RoleVo instance = newInstance();
    instance.setName("testRole");
    save(instance);

    assertNotNull(instance.getRoleId());
  }

  @Test(expected = ConstraintViolationException.class)
  @Transactional
  public void testCreateVoFail() {
    RoleVo instance = newInstance();
    save(instance);
    fail("Exception is expected.");
  }

}
