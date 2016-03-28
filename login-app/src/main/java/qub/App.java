package qub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import qub.domain.User;
import qub.service.IUserService;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class App implements CommandLineRunner {
//public class qub.App extends Application implements CommandLineRunner {

    @Autowired
    IUserService userService;

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

//    @Override
//    public void start(Stage stage) throws IOException {
//        Scene scene = new Scene(new StackPane());
//
//        LoginManager loginManager = new LoginManager(scene);
//        loginManager.showLoginScreen();
//
//        stage.setScene(scene);
//        stage.show();
//    }

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void run(String... strings) throws Exception {
//        launch(strings);
        log.info("Application started");

        // Testing the db
        String first = "First_test";
        String last = "Last_test";
        String email = "Email_test";

        // Creation
        userService.createUser(first, last, email);

        // Read
        User user = userService.getUserByEmail(email);
        Assert.notNull(user);
        Assert.isTrue(user.getEmail().equals(email));
        Assert.isTrue(user.getFirstName().equals(first));
        Assert.isTrue(user.getLastName().equals(last));

        // Read
        Assert.isNull(userService.getUserByEmail("incorrect_email"));

        // Update
        User updatedUser = new User(first, "new_last", "new_email");
        userService.updateUser(email, updatedUser);
        updatedUser = userService.getUserByEmail("new_email");
        Assert.isTrue(updatedUser.getFirstName().equals(first));
        Assert.isTrue(updatedUser.getLastName().equals("new_last"));

        // Delete
        userService.deleteUser("new_email");
        Assert.isNull(userService.getUserByEmail("new_email"));
    }

}
