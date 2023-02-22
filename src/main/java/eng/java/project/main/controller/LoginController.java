package eng.java.project.main.controller;

import eng.java.project.entity.hospital.util.InsuranceProvider;
import eng.java.project.entity.hospital.util.Symptom;
import eng.java.project.main.GlobalUser;
import eng.java.project.main.HealthcareApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String FILE_USERS = "files\\users.txt";
    GlobalUser user = GlobalUser.getInstance();

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void showHomeScreen() {
        if(checkLoginInput()) {
            try {
                GridPane root;
                root = FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("start_view.fxml")));
                HealthcareApplication.setMainPage(root);
            } catch (IOException ex) {
                String message = "Error occured while changing view to start_view after logging in";
                logger.error(message, ex);
            }
        } else {
            username.clear();
            password.clear();
        }
    }

    private boolean checkLoginInput() {
        Map<String, List<String>> users = new HashMap<>();
        try(Scanner scanner = new Scanner(new File(FILE_USERS))) {
            while(scanner.hasNextLine()) {
                String user = scanner.nextLine();
                String pass = scanner.nextLine();
                String role = scanner.nextLine();

                users.put(user, new ArrayList<>(Arrays.asList(pass, role)));
            }
        } catch (FileNotFoundException ex) {
            String message = "Error occured while reading users.txt from file";
            logger.error(message, ex);
        }

        String hashedPassword = null;
        try {
            hashedPassword = hashPassword(password.getText());
        } catch (NoSuchAlgorithmException ex) {
            String message = "Error occurred while hashing password";
            logger.error(message, ex);
        }

        if(users.containsKey(username.getText())) {
            List<String> values = users.get(username.getText());
            if(values.get(0).equals(hashedPassword)) {
                user.setCredentials(username.getText(), values.get(1));
                return true;
            }
        }

        return false;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toString(0xff & b);
            if(hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}
