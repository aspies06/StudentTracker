package cs151.application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ReportPage {
    private RadioButton wl;
    private RadioButton bl;
    private ToggleGroup options;
    private TableView<StudentProfile> table;
    private DatabaseConnector db;
    private Scene scene;
    private HomePage homePage;

    public ReportPage(Stage stage) {
        table = new TableView<>();
        db = DatabaseConnector.getDatabaseConnector();
        VBox root = new VBox(10);

        HBox optionsBox = new HBox(10);
        options = new ToggleGroup();
        wl = new RadioButton("Whitelisted");
        bl = new RadioButton("Blacklisted");
        wl.setToggleGroup(options);
        bl.setToggleGroup(options);
        wl.setSelected(true);
        optionsBox.getChildren().addAll(wl, bl);

        HBox buttons = new HBox(10);
        Button homeButton = new Button("Back Home");
        Button generateButton = new Button("Generate report");
        buttons.getChildren().addAll(generateButton, homeButton);

        homeButton.setOnAction(e -> stage.setScene(homePage.getScene()));

        generateButton.setOnAction(e -> updateTable());

        setUpTable();
        root.getChildren().addAll(optionsBox, buttons, table);
        scene = new Scene(root, 1200, 800);

        table.setRowFactory(tv -> {
            TableRow<StudentProfile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    StudentProfile selected = row.getItem();
                    StudentReportDetailPage detail = new StudentReportDetailPage(stage, selected);
                    detail.setPrevious(this);
                    stage.setScene(detail.getScene());
                }
            });
            return row;
        });
    }

    public Scene getScene() {
        return scene;
    }

    public void setHomePage(HomePage home) {
        homePage = home;
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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void updateTable() {
        ArrayList<StudentProfile> profiles = db.loadProfiles();
        ArrayList<StudentProfile> filtered = new ArrayList<>();

        RadioButton selectedToggle = (RadioButton) options.getSelectedToggle();
        boolean whiteListed = selectedToggle == wl;

        for (StudentProfile sp : profiles) {
            if ((sp.isWhiteList() && whiteListed) || (!whiteListed && sp.isBlackList())) {
                filtered.add(sp);
            }
        }
        table.getItems().clear();
        table.getItems().addAll(filtered);
    }
}