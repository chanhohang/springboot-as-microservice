package net.rc.lab.springboot.importer.console;

public class ImporterConsoleException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ImporterConsoleException(String message) {
    super(message);
  }

  public ImporterConsoleException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ImporterConsoleException(Throwable throwable) {
    super(throwable);
  }
}
