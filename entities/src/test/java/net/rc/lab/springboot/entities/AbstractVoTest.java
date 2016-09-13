package net.rc.lab.springboot.entities;

import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Lists;

import net.rc.lab.springboot.entities.common.SystemEntity;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.validator.constraints.Email;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.Pattern;

public abstract class AbstractVoTest<T extends SystemEntity> {

  @PersistenceContext
  private EntityManager entityManager;

  private List<SystemEntity> entities;

  private Class<T> clazz;

  public AbstractVoTest() {
    clazz = getEntityClass();
  }

  protected abstract Class<T> getEntityClass();

  protected abstract T newInstance();

  @Before
  public void setup() {
    entities = Lists.newArrayList();
  }

  @After
  @Transactional
  public void teardown() {
    entities.stream().forEach(p -> entityManager.remove(p));
  }

  protected void addEntity(SystemEntity entity) {
    entities.add(0, entity);
  }

  protected void save(SystemEntity entity) {
    entityManager.persist(entity);
    addEntity(entity);
  }

  protected List<SystemEntity> getEntities() {
    return entities;
  }

  @Test
  @Transactional
  public void testFieldsLength()
      throws IllegalArgumentException, IllegalAccessException, InstantiationException {
    T vo = newInstance();
    vo.setUpdateTimestamp(System.currentTimeMillis());
    Field idField = null;
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      idField = field.getAnnotation(Id.class) != null ? field : idField;
      if (skipField(field)) {
        continue;
      }
      Column column = field.getAnnotation(Column.class);
      if (column != null) {
        generateField(vo, field, column);
      }
    }
    save(vo);
    assertNotNull(idField.get(vo));
  }

  private void generateField(T vo, Field field, Column column) throws IllegalAccessException {
    int length = column.length();
    int max = 1 * (length + 1) - 1;

    if (field.getType().isAssignableFrom(String.class)) {
      field.set(vo, RandomStringUtils.random(length));
    } else if (field.getType().isAssignableFrom(Integer.class)) {
      field.set(vo, RandomUtils.nextInt(0, max));
    } else if (field.getType().isAssignableFrom(Long.class)) {
      field.set(vo, RandomUtils.nextLong(0, max));
    } else if (field.getType().isAssignableFrom(Double.class)) {
      field.set(vo, RandomUtils.nextDouble(0, max));
    } else if (field.getType().isAssignableFrom(Float.class)) {
      field.set(vo, RandomUtils.nextFloat(0, max));
    } else if (field.getType().isAssignableFrom(BigDecimal.class)) {
      int precision = column.precision();
      int scale = column.scale();
      max = 1 * (precision + 1) - 1;
      BigDecimal bg = new BigDecimal(RandomUtils.nextLong(0, max));
      bg = bg.setScale(scale);
      field.set(vo, bg);
    }
  }

  private boolean skipField(Field field) {
    // @formatter:off
    return field.getAnnotation(OneToMany.class) != null
        || field.getAnnotation(ManyToOne.class) != null
        || field.getAnnotation(Pattern.class) != null 
        || field.getAnnotation(Email.class) != null;
    // @formatter:on
  }

}
