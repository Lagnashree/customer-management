package com.lagnashree.customermanagement.repository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
@Repository
public class CustomerManagementRepository {

    /**
     * This function reads file that conatins customer details as JSON format and return JSON Object .
     * @return JSONObject object which contains personal details of a customer.
     * @throws IOException If there is IOEception while reading the JSON file
     * @throws ParseException If there is parsing exception while reading the JSON contain.
     */
    public JSONObject readCustomerData() throws IOException, ParseException {
            String filePath = "src/main/resources/Person.json";
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            return (JSONObject) obj;
        } catch (IOException e) {
            throw new IOException("Error reading customer data file", e);
        }
    }
}
