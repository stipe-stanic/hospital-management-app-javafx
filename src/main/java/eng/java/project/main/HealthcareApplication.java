package eng.java.project.main;

import eng.java.project.database.DatabaseSource;
import eng.java.project.database.DatabaseUtil;
import eng.java.project.main.controller.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public final class HealthcareApplication extends Application {
    public static Stage appStage;
    private static final DatabaseSource databaseUtil = new DatabaseUtil();
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String WINDOW_TITLE = "Healthcare Management System";

    @Override
    public void start(Stage stage) throws IOException {
        appStage = stage;

        FXMLLoader fmxlLoader = new FXMLLoader(HealthcareApplication.class.getResource("login_view.fxml"));
        Scene scene = new Scene(fmxlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setMainPage(GridPane root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        appStage.setTitle(WINDOW_TITLE);
        appStage.setScene(scene);
        appStage.show();
    }

    public static DatabaseSource getDatabaseSource() {
        return databaseUtil;
    }

    public static Stage getAppStage() {
        return appStage;
    }
}
