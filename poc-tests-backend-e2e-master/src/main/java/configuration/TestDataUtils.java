package configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class TestDataUtils {

    public JSONObject mappingJsonToObject(String filePath) {

        JSONObject jsonObject = null;

        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader(filePath.replace("\\", "/")));
            jsonObject = (JSONObject) object;
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String mappingJsonToString(String filePath) {
        return this.mappingJsonToObject(filePath).toJSONString();
    }

    public JSONArray mappingJsonToArray(String filePath) {

        JSONArray jsonArray = null;
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(filePath.replace("\\", "/")));
            jsonArray = (JSONArray) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public String getRandomValue(){
        Random random = new Random();
        int randomInt = random.nextInt();
        return Integer.toString(randomInt);
    }

}