package fr.univamu.iut.menus.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import io.github.cdimascio.dotenv.Dotenv;
import fr.univamu.iut.menus.repository.MenuRepositoryInterface;
import fr.univamu.iut.menus.repository.MenuRepositoryMariadb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApplicationPath("/api")
@ApplicationScoped
public class MenuApplication extends Application {

    /**
     * Méthode appelée par l'API CDI pour injecter la connection à la base de données au moment de la création
     * de la ressource
     * @return un objet implémentant l'interface MenuRepositoryInterface utilisée
     *          pour accéder aux données des menus, voire les modifier
     */
    @Produces
    private MenuRepositoryInterface openDbConnection(){
        try {
            List<Dotenv> dotenvCandidates = loadDotenvCandidates();
            Map<String, String> classpathConfig = loadClasspathDotenv();

            String infoConnection = getConfigValue(dotenvCandidates, classpathConfig, "INFO_CONNECTION");
            String user = getConfigValue(dotenvCandidates, classpathConfig, "USER");
            String password = getConfigValue(dotenvCandidates, classpathConfig, "PASSWORD");

            if (isBlank(infoConnection) || isBlank(user) || password == null) {
                throw new IllegalStateException("Configuration base de donnees manquante: INFO_CONNECTION, USER, PASSWORD");
            }

            return new MenuRepositoryMariadb(infoConnection, user, password);
        } catch (Exception e) {
            throw new IllegalStateException("Impossible d'initialiser la connexion a la base de donnees", e);
        }
    }

    /**
     * Méthode permettant de fermer la connexion à la base de données lorsque l'application est arrêtée
     * @param menuRepo la connexion à la base de données instanciée dans la méthode @openDbConnection
     */
    private void closeDbConnection(@Disposes MenuRepositoryInterface menuRepo ) {
        if (menuRepo != null) {
            menuRepo.close();
        }
    }

    private String getConfigValue(List<Dotenv> dotenvCandidates, Map<String, String> classpathConfig, String key) {
        String systemPropertyValue = System.getProperty(key);
        if (!isBlank(systemPropertyValue)) {
            return systemPropertyValue;
        }

        String envValue = System.getenv(key);
        if (!isBlank(envValue)) {
            return envValue;
        }

        for (Dotenv dotenv : dotenvCandidates) {
            String dotenvValue = dotenv != null ? dotenv.get(key) : null;
            if (!isBlank(dotenvValue)) {
                return dotenvValue;
            }
        }

        String classpathValue = classpathConfig.get(key);
        if (!isBlank(classpathValue)) {
            return classpathValue;
        }

        return null;
    }

    private Map<String, String> loadClasspathDotenv() {
        Map<String, String> values = new HashMap<>();
        InputStream stream = MenuApplication.class.getClassLoader().getResourceAsStream(".env");
        if (stream == null) {
            return values;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                int separator = trimmed.indexOf('=');
                if (separator <= 0) {
                    continue;
                }

                String key = trimmed.substring(0, separator).trim();
                String value = trimmed.substring(separator + 1).trim();
                values.put(key, unquote(value));
            }
        } catch (IOException ignored) {
            return values;
        }

        return values;
    }

    private String unquote(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }

        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }

    private List<Dotenv> loadDotenvCandidates() {
        List<Dotenv> result = new ArrayList<>();

        // Dossier de travail courant (utile en dev local).
        result.add(loadDotenvFrom("."));
        // Emplacement standard si le fichier est déplacé en resources.
        result.add(loadDotenvFrom("src/main/resources"));
        // Compatibilite avec l'emplacement actuel du projet.
        result.add(loadDotenvFrom("src/main/java/fr/univamu/iut/menus"));

        return result;
    }

    private Dotenv loadDotenvFrom(String directory) {
        return Dotenv.configure()
                .directory(directory)
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

