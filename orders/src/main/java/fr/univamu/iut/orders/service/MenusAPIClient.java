package fr.univamu.iut.orders.service;

import fr.univamu.iut.orders.model.Menu;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP client for interacting with the Menus API.
 * Fetches menu information needed for order processing.
 */
public class MenusAPIClient {
    
    private static final String MENUS_API_BASE_URL = "http://localhost:3004";
    private final Jsonb jsonb;
    
    public MenusAPIClient() {
        this.jsonb = JsonbBuilder.create();
    }
    
    /**
     * Fetches a menu by its ID from the Menus API.
     * 
     * @param menuId the menu ID to fetch
     * @return the Menu object
     * @throws IOException if the API call fails
     * @throws MenuNotFoundException if the menu doesn't exist (404)
     */
    public Menu getMenuById(int menuId) throws IOException, MenuNotFoundException {
        String urlString = MENUS_API_BASE_URL + "/menus/" + menuId;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == 404) {
                throw new MenuNotFoundException("Menu with ID " + menuId + " not found");
            }
            
            if (responseCode != 200) {
                throw new IOException("Menus API returned status code: " + responseCode);
            }
            
            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            // Parse JSON to Menu object
            return jsonb.fromJson(response.toString(), Menu.class);
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Exception thrown when a menu is not found in the Menus API.
     */
    public static class MenuNotFoundException extends Exception {
        public MenuNotFoundException(String message) {
            super(message);
        }
    }
}
