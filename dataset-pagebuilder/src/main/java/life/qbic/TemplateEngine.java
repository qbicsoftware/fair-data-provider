package life.qbic;

import freemarker.template.*;
import java.io.File;
import java.io.IOException;

public  class TemplateEngine {

    public static Configuration initiate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File("C://Users/aline/Dokumente/Studium/Semester_7/Bachelorarbeit/fairy-data-provider/dataset-pagebuilder/src/main/resources"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        return(cfg);
    }
    public static void buildPage(Configuration cfg){
        //TODO
    }

}

