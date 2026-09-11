package cs151.application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

public class EditStudentProfilesPage {
    private Scene scene;
    private SearchStudentProfilesPage homePage;

    private TextField nameField;
    private ComboBox<String> statusField;
    private CheckBox employedCheck;
    private TextField jobField;
    private ListView<String> langs;
    private ComboBox<String> roleField;
    private CheckBox wlCheck;
    private CheckBox blCheck;
    private DatabaseConnector db;
    private ListView<String> dbs;

    public EditStudentProfilesPage(Stage stage, StudentProfile student) {
        db = DatabaseConnector.getDatabaseConnector();
        VBox root = new VBox(10);
        stage.setTitle("Edit Student Profile");

        nameField = new TextField(student.getName());

        statusField = new ComboBox<>();
        statusField.getItems().addAll("Freshman", "Sophomore", "Junior", "Senior", "Graduate");
        statusField.setValue(student.getAcademicStatus());

        employedCheck = new CheckBox("Employed");
        employedCheck.setSelected(student.isEmployed());

        jobField = new TextField(student.getJobDetails());

        langs = new ListView<>();
        langs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        langs.getItems().addAll(db.loadLanguages()); // load from database connector
        langs.setPrefHeight(100);
        for (ProgrammingLanguage pl : student.getLanguages()) {
            langs.getSelectionModel().select(pl.getName());
        }

        dbs = new ListView<>();
        dbs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dbs.getItems().addAll("MongoDB", "MySQL", "Postgres", "None");
        dbs.setPrefHeight(100);
        for (String dbName : student.getDatabases()) {
            dbs.getSelectionModel().select(dbName);
        }

        roleField = new ComboBox<>();
        roleField.getItems().addAll("Front-End", "Back-End", "Full-Stack", "Data Analyst", "Other");
        roleField.setValue(student.getPreferredRole());

        wlCheck = new CheckBox("Whitelist");
        wlCheck.setSelected(student.isWhiteList());
        blCheck = new CheckBox("Blacklist");
        blCheck.setSelected(student.isBlackList());
        HBox checkboxes = new HBox(10, wlCheck, blCheck);

        Button saveBtn = new Button("Save Changes");
        Button cancelBtn = new Button("Cancel");
        HBox buttons = new HBox(10, saveBtn, cancelBtn);

        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String status = statusField.getSelectionModel().getSelectedItem();
            boolean employed = employedCheck.isSelected();
            String job = jobField.getText().trim();

            ArrayList<ProgrammingLanguage> langList = new ArrayList<>();
            ArrayList<String> selectedLangs = new ArrayList<>(langs.getSelectionModel().getSelectedItems());
            Collections.sort(selectedLangs);
            for (String lang : selectedLangs) {
                langList.add(new ProgrammingLanguage(lang));
            }

            ArrayList<String> dbList = new ArrayList<>(dbs.getSelectionModel().getSelectedItems());
            Collections.sort(dbList);

            String role = roleField.getSelectionModel().getSelectedItem();
            boolean white = wlCheck.isSelected();
            boolean black = blCheck.isSelected();

            StudentProfile edited = new StudentProfile(
                    name, status, employed, job, langList, dbList, role,
                    student.getComments(), white, black
            );
            db.deleteProfile(student);
            db.saveProfile(edited);

            homePage.updateTable();
            stage.setScene(homePage.getScene());
            stage.setTitle("Search Student Profiles");
        });

        cancelBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancelled");
            alert.setHeaderText(null);
            alert.setContentText("Changes discarded.");
            alert.showAndWait();
            stage.setScene(homePage.getScene());
            stage.setTitle("Search Student Profiles");
        });

        root.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Academic Status:"), statusField,
                employedCheck,
                new Label("Job Details:"), jobField,
                new Label("Programming Languages:"), langs,
                new Label("Databases:"), dbs,
                new Label("Preferred Role:"), roleField,
                checkboxes,
                buttons
        );
        scene = new Scene(root, 1200, 800);
    }

    public Scene getScene() {
        return scene;
    }

    public void setHomePage(SearchStudentProfilesPage home) {
        homePage = home;
    }
}