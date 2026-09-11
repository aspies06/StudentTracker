package cs151.application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class StudentReportDetailPage {

    private Scene scene;
    private Stage stage;
    private StudentProfile student;
    private TableView<CommentEntry> commentsTable;
    private ReportPage previousPage;


    public static class CommentEntry {
        private final String date;
        private final String excerpt;
        private final String full;


        public CommentEntry(String date, String excerpt, String full) {
            this.date = date;
            this.excerpt = excerpt;
            this.full = full;
        }

        public String getDate() { return date; }
        public String getExcerpt() { return excerpt; }
        public String getFull() { return full;}
    }

    public StudentReportDetailPage(Stage stage, StudentProfile student) {
        this.stage = stage;
        this.student = student;

        VBox root = new VBox(12);
        root.setPadding(new Insets(15));

        Label title = new Label("Student Report: " + student.getName());

        Label info = new Label(
                "Academic Status: " + student.getAcademicStatus() + "\n" +
                        "Employed: " + student.isEmployed() + "\n" +
                        "Preferred Role: " + student.getPreferredRole() + "\n" +
                        "Languages: " + student.getLanguages() + "\n" +
                        "Databases: " + student.getDatabases()
        );

        commentsTable = new TableView<>();
        TableColumn<CommentEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<CommentEntry, String> commentCol = new TableColumn<>("Comment Excerpt");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("excerpt"));

        commentsTable.getColumns().addAll(dateCol, commentCol);
        commentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        commentsTable.setPrefHeight(300);

        loadComments();

        commentsTable.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    CommentEntry entry = commentsTable.getSelectionModel().getSelectedItem();
                    if(entry == null){
                        return;
                    }
                    showFullCommentWindow(entry.getFull());
                }

            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(previousPage.getScene()));

        root.getChildren().addAll(title, info, commentsTable, backBtn);
        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }

    private void loadComments() {
        ArrayList<String> rawComments = student.getComments();
        List<CommentEntry> entries = new ArrayList<>();
        if(rawComments != null){
            for(String c : rawComments){
                String text = c.trim();
                String date =  "";

                int pipeIndex = text.lastIndexOf("|");
                if (pipeIndex != -1 && pipeIndex < text.length() - 1) {
                    date = text.substring(pipeIndex + 1).trim();
                    text = text.substring(0, pipeIndex).trim();
                }
                String excerpt = preview(text);
                entries.add(new CommentEntry(date, excerpt, text));
            }
        }
        if(entries.isEmpty()){
            commentsTable.setPlaceholder(new Label("No comments found"));
        }
        commentsTable.getItems().setAll(entries);
    }

    private String preview(String comment) {
        return comment.length() > 60 ? comment.substring(0, 57) + "..." : comment;
    }


    private void showFullCommentWindow(String commentText){
        final Stage popup = new Stage();
        popup.setTitle("Full Comment");

        TextArea area = new TextArea(commentText);
        area.setWrapText(true);
        area.setEditable(false);

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.close();
            }
        });

        VBox root = new VBox(10, area, closeBtn);
        root.setPadding(new Insets(10));

        Scene popupScene = new Scene(root, 1200, 800);
        popup.setScene(popupScene);
        popup.initOwner(this.stage);

        popup.show();
    }
    public void setPrevious(ReportPage reportPage){
        this.previousPage = reportPage;
    }

}