package net.rc.lab.springboot.importer.writer;


import net.rc.lab.springboot.entities.common.SystemEntity;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public abstract class AbstractItemWriter<T extends SystemEntity> implements ItemWriter<T> {

  protected abstract T findExisting(T item);

  protected abstract void update(T existing, T item);

  protected abstract void insert(T item);

  @Override
  public void write(List<? extends T> items) throws Exception {
    for (T item : items) {
      T existing = findExisting(item);
      if (existing != null) {
        setSystemEntity(existing, item);
        update(existing, item);
      } else {
        insert(item);
      }
    }
  }

  protected void setSystemEntity(T existing, T item) {
    item.setVersion(existing.getVersion());
    item.setUpdateTimestamp(System.currentTimeMillis());
  }

}
