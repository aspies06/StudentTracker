package cs151.application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ProgrammingLanguagePage {
    private TableView<ProgrammingLanguage> table;
    private Scene scene;
    private HomePage homePage;
    private DatabaseConnector db;

    public ProgrammingLanguagePage(Stage stage) {
        VBox root = new VBox(10);
        table = new TableView<>();
        db = DatabaseConnector.getDatabaseConnector();

        Label nameLabel = new Label("Enter the name of the programming language:");
        TextField name = new TextField();
        name.setPromptText("Language Name");

        HBox buttons = new HBox(10);
        Button enter = new Button("Enter");
        Button back = new Button("Back Home");
        buttons.getChildren().addAll(enter, back);

        TableColumn<ProgrammingLanguage, String> plNameColumn = new TableColumn<>("Name");
        plNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(plNameColumn);

        table.setPlaceholder(new Label("Add a programming language"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        updateTable();

        enter.setOnAction(e -> {
            String entry = name.getText().trim();
            if (!entry.isEmpty()) {
                db.saveLanguage(entry);
                name.clear();
                updateTable();
            }
        });

        back.setOnAction(e -> {
            name.clear();
            stage.setTitle("Home");
            stage.setScene(homePage.getScene());
        });

        root.getChildren().addAll(nameLabel, name, buttons, table);
        scene = new Scene(root, 1200, 800);
    }

    public Scene getScene() {
        return scene;
    }

    public void setHomePage(HomePage home) {
        homePage = home;
    }

    private void updateTable() {
        ArrayList<String> langs = db.loadLanguages();
        table.getItems().clear();
        for (String l : langs) {
            table.getItems().add(new ProgrammingLanguage(l));
        }
    }
}
