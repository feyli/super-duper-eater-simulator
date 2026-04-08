package fr.univamu.iut.dishes_users;

import fr.univamu.iut.dishes_users.dishes.DishesResource;
import fr.univamu.iut.dishes_users.dishes.DishesServices;
import fr.univamu.iut.dishes_users.users.UsersResource;
import fr.univamu.iut.dishes_users.users.UsersServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
@ApplicationScoped
public class DishesUsersApplication extends Application {

    @Produces
    private DishesUsersRepositoryInterface openDbConnection(){
        DishesUsersRepositoryMariadb db = null;
        try{
            db = new DishesUsersRepositoryMariadb("jdbc:mariadb://mysql-dealtoniut.alwaysdata.net/dealtoniut_dishes_users_db", "dealtoniut_dishes_users","Kikouine.123");//System.getenv("DB_INFO"), System.getenv("DB_USER"), System.getenv("DB_PWD"));
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        return db;
    }

    private void closeDbConnection(@Disposes DishesUsersRepositoryInterface dishRepo, DishesUsersRepositoryInterface userRepo){dishRepo.close(); userRepo.close();}
}