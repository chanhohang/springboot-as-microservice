package net.rc.lab.springboot.importer.common;

import org.springframework.batch.core.Job;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobStore {

  private Map<String, Job> jobMap = new HashMap<>();

  public void addJob(String name, Job job) {
    this.jobMap.put(name, job);
  }

  public Job getJob(String name) {
    return jobMap.get(name);
  }

  public Collection<String> getJobNames() {
    return jobMap.keySet();
  }
}
