package cs151.application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage {
    private Scene scene;
    private ProgrammingLanguagePage pl;
    private StudentProfilePage sp;
    private SearchStudentProfilesPage search;
    private ReportPage report;

    public HomePage(Stage stage) {
        VBox root = new VBox(10);
        Button plButton = new Button("Create a Programming Language");
        Button spButton = new Button("Create a Student Profile");
        Button searchButton = new Button("Search for Student Profiles");
        Button reportButton = new Button("Generate a Report");

        plButton.setOnAction(e -> {
            stage.setScene(pl.getScene());
            stage.setTitle("Programming Languages");
        });

        spButton.setOnAction(e -> {
            sp.updateLangs();
            sp.updateProfiles();
            stage.setScene(sp.getScene());
            stage.setTitle("Student Profiles");
        });

        searchButton.setOnAction(e -> {
            search.updateLangs();
            search.updateTable();
            stage.setScene(search.getScene());
            stage.setTitle("Search Student Profiles");
        });

        reportButton.setOnAction(e -> {
            report.updateTable();
            stage.setScene(report.getScene());
            stage.setTitle("Reports");
        });

        scene = new Scene(root, 1200, 800);
        root.getChildren().addAll(plButton, spButton, searchButton, reportButton);
    }

    public Scene getScene() {
        return scene;
    }

    public void setPlPage(ProgrammingLanguagePage plv) {
        pl = plv;
    }

    public void setSpPage(StudentProfilePage spv) {
        sp = spv;
    }

    public void setSearch(SearchStudentProfilesPage sv) {
        search = sv;
    }

    public void setReport(ReportPage r) {report = r;}
}