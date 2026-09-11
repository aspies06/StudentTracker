package cs151.application;

import java.text.Collator;
import java.util.ArrayList;

public class StudentProfile implements Comparable<StudentProfile> {
    private String name;
    private String academicStatus;
    private boolean employed;
    private String jobDetails;
    private ArrayList<ProgrammingLanguage> languages;
    private ArrayList<String> databases;
    private String preferredRole;
    private ArrayList<String> comments;
    private boolean whiteList;
    private boolean blackList;

    public StudentProfile(String n, String a, boolean e, String j, ArrayList<ProgrammingLanguage> l,
                          ArrayList<String> d, String p, ArrayList<String> c, boolean w, boolean b) {
        name = n;
        academicStatus = a;
        employed = e;
        jobDetails = j;
        languages = l;
        databases = d;
        preferredRole = p;
        comments = c;
        whiteList = w;
        blackList = b;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAcademicStatus() { return academicStatus; }
    public void setAcademicStatus(String academicStatus) { this.academicStatus = academicStatus; }

    public boolean isEmployed() { return employed; }
    public void setEmployed(boolean employed) { this.employed = employed; }

    public String getJobDetails() { return jobDetails; }
    public void setJobDetails(String jobDetails) { this.jobDetails = jobDetails; }

    public ArrayList<ProgrammingLanguage> getLanguages() { return languages; }
    public void setLanguages(ArrayList<ProgrammingLanguage> languages) { this.languages = languages; }

    public ArrayList<String> getDatabases() { return databases; }
    public void setDatabases(ArrayList<String> databases) { this.databases = databases; }

    public String getPreferredRole() { return preferredRole; }
    public void setPreferredRole(String preferredRole) { this.preferredRole = preferredRole; }

    public ArrayList<String> getComments() { return comments; }
    public void setComments(ArrayList<String> comments) { this.comments = comments; }

    public boolean isWhiteList() { return whiteList; }
    public void setWhiteList(boolean whiteList) { this.whiteList = whiteList; }

    public boolean isBlackList() { return blackList; }
    public void setBlackList(boolean blackList) { this.blackList = blackList; }

    @Override
    public String toString() {
        String langStr = "";
        if (languages != null && !languages.isEmpty()) {
            ArrayList<String> langNames = new ArrayList<>();
            for (ProgrammingLanguage pl : languages) {
                langNames.add(pl.getName());
            }
            langStr = String.join(";", langNames);
        }

        String dbStr = "";
        if (databases != null && !databases.isEmpty()) {
            dbStr = String.join(";", databases);
        }

        String commentStr = "";
        if (comments != null && !comments.isEmpty()) {
            commentStr = String.join(";", comments);
        }

        return name + "," + academicStatus + "," + employed + "," + jobDetails + "," + langStr + "," + dbStr
                + "," + preferredRole + "," + commentStr + "," + whiteList + "," + blackList;
    }

    @Override
    public int compareTo(StudentProfile o) {
        Collator c = Collator.getInstance();
        return c.compare(name, o.name);
    }
}
