package PlivoApiAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public Properties fetchFromPropertiesFile(){
        Properties properties = new Properties();
        try {
            File file = new File("src/test/resources/config.properties");
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        return properties;
    }
    public String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
