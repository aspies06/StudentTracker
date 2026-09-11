package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Locale;
import javafx.scene.control.Alert;

public class SearchStudentProfilesPage {
    private TextField nameContains;
    private ComboBox<String> status;
    private ComboBox<String> employed;
    private ListView<String> dbs;
    private ComboBox<String> role;
    private CheckBox anyLanguage;
    private CheckBox anyDatabase;
    private ListView<String> langs;
    private ArrayList<String> langSet;
    private ArrayList<StudentProfile> profiles;

    private TableView<StudentProfile> table;
    private DatabaseConnector db;
    private Scene scene;
    private HomePage home;
    private Stage stage;

    public SearchStudentProfilesPage(Stage s) {
        stage = s;
        table = new TableView<>();
        db = DatabaseConnector.getDatabaseConnector();
        nameContains = new TextField();
        nameContains.setPromptText("Name contains…");

        status = new ComboBox<>();
        status.setPromptText("Any status");
        status.setValue("Any status");

        employed = new ComboBox<>();
        employed.getItems().addAll("Any", "Employed", "Not Employed");
        employed.setValue("Any");

        langs = new ListView<>();
        langs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        langSet = db.loadLanguages();

        dbs = new ListView<>();
        dbs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        role = new ComboBox<>();
        role.setPromptText("Any role");

        anyLanguage = new CheckBox("Any language");
        anyLanguage.setSelected(true);
        anyDatabase = new CheckBox("Any database");
        anyDatabase.setSelected(true);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        int r = 0;
        form.add(new Label("Name"), 0, r); form.add(nameContains, 1, r++);
        form.add(new Label("Academic Status"), 0, r); form.add(status, 1, r++);
        form.add(new Label("Employment"), 0, r); form.add(employed, 1, r++);

        VBox langBox = new VBox(6, langs, anyLanguage);
        form.add(new Label("Languages (select any)"), 0, r); form.add(langBox, 1, r++);

        VBox dbBox = new VBox(6, dbs, anyDatabase);
        form.add(new Label("Databases (select any)"), 0, r); form.add(dbBox, 1, r++);

        langs.setPrefHeight(150);
        dbs.setPrefHeight(150);

        form.add(new Label("Preferred Role"), 0, r); form.add(role, 1, r++);

        Button searchBtn = new Button("Search");
        Button editBtn = new Button("Edit");
        Button commentBtn = new Button("Add Comment");
        Button resetBtn  = new Button("Reset");
        Button backBtn   = new Button("Back Home");
        HBox actions = new HBox(10, searchBtn, resetBtn, editBtn, commentBtn, backBtn);

        setUpTable();
        table.setPlaceholder(new Label("Run a search to see results."));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox root = new VBox(12, form, actions, table);
        scene = new Scene(root, 1200, 800);
        refreshFacetOptions();

        searchBtn.setOnAction(e -> applySearch());
        resetBtn.setOnAction(e -> updateTable());

        backBtn.setOnAction(e -> {
            if (home != null) {
                Stage currS = (Stage) scene.getWindow();
                currS.setScene(home.getScene());
                currS.setTitle("Home Page");
            }
        });

        editBtn.setOnAction(e -> {
            StudentProfile selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning();
                return;
            }
            stage = (Stage) scene.getWindow();
            EditStudentProfilesPage editPage = new EditStudentProfilesPage(stage, selected);
            editPage.setHomePage(this);
            stage.setScene(editPage.getScene());
            stage.setTitle("Edit Student Profile");
        });

        commentBtn.setOnAction(e -> {
            StudentProfile selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning();
                return;
            }
            stage = (Stage) scene.getWindow();
            AddCommentPage commentPage = new AddCommentPage(stage, selected);
            commentPage.setHomePage(this);
            stage.setScene(commentPage.getScene());
            stage.setTitle("Add Comment");
        });

        anyLanguage.setOnAction(e -> {
            if (anyLanguage.isSelected() && !langs.getSelectionModel().getSelectedItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Warning");
                alert.setHeaderText(null);
                alert.setContentText("You cannot select 'Any language' while specific languages are already selected.");
                alert.showAndWait();
                anyLanguage.setSelected(false);
            }
        });

        anyDatabase.setOnAction(e -> {
            if (anyDatabase.isSelected() && !dbs.getSelectionModel().getSelectedItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Warning");
                alert.setHeaderText(null);
                alert.setContentText("You cannot select 'Any database' while specific databases are already selected.");
                alert.showAndWait();
                anyDatabase.setSelected(false);
            }
        });

        langs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (anyLanguage.isSelected() && newVal != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Warning");
                alert.setHeaderText(null);
                alert.setContentText("You cannot select specific languages while 'Any language' is checked.");
                alert.showAndWait();
                langs.getSelectionModel().clearSelection();
            }
        });

        dbs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (anyDatabase.isSelected() && newVal != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Warning");
                alert.setHeaderText(null);
                alert.setContentText("You cannot select specific databases while 'Any database' is checked.");
                alert.showAndWait();
                dbs.getSelectionModel().clearSelection();
            }
        });
    }

    public Scene getScene() { return scene; }

    public void setHomePage(HomePage h) { this.home = h; }

    public void updateLangs() {
        langSet = db.loadLanguages();
        refreshFacetOptions();
    }

    public void updateTable() {
        resetForm();
        table.getItems().clear();
    }

    private void showWarning() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("No Student Profile Selected");
        a.setContentText("A student profile must be selected to be edited!");
        a.show();
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

    private void refreshFacetOptions() {
        String[] dbSet = {"MongoDB", "MySQL", "Postgres", "None"};
        String[] roleSet = {"Front-End", "Back-End", "Full-Stack", "Data Analyst", "Other"};
        String[] statSet = {"Freshmen", "Sophomore", "Junior", "Senior", "Graduate"};

        langs.getItems().setAll(langSet);
        dbs.getItems().setAll(dbSet);

        status.getItems().clear();
        status.getItems().add("Any status");
        status.getItems().addAll(statSet);

        role.getItems().clear();
        role.getItems().add("Any role");
        role.getItems().addAll(roleSet);
    }

    private void applySearch() {
        profiles = db.loadProfiles();
        ArrayList<StudentProfile> out = new ArrayList<>();

        String nameNeedle = norm(nameContains.getText());
        String statusExact = status.getValue();
        String roleExact = role.getValue();

        ArrayList<String> langNeedles = new ArrayList<>();
        if (!anyLanguage.isSelected()) {
            ObservableList<String> sel = langs.getSelectionModel().getSelectedItems();
            for (String v : sel) {
                String n = norm(v);
                if (n != null) langNeedles.add(n);
            }
        }

        ArrayList<String> dbNeedles = new ArrayList<>();
        if (!anyDatabase.isSelected()) {
            ObservableList<String> sel = dbs.getSelectionModel().getSelectedItems();
            for (String v : sel) {
                String n = norm(v);
                if (n != null) dbNeedles.add(n);
            }
        }

        String employedSel = employed.getValue();
        boolean filterEmployed = (employedSel != null && !"Any".equals(employedSel));
        boolean wantEmployed = "Employed".equalsIgnoreCase(employedSel);

        for (StudentProfile s : profiles) {
            if (s == null) continue;

            if (nameNeedle != null) {
                String hay = norm(s.getName());
                if (hay == null || !hay.contains(nameNeedle)) continue;
            }

            if (statusExact != null && !"Any status".equalsIgnoreCase(statusExact)) {
                String val = norm(s.getAcademicStatus());
                if (!equalsCI(val, norm(statusExact))) continue;
            }

            if (roleExact != null && !"Any role".equalsIgnoreCase(roleExact)) {
                String val = norm(s.getPreferredRole());
                if (!equalsCI(val, norm(roleExact))) continue;
            }

            if (!langNeedles.isEmpty()) {
                boolean found = false;
                ArrayList<ProgrammingLanguage> stuLangs = s.getLanguages();
                if (stuLangs != null) {
                    for (ProgrammingLanguage pl : stuLangs) {
                        String lname = pl != null ? norm(pl.getName()) : null;
                        if (lname != null) {
                            for (String needle : langNeedles) {
                                if (equalsCI(lname, needle)) { found = true; break; }
                            }
                        }
                        if (found) break;
                    }
                }
                if (!found) continue;
            }

            if (!dbNeedles.isEmpty()) {
                boolean found = false;
                ArrayList<String> stuDbs = s.getDatabases();
                if (stuDbs != null) {
                    for (String db : stuDbs) {
                        String d = norm(db);
                        if (d != null) {
                            for (String needle : dbNeedles) {
                                if (equalsCI(d, needle)) { found = true; break; }
                            }
                        }
                        if (found) break;
                    }
                }
                if (!found) continue;
            }

            if (filterEmployed) {
                if (s.isEmployed() != wantEmployed) continue;
            }
            out.add(s);
        }
        table.setItems(FXCollections.observableArrayList(out));
    }

    private void resetForm() {
        nameContains.clear();
        status.setValue("Any status");
        status.setPromptText("Any status");
        employed.setValue("Any");
        langs.getSelectionModel().clearSelection();
        dbs.getSelectionModel().clearSelection();
        role.setValue("Any role");
        role.setPromptText("Any role");
        anyLanguage.setSelected(true);
        anyDatabase.setSelected(true);
    }

    private static String norm(String s) {
        if (s == null) return null;
        String t = s.trim().toLowerCase(Locale.ROOT);
        return t.isEmpty() ? null : t;
    }

    private static boolean equalsCI(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}