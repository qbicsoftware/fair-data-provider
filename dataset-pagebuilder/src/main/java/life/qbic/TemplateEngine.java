package life.qbic;

import freemarker.template.*;

import java.io.*;
import java.util.Map;

public  class TemplateEngine {

    public static Configuration initiate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File("C://Users/aline/Dokumente/Studium/Semester_7/Bachelorarbeit/fairy-data-provider/dataset-pagebuilder/src/main/resources"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER); // change to DEBUG_HANDLER only in the end!!
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.getRecognizeStandardFileExtensions();
        return(cfg);
    }
    public static void buildPage(Map<?,?> dataModel, Template temp, String outputFile) throws IOException, TemplateException {

        OutputStream out = new FileOutputStream(outputFile);
        Writer writer = new OutputStreamWriter(out);

        try {
            temp.process(dataModel, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

