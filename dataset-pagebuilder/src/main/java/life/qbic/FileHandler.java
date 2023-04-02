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

    public static void createLandingPage(Map<String,Object> dataModel, ServerCommunication sc) throws IOException {
        initiateFileIndex();
        try {
            String outputPath = Paths.get("dataset-pagebuilder/src/main/webapp/" + dataModel.get("identifier") + ".html").toAbsolutePath().toString();
            String datasetIndex = Paths.get("dataset-pagebuilder/src/main/webapp/", "index.html").toAbsolutePath().toString();
            // set the configuration to convert dataModel to html/xml
            Configuration cfg = TemplateEngine.initiate();
            TemplateEngine.buildPage(dataModel, cfg.getTemplate("LandingPage_template.ftlh"), outputPath);
            // local file --> server
            sc.transferFile(new File(outputPath), "/var/www/html/datasets/");
            //updateFileIndex(dataModel);
            addFileToIndex(FILEINDEX.toFile(), dataModel);
            // Build the new sitemap
            TemplateEngine.buildPage(FileHandler.createGeneralDataModel(),cfg.getTemplate("Sitemap_template.ftlx"), SITEMAP.toString());
            sc.transferFile(new File(SITEMAP.toString()), "/var/www/html/");
            // Build the new index page
            TemplateEngine.buildPage(FileHandler.createGeneralDataModel(),cfg.getTemplate("NavigationPage_template.ftlh"), datasetIndex);
            sc.transferFile(new File(datasetIndex), "/var/www/html/datasets/");

        }catch(IOException e){
            e.printStackTrace();

        }catch (TemplateException | JSchException | SftpException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String,Object> createDatasetDataModel(String[] keys, String[] values) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> dataModel = createBasicDataModel();
        for (int i = 0; i < keys.length; i++) {
            if(nonNull(keys[i]) && nonNull(values[i]) && !values[i].equals("null")) {
                dataModel.put(keys[i], values[i]);
            }
        }
        String url = BASISURL + "datasets/" + dataModel.get("identifier") + ".html";
        dataModel.put("url", url);
        dataModel.put("@id", url);
        dataModel.put("markup", mapper.writeValueAsString(dataModel));

        System.out.println("Datamodel created for file" + dataModel.get("identifier"));

        return dataModel;
    }

    private static Map<String, Object> createBasicDataModel() {
        Map<String,String> providerMarkup = new HashMap<>();
        Map<String,Object> dataModel = new HashMap<>();

        providerMarkup.put("@type","Organization");
        providerMarkup.put("name", "QBiC");
        providerMarkup.put("@id", "https://ror.org/00v34f693");
        dataModel.put("@context","http://schema.org");
        dataModel.put("@type","dataset");
        dataModel.put("provider",providerMarkup);

        return dataModel;
    }

    public static Map<String, List<String>> createGeneralDataModel() throws FileNotFoundException {
        // Fileindex: 1.column: id, 2.column: url, 3. column: lastMod, 4. column: description
        Map<String, List<String>> generalDataModel = new HashMap<>();
        List<String> ids = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        FileReader reader = new FileReader(FILEINDEX.toFile());
        BufferedReader buffReader = new BufferedReader(reader);
        List<String> allLines = buffReader.lines().toList();

        for (String line : allLines) {
            String[] separateValues = line.split("\\t");
            ids.add(separateValues[0]);
            urls.add(separateValues[1]);
            dates.add(separateValues[2]);
            descriptions.add(separateValues[3]);
        }
        generalDataModel.put("ids", ids);
        generalDataModel.put("urls", urls);
        generalDataModel.put("dates", dates);
        generalDataModel.put("descriptions", descriptions);

        return generalDataModel;
    }

    private static void initiateFileIndex() throws IOException {
        if(!FILEINDEX.toFile().isFile()) {
            FileOutputStream fileIndex = new FileOutputStream(FILEINDEX.toString());
            fileIndex.close();
        }
    }

    static void addFileToIndex( File file, Map<String,Object> dataModel) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.GERMANY);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        String lastModified = simpleDateFormat.format(new Date());

        try (FileWriter writer = new FileWriter(file,true)) {
            writer.write(String.format("%s\t%s\t%s\t%s%n", dataModel.get("identifier"), dataModel.get("url"), lastModified, dataModel.get("description")));
        }
    }

    private static void updateFileIndex(Map<String,Object> dataModel) throws IOException {
        final String identifier = dataModel.get("identifier").toString();
        //final File fileIndex = Paths.get("dataset-pagebuilder/src/main/resources/FileIndex.txt").toFile();
        final File tempFileIndex = Paths.get("dataset-pagebuilder/src/main/resources/TempFileIndex.txt").toFile();

        FileOutputStream output = new FileOutputStream(tempFileIndex);
        output.close();

        FileReader reader = new FileReader(FILEINDEX.toFile());
        BufferedReader buffReader = new BufferedReader(reader);
        String lineToRead = buffReader.readLine();

        boolean wasReplaced = false;
        String currentIdOfFile;

        // Fileindex: 1.column: id, 2.column: url, 3. column: lastMod
        while(lineToRead != null) {
            currentIdOfFile = lineToRead.split("\\t")[0];
            if (currentIdOfFile.equals(identifier)) {
                FileHandler.addFileToIndex(tempFileIndex,dataModel);
                wasReplaced = true;
            } else{
                try (FileWriter writer = new FileWriter(tempFileIndex,true)) {
                    writer.write(lineToRead);
                }
            }
            lineToRead = buffReader.readLine();
        }
        if (!wasReplaced){
            FileHandler.addFileToIndex(tempFileIndex,dataModel);
        }
        System.out.println(tempFileIndex.renameTo(FILEINDEX.toFile()));
    }
}
