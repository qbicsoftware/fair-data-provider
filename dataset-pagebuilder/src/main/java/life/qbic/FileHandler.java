package life.qbic;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 *
 */
public class FileHandler {
    /**
     * reads json file into a map for template engine to use in html
     * @param file json-ld file
     * @return Map<String,String>
     * @throws IOException when file could not be mapped to a map
     */
    public static Map<String,String> readJsonFile(File file) throws IOException {

        Map<String,String> dataModel = new HashMap<>();
        try {
            //mapper converts the json file into a java Map
            ObjectMapper mapper = new ObjectMapper();
            dataModel = mapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return(dataModel);
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
}
