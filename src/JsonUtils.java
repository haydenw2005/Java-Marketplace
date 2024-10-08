import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for handling JSON file operations.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Hayden, Soham
 * @version November 13, 2023
 */
public class JsonUtils {
    static final String FILE_PATH = "data.json";

    /**
     * ADDS OBJECT TO ANY PLACE IN JSON
     *
     * @param nodeDir      - String JSON path of object to be removed
     * @param newObjectKey - key of the object to be removed
     * @param object       - object being added
     * @param objectMapper - objectMapper utility
     */
    public static void addObjectToJson(String nodeDir, String newObjectKey, Object object, ObjectMapper objectMapper)
            throws IOException {
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode nestedObject = rootNode.at(nodeDir);
        JsonNode objectNode = objectMapper.valueToTree(object);
        ((ObjectNode) nestedObject).put(newObjectKey, (JsonNode) objectNode);
        String updatedJsonString = JsonUtils.serializeToJson(rootNode);
        JsonUtils.writeJsonToFile(updatedJsonString, FILE_PATH);
    }

    /**
     * REMOVES ANY OBJECT FROM JSON
     * 
     * @param nodeDir      - String JSON path of object to be removed
     * @param objectKey    - key of the object to be removed
     * @param objectMapper - objectMapper utility
     */
    public static void removeObjectFromJson(String nodeDir, String objectKey, ObjectMapper objectMapper)
            throws IOException {
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode nestedObject = rootNode.at(nodeDir);
        ((ObjectNode) nestedObject).remove(objectKey);
        String updatedJsonString = JsonUtils.serializeToJson(rootNode);
        JsonUtils.writeJsonToFile(updatedJsonString, FILE_PATH);
    }

    // IMPORTANT - CAN GET ANY OBJECT FROM KEY IN JSON !!!!!!!!!!
    public static <T> T objectByKey(ObjectMapper objectMapper, String nodeDir, Class<T> targetClass)
            throws IOException {
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode nestedObject = rootNode.at(nodeDir);
        return objectMapper.treeToValue(nestedObject, targetClass);
    }

    /**
     * Check if JSON object contains a particular key.
     * 
     * @param nodeDir      - JSON path of object.
     * @param objectKey    - key to check if exists.
     * @param objectMapper - utility.
     * @return
     * @throws IOException
     */
    public static boolean hasKey(String nodeDir, String objectKey, ObjectMapper objectMapper) throws IOException {
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode nestedObject = rootNode.at(nodeDir);
        return ((ObjectNode) nestedObject).has(objectKey);
    }

    public static JsonNode readJsonFile(ObjectMapper objectMapper) throws IOException {
        File file = new File(FILE_PATH);
        return objectMapper.readTree(file);
    }

    // Function to serialize a JsonNode to a JSON string
    public static String serializeToJson(JsonNode jsonNode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(jsonNode);
    }

    // Function to write a JSON string to a file
    public static void writeJsonToFile(String jsonString, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonString);
        }
    }
}
