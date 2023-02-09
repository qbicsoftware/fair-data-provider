package life.qbic;
import freemarker.template.*;
import java.io.*;
import java.util.Map;

/**
 * Creates html/xml pages on base of a template and a java data model
 * Data model: Map
 */
public  class TemplateEngine {

    public static Configuration initiate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File("dataset-pagebuilder/src/main/resources"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER); // change to DEBUG_HANDLER only in the end!!
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.getRecognizeStandardFileExtensions();
        return(cfg);
    }

    /**
     * @param dataModel: Map<?,?>
     * @param temp: html or xml template
     * @param outputFile: Path to the file that will be created
     * @throws IOException: if OutputStream is unsuccessful to write file
     * @throws TemplateException: prints template-language (html/xml) & java stack trace
     */
    public static void buildPage(Map<?,?> dataModel, Template temp, String outputFile) throws IOException, TemplateException {
        OutputStream out = new FileOutputStream(outputFile);
        Writer writer = new OutputStreamWriter(out);
        try {
            temp.process(dataModel, writer);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

}

