package life.qbic;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonReader {
    public static Map readFile(File file) {
        Map dataModel = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            dataModel = mapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String markup = dataModel.toString();
        dataModel.put("markup",markup);
        return(dataModel);
    }
}
