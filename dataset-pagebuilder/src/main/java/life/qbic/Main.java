package life.qbic;

import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    System.out.println("Hello world!");

    File file = new File("C://Users/aline/Dokumente/Studium/Semester_7/Bachelorarbeit/fairy-data-provider/dataset-pagebuilder/src/main/resources/jsonld_example.json");
    Map dataModel = JsonReader.readFile(file);

    try {
      Configuration cfg = TemplateEngine.initiate();
      TemplateEngine.buildPage(cfg);
    }catch(IOException e){
      e.printStackTrace();
    }


  }




  }
