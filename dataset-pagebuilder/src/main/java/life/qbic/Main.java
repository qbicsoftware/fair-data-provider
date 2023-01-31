package life.qbic;
import picocli.CommandLine;



public class Main {

  public static void main(String[] args) {

    int exitCode = new CommandLine(new CommandLineInput()).execute(args);
    System.exit(exitCode);

  }
}
