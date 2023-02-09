package life.qbic;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import static java.nio.file.Files.readAllBytes;


/**
 *
 */
public class FileHandler {
    static final Path SITEMAP = Paths.get("dataset-pagebuilder/src/main/resources/sitemap.xml");
    static final Path FILEINDEX = Paths.get("dataset-pagebuilder/src/main/resources/FileIndex.txt").toAbsolutePath();

    public static int createLandingPage(File jsonldFile) throws IOException {
        initiateFileIndex();
        // read the json file into java
        Map<String,String> dataModel = readJsonFile(jsonldFile);
        try {
            String outputPath = Paths.get("dataset-pagebuilder/src/main/resources/" + dataModel.get("identifier") + ".html").toAbsolutePath().toString();
            // set the configuration to convert dataModel to html/xml
            Configuration cfg = TemplateEngine.initiate();
            TemplateEngine.buildPage(dataModel, cfg.getTemplate("LandingPage_template.ftlh"), outputPath);
            addFileToIndex(dataModel.get("identifier"), FILEINDEX.toFile(), dataModel.get("url"));
            // local file --> server
            ServerCommunication.transferFile(new File(outputPath));
            // Build the new sitemap
            TemplateEngine.buildPage(FileHandler.createDataModelFromIndex(),cfg.getTemplate("Sitemap_template.ftlx"), SITEMAP.toString());
            ServerCommunication.transferFile(new File(SITEMAP.toString()));
            return 0;
        }catch(IOException e){
            e.printStackTrace();
            return 1;
        }catch (TemplateException | JSchException | SftpException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * reads json file into a map for template engine to convert to html
     * @param file json-ld file
     * @return Map<String,String>
     */
    public static Map<String,String> readJsonFile(File file) {

        Map<String,String> dataModel = new HashMap<>();
        try {
            //mapper converts the json file into a java Map
            ObjectMapper mapper = new ObjectMapper();
            dataModel = mapper.readValue(file, Map.class);
            dataModel.put("markup", convertJsonToString(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return(dataModel);
    }

    private static String convertJsonToString(File file) throws IOException {
        System.out.println(new String(readAllBytes(file.toPath())));
        return new String(readAllBytes(file.toPath()));
    }

    public static Map<String, List<String>> createDataModelFromIndex() throws FileNotFoundException {
        // 1.column: id, 2.column: url, 3. column: lastMod
        FileReader reader = new FileReader("dataset-pagebuilder/src/main/resources/FileIndex.txt");
        BufferedReader buffReader = new BufferedReader(reader);
        List<String> allLines = buffReader.lines().toList();
        Map<String, List<String>> sitemapDataModel = new HashMap<>();

        List<String> dates = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (String line : allLines) {
            String[] separateValues = line.split("\\t");
            urls.add(separateValues[1]);
            dates.add(separateValues[2]);
        }
        sitemapDataModel.put("urls", urls);
        sitemapDataModel.put("dates", dates);

        return sitemapDataModel;
    }

    private static void initiateFileIndex() throws IOException {
        if(!FILEINDEX.toFile().isFile()) {
            FileOutputStream fileIndex = new FileOutputStream(FILEINDEX.toString());
            fileIndex.close();
        }
    }

    static void addFileToIndex(String datasetID, File index, String url) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.GERMANY);
        String lastModified = simpleDateFormat.format(new Date());

        try (FileWriter writer = new FileWriter(index,true)) {
            writer.write(String.format("%s\t%s\t%s%n", datasetID, url, lastModified));
        }
    }
}
