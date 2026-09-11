package cs151.application;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseConnector {
    private static final String spFile = "student-profiles.csv";
    private static final String plFile = "programming-languages.csv";
    private static DatabaseConnector db = new DatabaseConnector();

    private DatabaseConnector() { }

    public static DatabaseConnector getDatabaseConnector() {
        return db;
    }

    public ArrayList<String> loadLanguages() {
        boolean isHeader = true;
        ArrayList<String> languages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(plFile))) {
            String line = br.readLine();
            while (line != null) {
                if (!isHeader) {
                    languages.add(line);
                } else {
                    isHeader = false;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(languages);
        return languages;
    }

    public ArrayList<StudentProfile> loadProfiles() {
        boolean isHeader = true;
        ArrayList<StudentProfile> profiles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(spFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] curr = line.split(",", -1);
                String name = curr[0];
                String academicStatus = curr[1];
                boolean employed = Boolean.parseBoolean(curr[2]);
                String jobDetails = curr[3];

                ArrayList<ProgrammingLanguage> languages = new ArrayList<>();
                if (!curr[4].isBlank()) {
                    String[] langs = curr[4].split(";");
                    for (String lang : langs) {
                        languages.add(new ProgrammingLanguage(lang.trim()));
                    }
                }

                ArrayList<String> databases = new ArrayList<>();
                if (!curr[5].isBlank()) {
                    String[] dbs = curr[5].split(";");
                    for (String db : dbs) {
                        databases.add(db.trim());
                    }
                }

                String preferredRole = curr[6];

                ArrayList<String> comments = new ArrayList<>();
                if (!curr[7].isBlank()) {
                    String[] allComments = curr[7].split(";");
                    for (String c : allComments) {
                        comments.add(c.trim());
                    }
                }

                boolean whiteList = Boolean.parseBoolean(curr[8]);
                boolean blackList = Boolean.parseBoolean(curr[9]);
                StudentProfile s = new StudentProfile(name, academicStatus, employed, jobDetails,
                        languages, databases, preferredRole, comments, whiteList, blackList
                );
                profiles.add(s);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(profiles);
        return profiles;
    }

    public void saveLanguage(String language) {
        try (FileWriter fw = new FileWriter(plFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(language);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfile(StudentProfile s) {
        try (BufferedReader br = new BufferedReader(new FileReader(spFile));
             PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(spFile, true)))) {
            String line;
            boolean isDuplicate = false;
            while ((line = br.readLine()) != null) {
                String name = line.split(",")[0];
                if (s.getName().equals(name)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                pw.println(s);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Duplicate Error");
                a.setContentText("No duplicate names allowed!");
                a.show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProfile(StudentProfile s) {
        ArrayList<StudentProfile> profiles = loadProfiles();
        try (PrintWriter pw = new PrintWriter(new BufferedWriter((new FileWriter(spFile))))) {
            pw.println("Name, Academic Status, Employed, Job Details, Programming Languages, Databases, " +
                    "Preferred Role, Comments, Whitelist, Blacklist");
            for (StudentProfile p : profiles) {
                String name = p.getName();
                if (!s.getName().equals(name)) {
                    pw.println(p);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
