package be.goodgid.objectmapper.Service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import be.goodgid.objectmapper.Domain.Car;

/**
 * ref : https://www.baeldung.com/jackson-object-mapper-tutorial#3-json-to-jackson-jsonnode
 */
public class DefaultObjectMapper {

    private ObjectMapper objectMapper = new ObjectMapper();

    private String json = "{ \"color\" : \"Green\", \"type\" : \"Bentley\" }";

    // 3.2. JSON to Java Object
    public void mapper_3_2() {
        Car car = new Car();
        try {
            car = objectMapper.readValue(json, Car.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Color : " + car.getColor());
        System.out.println("Type : " + car.getType());
    }

    // 3.3. JSON to Jackson JsonNode
    public void mapper_3_3() {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Color : " + jsonNode.get("color").asText());
        System.out.println("Type : " + jsonNode.get("type").asText());
    }

    // 3.4. Creating a Java List From a JSON Array String
    public void mapper_3_4() {
        List<Car> listCar = Lists.newArrayList();
        String jsonCarArray =
                "[{ \"color\" : \"Green\", \"type\" : \"Bentley\" }, { \"color\" : \"Red\", \"type\" : \"Maserati\" }]";

        try {
            listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Index : 1 ");
        System.out.println("Color : " + listCar.get(0).getColor());
        System.out.println("Type : " + listCar.get(0).getType());

        System.out.println("\nIndex : 2 ");
        System.out.println("Color : " + listCar.get(1).getColor());
        System.out.println("Type : " + listCar.get(1).getType());
    }

    // 3.5. Creating Java Map From JSON String
    public void mapper_3_5() {
        Map<String, Object> map = Maps.newHashMap();
        try {
            map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        /**
         "color" -> "Green"
         "type" -> "Bentley"
         */
        System.out.println("Color : " + map.get("color"));
        System.out.println("Type : " + map.get("type"));
    }

    public static void main(String[] args) {
        DefaultObjectMapper service = new DefaultObjectMapper();
//        service.mapper_3_2();
//        service.mapper_3_3();
//        service.mapper_3_4();
//        service.mapper_3_5();
    }

}
