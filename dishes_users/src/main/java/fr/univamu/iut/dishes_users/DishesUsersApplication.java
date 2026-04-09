package fr.univamu.iut.dishes_users;

import fr.univamu.iut.dishes_users.repository.DishesUsersRepositoryInterface;
import fr.univamu.iut.dishes_users.repository.DishesUsersRepositoryMariadb;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;

/**
 * Main application class for the dishes and users REST API
 */
@ApplicationPath("/api")
@ApplicationScoped
public class DishesUsersApplication extends Application {

    /**
     * Produces a database connection for dependency injection
     * @return DishesUsersRepositoryInterface database repository instance
     */
    @Produces
    private DishesUsersRepositoryInterface openDbConnection(){
        DishesUsersRepositoryMariadb db = null;
        try{
            db = new DishesUsersRepositoryMariadb(System.getenv("DB_INFO"), System.getenv("DB_USER"), System.getenv("DB_PWD"));
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Closes database connections when disposing
     * @param dishRepo dishes repository to close
     * @param userRepo users repository to close
     */
    private void closeDbConnection(@Disposes DishesUsersRepositoryInterface dishRepo, DishesUsersRepositoryInterface userRepo){dishRepo.close(); userRepo.close();}
}