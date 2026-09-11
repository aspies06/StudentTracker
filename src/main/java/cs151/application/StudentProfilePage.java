package cs151.application;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class StudentProfilePage {
    private static final String[] statuses = {"Freshmen", "Sophomore", "Junior", "Senior", "Graduate"};
    private static final String[] databases = {"MySQL", "Postgres", "MongoDB", "None"};
    private static final String[] profRoles = {"Front-End", "Back-End", "Full-Stack", "Data Analyst", "Other"};

    private TextField fullName;
    private ListView<String> academicStatuses;
    private RadioButton yes;
    private RadioButton no;
    private TextField jobDetails;
    private ListView<ProgrammingLanguage> langs;
    private ListView<String> dbs;
    private ListView<String> roles;
    private CheckBox white;
    private CheckBox black;

    private TableView<StudentProfile> table;
    private DatabaseConnector db;
    private Scene scene;
    private HomePage homePage;

    public StudentProfilePage(Stage stage) {
        VBox root = new VBox(5);
        table = new TableView<>();
        db = DatabaseConnector.getDatabaseConnector();

        Label nameLabel = new Label("Type your full name:");
        fullName = new TextField();
        fullName.setPromptText("Full Name");

        Label statusLabel = new Label("Select your year:");
        academicStatuses = new ListView<>();
        academicStatuses.getItems().addAll(statuses);
        academicStatuses.setPrefHeight(100);
        academicStatuses.setMinHeight(100);

        HBox employed = new HBox(10);
        ToggleGroup options = new ToggleGroup();
        yes = new RadioButton("Employed");
        no = new RadioButton("Not Employed");
        yes.setToggleGroup(options);
        no.setToggleGroup(options);
        no.setSelected(true);
        jobDetails = new TextField();
        jobDetails.setPromptText("Job Details (if employed)");
        employed.getChildren().addAll(yes, no, jobDetails);

        Label langsLabel = new Label("Select the programming languages you know:");
        langs = new ListView<>();
        langs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        langs.setPrefHeight(100);
        langs.setMinHeight(100);

        Label dbsLabel = new Label("Select the databases you know:");
        dbs = new ListView<>();
        dbs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dbs.getItems().addAll(databases);
        dbs.setPrefHeight(100);
        dbs.setMinHeight(100);

        Label rolesLabel = new Label("Select your preferred role:");
        roles = new ListView<>();
        roles.getItems().addAll(profRoles);
        roles.setPrefHeight(100);
        roles.setMinHeight(100);

        HBox wb = new HBox(10);
        white = new CheckBox("Whitelist");
        black = new CheckBox("Blacklist");
        wb.getChildren().addAll(white, black);

        HBox buttons = new HBox(10);
        Button enter = new Button("Enter");
        Button delete = new Button("Delete");
        Button back = new Button("Back Home");
        buttons.getChildren().addAll(enter, delete, back);

        setUpTable();
        table.setPlaceholder(new Label("Add a student profile"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        updateProfiles();

        root.getChildren().addAll(
                nameLabel, fullName, statusLabel, academicStatuses,
                employed, langsLabel, langs, dbsLabel, dbs, rolesLabel,
                roles, wb, buttons, table
        );

        back.setOnAction(e -> {
            reset();
            stage.setTitle("Home");
            stage.setScene(homePage.getScene());
        });

        delete.setOnAction(e -> {
            StudentProfile selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("No Student Profile Selected");
                a.setContentText("A student profile must be selected for deletion!");
                a.show();
                return;
            }
            db.deleteProfile(selected);
            updateProfiles();
        });

        enter.setOnAction(e -> {
            String name = fullName.getText().trim();
            String status = academicStatuses.getSelectionModel().getSelectedItem();
            RadioButton selectedToggle = (RadioButton) options.getSelectedToggle();
            boolean isEmployed = selectedToggle == yes;
            String job = jobDetails.getText().trim();
            ObservableList<ProgrammingLanguage> selectedLangs = langs.getSelectionModel().getSelectedItems();
            ObservableList<String> selectedDbs = dbs.getSelectionModel().getSelectedItems();
            String role = roles.getSelectionModel().getSelectedItem();
            boolean w = white.isSelected();
            boolean b = black.isSelected();

            Alert a = new Alert(Alert.AlertType.ERROR);
            if (name.isEmpty() || status == null || selectedLangs.isEmpty() || selectedDbs.isEmpty() || role == null) {
                a.setTitle("Empty Field");
                a.setContentText("Required field(s) missing! Make sure you have entered a name, academic status, " +
                        "at least one language and database, and preferred role!");
                a.show();
                return;
            }
            if (isEmployed && job.isEmpty()) {
                a.setTitle("Empty Field");
                a.setContentText("Job details required for employed students!");
                a.show();
                return;
            }
            if (w && b) {
                a.setTitle("Empty Field");
                a.setContentText("Cannot be both whitelisted and blacklisted!");
                a.show();
                return;
            }

            StudentProfile s = new StudentProfile(name, status, isEmployed, job, new ArrayList<>(selectedLangs),
                    new ArrayList<>(selectedDbs), role, new ArrayList<String>(), w, b);
            db.saveProfile(s);
            updateProfiles();
            reset();
        });
        scene = new Scene(root, 1200, 800);
    }

    public Scene getScene() {
        return scene;
    }

    public void setHomePage(HomePage home) {
        homePage = home;
    }

    public void updateLangs() {
        ArrayList<String> newLangs = db.loadLanguages();
        langs.getItems().clear();
        for (String l : newLangs) {
            langs.getItems().add(new ProgrammingLanguage(l));
        }
    }

    private void reset() {
        fullName.clear();
        academicStatuses.getSelectionModel().clearSelection();
        no.setSelected(true);
        jobDetails.clear();
        langs.getSelectionModel().clearSelection();
        dbs.getSelectionModel().clearSelection();
        roles.getSelectionModel().clearSelection();
        white.setSelected(false);
        black.setSelected(false);
    }

    private void setUpTable() {
        table.getColumns().clear();
        TableColumn<StudentProfile, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudentProfile, String> academicColumn = new TableColumn<>("Academic Status");
        academicColumn.setCellValueFactory(new PropertyValueFactory<>("academicStatus"));

        TableColumn<StudentProfile, Boolean> employedColumn = new TableColumn<>("Employment Status");
        employedColumn.setCellValueFactory(new PropertyValueFactory<>("employed"));

        TableColumn<StudentProfile, String> jobColumn = new TableColumn<>("Job Details");
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("jobDetails"));

        TableColumn<StudentProfile, ArrayList<ProgrammingLanguage>> languagesColumn = new TableColumn<>("Programming Languages");
        languagesColumn.setCellValueFactory(new PropertyValueFactory<>("languages"));

        TableColumn<StudentProfile, ArrayList<String>> databasesColumn = new TableColumn<>("Databases");
        databasesColumn.setCellValueFactory(new PropertyValueFactory<>("databases"));

        TableColumn<StudentProfile, String> roleColumn = new TableColumn<>("Preferred Professional Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));

        TableColumn<StudentProfile, String> commentColumn = new TableColumn<>("Comments");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        TableColumn<StudentProfile, Boolean> spWhiteListColumn = new TableColumn<>("White List");
        spWhiteListColumn.setCellValueFactory(new PropertyValueFactory<>("whiteList"));

        TableColumn<StudentProfile, Boolean> spBlackListColumn = new TableColumn<>("Black List");
        spBlackListColumn.setCellValueFactory(new PropertyValueFactory<>("blackList"));

        table.getColumns().addAll(nameColumn, academicColumn, employedColumn, jobColumn, languagesColumn,
                databasesColumn, roleColumn, commentColumn, spWhiteListColumn, spBlackListColumn);
    }

    public void updateProfiles() {
        ArrayList<String> newLangs = db.loadLanguages();
        langs.getItems().clear();
        for (String l : newLangs) {
            langs.getItems().add(new ProgrammingLanguage(l));
        }

        ArrayList<StudentProfile> profiles = db.loadProfiles();
        table.getItems().clear();
        table.getItems().addAll(profiles);
    }
}