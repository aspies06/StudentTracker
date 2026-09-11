package cs151.application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        HomePage home = new HomePage(stage);
        ProgrammingLanguagePage pl = new ProgrammingLanguagePage(stage);
        StudentProfilePage sp = new StudentProfilePage(stage);
        SearchStudentProfilesPage search = new SearchStudentProfilesPage(stage);
        ReportPage report = new ReportPage(stage);

        home.setPlPage(pl);
        home.setSpPage(sp);
        home.setSearch(search);
        home.setReport(report);
        pl.setHomePage(home);
        sp.setHomePage(home);
        search.setHomePage(home);
        report.setHomePage(home);

        stage.setTitle("Home Page");
        stage.setScene(home.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}