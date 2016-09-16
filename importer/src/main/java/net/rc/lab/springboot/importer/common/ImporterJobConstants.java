package net.rc.lab.springboot.importer.common;

public interface ImporterJobConstants {

  String FILE_PATH = "file.path";
  String OWNER = "owner";
  String UTF_8 = "UTF-8";
  String URL_FILE_PREFIX = "file:";

  String JOB_PARAMTERS_FILE_PATH = "#{jobParameters['" + FILE_PATH + "']}";
}
