package cs151.application;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

public class AddCommentPage {
    private Scene scene;
    private SearchStudentProfilesPage homePage;
    private StudentProfile student;
    private ArrayList<String> comments;

    private TextArea commentField;

    private TableView<String> table;
    private DatabaseConnector db;

    public AddCommentPage(Stage stage, StudentProfile selected) {
        db = DatabaseConnector.getDatabaseConnector();
        table = new TableView<String>();
        student = selected;
        comments = student.getComments();

        Label commentLabel = new Label("Write a new comment here");
        commentField = new TextArea();
        commentField.setPromptText("Enter the comment here");

        Button addBtn = new Button("Add Comment");
        Button homeBtn = new Button("Back Home");
        HBox buttons = new HBox(10, addBtn, homeBtn);

        setUpTable();
        table.setPlaceholder(new Label("No comments yet"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        updateTable();
        VBox root = new VBox(12, commentLabel, commentField, buttons, table);

        addBtn.setOnAction(e -> {
            String comment = commentField.getText();
            commentField.clear();
            if (comment.trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Empty Comment");
                a.setContentText("The comment field is empty!");
                a.show();
                return;
            } else if (comment.contains(",")) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Invalid Comment");
                a.setContentText("Comment can't contain commas");
                a.show();
                return;
            }
            db.deleteProfile(student);
            comment += "|" + LocalDate.now();
            comments.add(comment);
            student.setComments(comments);
            db.saveProfile(student);
            updateTable();
        });

        homeBtn.setOnAction(e -> {
            homePage.updateTable();
            stage.setScene(homePage.getScene());
            stage.setTitle("Search Student Profiles");
        });

        scene = new Scene(root, 1200, 800);
    }

    public Scene getScene() {
        return scene;
    }

    public void setHomePage(SearchStudentProfilesPage home) {
        homePage = home;
    }

    private void setUpTable() {
        table.getColumns().clear();
        TableColumn<String, String> commentColumn = new TableColumn<>("Comments about " + student.getName());
        commentColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue())
        );
        table.getColumns().add(commentColumn);
    }

    private void updateTable() {
        table.getItems().clear();
        table.getItems().addAll(comments);
    }
}
