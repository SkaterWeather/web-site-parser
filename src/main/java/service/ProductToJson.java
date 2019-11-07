package service;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import storage.Storage;

public class ProductToJson {
    private static final String FILE_NAME = "results.json";

    public static void convertIntoFile() {
        File file = new File(FILE_NAME);
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(file, Storage.productMap.values());
            System.out.println("Result file successfully created: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error trying to convert data into JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
