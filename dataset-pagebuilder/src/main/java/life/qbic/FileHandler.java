package life.qbic;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import static java.util.Objects.nonNull;


/**
 *
 */
public class FileHandler {
    static final Path SITEMAP = Paths.get("dataset-pagebuilder/src/main/resources/sitemap.xml");
    static final Path FILEINDEX = Paths.get("dataset-pagebuilder/src/main/resources/FileIndex.txt").toAbsolutePath();
    static final String BASISURL = "https://fair.qbic.uni-tuebingen.de/";

    public static void createLandingPage(Map<String,String> dataModel) throws IOException {
        initiateFileIndex();
        try {
            String outputPath = Paths.get("dataset-pagebuilder/src/main/webapp/" + dataModel.get("identifier") + ".html").toAbsolutePath().toString();
            // set the configuration to convert dataModel to html/xml
            Configuration cfg = TemplateEngine.initiate();
            TemplateEngine.buildPage(dataModel, cfg.getTemplate("LandingPage_template.ftlh"), outputPath);
            addFileToIndex(dataModel.get("identifier"), FILEINDEX.toFile(), dataModel.get("url"));
            // local file --> server
            ServerCommunication.transferFile(new File(outputPath));
            // Build the new sitemap
            TemplateEngine.buildPage(FileHandler.getSitemapDataModel(),cfg.getTemplate("Sitemap_template.ftlx"), SITEMAP.toString());
            ServerCommunication.transferFile(new File(SITEMAP.toString()));

        }catch(IOException e){
            e.printStackTrace();

        }catch (TemplateException | JSchException | SftpException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String,String> createDataModel(String[] keys, String[] values) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> dataModel = new HashMap<>();
        dataModel.put("@context","http://schema.org");
        dataModel.put("@type","dataset");
        for (int i = 0; i < keys.length; i++) {
            if(nonNull(keys[i]) && nonNull(values[i])) {
                dataModel.put(keys[i], values[i]);
            }
        }
        String url = BASISURL + dataModel.get("identifier");
        dataModel.put("url", url);
        dataModel.put("@id", url);
        dataModel.put("markup", mapper.writeValueAsString(dataModel));

        System.out.println("Datamodel created for file" + dataModel.get("identifier"));

        return dataModel;
    }

    public static Map<String, List<String>> getSitemapDataModel() throws FileNotFoundException {
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
            System.out.println("File is added to the index");
        }
    }
}
