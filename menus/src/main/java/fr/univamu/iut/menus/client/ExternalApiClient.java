package fr.univamu.iut.menus.client;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Client HTTP utilitaire pour enrichir les menus avec les APIs locales users/dishes.
 */
public class ExternalApiClient {
    private static final String USERS_URL = "http://localhost:3003/users";
    private static final String DISHES_URL = "http://localhost:3003/dishes";

    private final HttpClient httpClient;

    public ExternalApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public Map<Integer, String> fetchUserNamesById() {
        JsonArray users = fetchArray(USERS_URL, "users");
        Map<Integer, String> byId = new HashMap<>();
        if (users == null) {
            return byId;
        }

        for (JsonValue value : users) {
            if (value.getValueType() != JsonValue.ValueType.OBJECT) {
                continue;
            }

            JsonObject user = value.asJsonObject();
            Integer id = readInt(user, "id");
            if (id == null) {
                continue;
            }

            String name = readString(user, "lastName");

            byId.put(id, name);
        }

        return byId;
    }

    public Map<Integer, DishData> fetchDishesById() {
        JsonArray dishes = fetchArray(DISHES_URL, "dishes");
        Map<Integer, DishData> byId = new HashMap<>();
        if (dishes == null) {
            return byId;
        }

        for (JsonValue value : dishes) {
            if (value.getValueType() != JsonValue.ValueType.OBJECT) {
                continue;
            }

            JsonObject dish = value.asJsonObject();
            Integer id = readInt(dish, "id");
            if (id == null) {
                continue;
            }

            byId.put(id, new DishData(readString(dish, "name"), readDecimal(dish, "price")));
        }

        return byId;
    }

    private JsonArray fetchArray(String url, String expectedArrayName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return null;
            }

            try (JsonReader reader = Json.createReader(new StringReader(response.body()))) {
                JsonStructure structure = reader.read();
                if (structure.getValueType() == JsonValue.ValueType.ARRAY) {
                    return structure.asJsonArray();
                }

                JsonObject object = structure.asJsonObject();
                if (object.containsKey(expectedArrayName) && object.get(expectedArrayName).getValueType() == JsonValue.ValueType.ARRAY) {
                    return object.getJsonArray(expectedArrayName);
                }
            }
        } catch (Exception ignored) {
            return null;
        }

        return null;
    }

    private Integer readInt(JsonObject object, String key) {
        if (!object.containsKey(key) || object.isNull(key)) {
            return null;
        }

        JsonValue value = object.get(key);
        if (value.getValueType() == JsonValue.ValueType.NUMBER) {
            return object.getInt(key);
        }
        if (value.getValueType() == JsonValue.ValueType.STRING) {
            try {
                return Integer.parseInt(object.getString(key));
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }

    private String readString(JsonObject object, String key) {
        if (!object.containsKey(key) || object.isNull(key)) {
            return null;
        }

        JsonValue value = object.get(key);
        if (value.getValueType() == JsonValue.ValueType.STRING) {
            return ((JsonString) value).getString();
        }

        return value.toString();
    }

    private BigDecimal readDecimal(JsonObject object, String key) {
        if (!object.containsKey(key) || object.isNull(key)) {
            return null;
        }

        JsonValue value = object.get(key);
        if (value.getValueType() == JsonValue.ValueType.NUMBER) {
            return ((JsonNumber) value).bigDecimalValue();
        }
        if (value.getValueType() == JsonValue.ValueType.STRING) {
            try {
                return new BigDecimal(((JsonString) value).getString());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }

    public static class DishData {
        private final String name;
        private final BigDecimal price;

        public DishData(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}

