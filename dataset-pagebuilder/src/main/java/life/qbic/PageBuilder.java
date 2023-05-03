package life.qbic;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import freemarker.template.*;
import java.io.*;
import java.util.List;
import java.util.Map;

import static life.qbic.FileIndexHandler.*;
import static life.qbic.FileIndexHandler.FILEINDEX;
import static life.qbic.FileIndexHandler.SITEMAP;

/**
 * Creates html/xml pages on base of a template and a java data model
 * Data model: Map
 */
public  class PageBuilder {

    static final Configuration cfg;

    static {
        try {
            cfg = initiate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createLandingPage(Map<String,Object> dataModel, ServerCommunication sc) {
        String outputPath = setOutputPath("webapp" + File.separator + dataModel.get("identifier") + ".html").toString();
        try {
            buildPage(dataModel, cfg.getTemplate("LandingpageTemplate.ftlh"), outputPath);
            // local file --> server
            sc.transferFile(new File(outputPath), "/var/www/html/datasets/");
            updateFileIndex(dataModel);

        }catch(IOException | TemplateException | JSchException | SftpException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void createSEOPages(ServerCommunication sc) {
        String datasetNavPagePath = setOutputPath("webapp" + File.separator + "index.html").toString();
        try {
            Map<String, List<String>> generalDataModel = TSVDataModel.createGeneralDataModel();
            // Build new sitemap
            PageBuilder.buildPage(generalDataModel, cfg.getTemplate("Sitemap_template.ftlx"), SITEMAP.toString());
            sc.transferFile(new File(SITEMAP.toString()), "/var/www/html/");
            // Build new dataset navigation page
            PageBuilder.buildPage(generalDataModel,cfg.getTemplate("NavigationPage_template.ftlh"), datasetNavPagePath);
            sc.transferFile(new File(datasetNavPagePath), "/var/www/html/datasets/");
            // Update FileIndex on server
            sc.transferFile(new File(FILEINDEX.toString()), "/var/");
        }catch(IOException | TemplateException | JSchException | SftpException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Configuration initiate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File(setOutputPath("resources").toString()));
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
    private static void buildPage(Map<?,?> dataModel, Template temp, String outputFile) throws IOException, TemplateException {
        OutputStream out = new FileOutputStream(outputFile);
        Writer writer = new OutputStreamWriter(out);
        try {
            temp.process(dataModel, writer);
            System.out.println("Page created");
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

