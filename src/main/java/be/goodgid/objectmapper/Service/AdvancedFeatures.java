package be.goodgid.objectmapper.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Lists;

import be.goodgid.objectmapper.Domain.Car;
import be.goodgid.objectmapper.Domain.WrapperCar;
import be.goodgid.objectmapper.Serializable.CustomCarDeserializer;
import be.goodgid.objectmapper.Serializable.CustomCarSerializer;

/**
 * ref : https://www.baeldung.com/jackson-object-mapper-tutorial#3-json-to-jackson-jsonnode
 */
public class AdvancedFeatures {

    private ObjectMapper objectMapper = new ObjectMapper();

    private String json = "{ \"color\" : \"Black\", \"type\" : \"Bentley\" }";

    // 4.1. Configuring Serialization or Deserialization Feature
    public void mapper_4_1() {
        Car car = new Car();

        String jsonString
                = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";

        try {
            car = objectMapper.readValue(jsonString, Car.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            /**
             com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "year" (class be.goodgid.objectmapper.Domain.Car), not marked as ignorable (2 known properties: "type", "color"])
             at [Source: (String)"{ "color" : "Black", "type" : "Fiat", "year" : "1970" }"; line: 1, column: 49] (through reference chain: be.goodgid.objectmapper.Domain.Car["year"])
             */
        }

        // Through the configure method
        // we can extend the default process to ignore the new fields
        // --> Unknow Propertie는 무시한다.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            car = objectMapper.readValue(jsonString, Car.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        /**
         car = {Car@913}
         color = "Black"
         type = "Fiat"
         // year 값은 무시한 채 Json Parsing이 되었다.
         */

        System.out.println("===== objectMapper.configure() =====");
        System.out.println("Color : " + car.getColor());
        System.out.println("Type : " + car.getType());

        // JsonNode는 특정 Class에 종속되지 않기 때문에
        // 주어진 Json String 파싱이 가능하다.
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("===== JsonNode =====");
        System.out.println("Color : " + jsonNode.get("color").asText());
        System.out.println("Type : " + jsonNode.get("type").asText());
        System.out.println("Year : " + jsonNode.get("year").asText());
    }

    // 4.2. Creating Custom Serializer or Deserializer
    public void mapper_4_2() {

        // This custom serializer can be invoked like this
        {
            SimpleModule module =
                    new SimpleModule("CustomCarSerializer",
                                     new Version(1, 0, 0, null, null, null));

            module.addSerializer(Car.class, new CustomCarSerializer());
            objectMapper.registerModule(module);

            Car car = new Car("Green", "Bentley");

            String carJson = null;
            try {
                carJson = objectMapper.writeValueAsString(car);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            System.out.println("===== Serializer =====");
            System.out.println("carJson : " + carJson);
        }

        // This custom deserializer can be invoked in this way
        {
            SimpleModule module =
                    new SimpleModule("CustomCarDeserializer",
                                     new Version(1, 0, 0, null, null, null));

            module.addDeserializer(Car.class, new CustomCarDeserializer());
            objectMapper.registerModule(module);

            Car car2 = new Car();
            try {
                car2 = objectMapper.readValue(json, Car.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            System.out.println("===== Deserializer =====");
            System.out.println("Color : " + car2.getColor());
            System.out.println("Type : " + car2.getType());
        }
    }

    // 4.3. Handling Date Formats
    public void mapper_4_3() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
        objectMapper.setDateFormat(df);

        WrapperCar wrapperCar = new WrapperCar();
        Car car = new Car("Green", "Bentley");
        wrapperCar.setCar(car);
        wrapperCar.setDatePurchased(new Date());

        String carAsString = null;
        try {
            carAsString = objectMapper.writeValueAsString(wrapperCar);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("carAsString : " + carAsString);
        // Output : {"car":{"color":"Green","type":"Bentley"},"datePurchased":"2020-09-13 22:20 PM KST"}
    }

    // 4.4. Handling Collections
    public void mapper_4_4() {
        String jsonCarArray =
                "[{ \"color\" : \"Black\", \"type\" : \"Bentley\" }, { \"color\" : \"Red\", \"type\" : \"Maserati\" }]";

        List<Car> listCar = Lists.newArrayList();

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

    public static void main(String[] args) {
        AdvancedFeatures service = new AdvancedFeatures();
//        service.mapper_4_1();
//        service.mapper_4_2();
//        service.mapper_4_3();
        service.mapper_4_4();
    }

}
