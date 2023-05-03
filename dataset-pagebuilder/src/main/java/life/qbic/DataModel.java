package life.qbic;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static life.qbic.FileIndexHandler.FILEINDEX;

public abstract class DataModel {

    /**
     * Needed for the sitemap and the Dataset navigation page
     * @return DataModel with general information of all datasets available on server
     * @throws FileNotFoundException if the FileIndex is not available
     */
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

    /**
     * @param keys terms used as keys in the map -> schema.org properties
     * @param values values describing the keys
     * @return Data model for a dataset
     * @throws JsonProcessingException if the dataset can't be turned into a json format
     */
    public abstract Map<String,Object> createDataModel(String[] keys, String[] values) throws JsonProcessingException;
}
