package life.qbic;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonReader {
    public static Map<String,String> readFile(File file) throws IOException {

        //read json file into a map for template engine to use in html
        Map<String,String> dataModel = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            dataModel = mapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return(dataModel);
    }
}
