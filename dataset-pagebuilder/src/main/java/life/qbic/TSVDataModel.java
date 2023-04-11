package life.qbic;

import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static java.util.Objects.nonNull;

public class TSVDataModel extends DataModel {
    static private final String BASISURL = "https://fair.qbic.uni-tuebingen.de/";
    public Map<String,Object> createDataModel(String[] keys, String[] values) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> dataModel = setBasicDatasetDataModel();

        // fill the dataModel with the keys & values for the specific dataset
        for (int i = 0; i < keys.length; i++) {
            if(nonNull(keys[i]) && nonNull(values[i]) && !values[i].equals("null")) {
                switch (keys[i]) {
                    case "keywords" -> dataModel.put(keys[i], Arrays.asList(values[i].split(",")));
                    case "creator" -> {
                        Map<String, String> creator = new HashMap<>();
                        creator.put("@type", "Person");
                        creator.put("name", values[i]);
                        dataModel.put(keys[i], creator);
                    }
                    default -> dataModel.put(keys[i], values[i]);
                }
            }
        }
        String url = BASISURL + "datasets/" + dataModel.get("identifier") + ".html";
        dataModel.put("url", url);
        dataModel.put("@id", url);
        dataModel.put("markup", mapper.writeValueAsString(dataModel));

        System.out.println("TSVDataModel created for file " + dataModel.get("identifier"));
        return dataModel;
    }

    private static Map<String, Object> setBasicDatasetDataModel() {
        Map<String,String> publisherMarkup = new HashMap<>();
        Map<String,Object> dataModel = new HashMap<>();

        // Adding the basic schema.org and bioschemas.org json-ld markup for a dataset
        dataModel.put("@context","http://schema.org");
        dataModel.put("@type","dataset");
        dataModel.put("http://purl.org/dc/terms/conformsTo","https://bioschemas.org/profiles/Dataset/1.0-RELEASE");

        // Adding QBiC as a provider
        publisherMarkup.put("@type","Organization");
        publisherMarkup.put("name", "QBiC");
        publisherMarkup.put("@id", "https://ror.org/00v34f693");
        dataModel.put("publisher", publisherMarkup);

        return dataModel;
    }
}
