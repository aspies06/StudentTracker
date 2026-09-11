# Name of application: Students' Knowledgebase for Faculties
# Version: 0.9

# who did what:
1. Aidan Spies: Created ReportPage with RadioButtons for whitelist/blacklist, table, and buttons to generate a report or
go back to home, moved buttons to be above the tables for all pages, created logic for generating report
2. Madhav Thankasala: Created the StudentReportDetailPage and added feature to open a new page to show student details
and comments when double clicking on a user in the report page.
3. Ayush Mithbawkar: Added a feature to open a popup to show the full content of a comment when clicking on a comment
in the student report detail page.

# Version: 0.8

# who did what:
1. Aidan Spies: Created the AddCommentPage and added all the inputs/logic for it, put checkboxes and buttons in an HBox
for the edit page, fixed a bug where editing a profile would prevent the back home button from working in the edit
page, made sure profiles were updated in the student page and search page whenever a profile was edited, created a
method for showing alerts when no profile was selected in the search page, updated title of the change when the user
visited the edit or add comment page and when they came back to the search page
2. Madhav Thankasala: fixed inputs for langauges and databases in the edit page
3. Ayush Mithbawkar:


# Version: 0.7

# who did what:
1. Aidan Spies: Moved all methods related to accessing/editing content in csv files to a singleton class, created edit
   button for SearchStudentProfilesPage and allowed user to select a student profile to edit, changed local variables to
   instance variables and initialized them in the constructor for all classes, moved database responsibilities out of
   HomePage, added comment area for edit page, stored edited profile and removed original profile
2. Madhav Thankasala: Created EditStudentProfilesPage, created inputs for editing a student profile, added functionality
   to the edit button, handled logic for edit page
3. Ayush Mithbawkar: Resized edit page window, added hard coded array for roles


# Version: 0.6

# who did what:
1. Aidan Spies: Allowed user to delete student profiles, redesigned the UI of the SearchStudentPage, added hard-coded
   arrays for preferred roles, databases, and academic status, fixed error where list of languages wouldn't update when
   adding a new student profile with a unique language
2. Madhav Thankasala: Added alerts to check inputs
3. Ayush Mithbawkar: Created SearchStudentProfilesPage class with input fields and search logic


# Version: 0.5

# who did what:
1. Aidan Spies: Wrote the code for home page and programming language page, finished StudentProfile class, created
ViewTables and inputs for languages and profiles, stored profiles in csv file, separated the pages into different java
files, added objective, references, problem statement, scope, functional & non functional requirements for the func
spec, created data model and class diagram for tech spec, cleaned up both documents
2. Madhav Thankasala: Stored programming language, created programming-languages.csv, helped work on inputs for
student profiles, created use case for func spec, created sequence diagram for tech spec
3. Ayush Mithbawkar: Stored programming language, cleaned up files, helped create StudentProfile class and inputs for
name, created mockup ui for func spec, helped with sequence diagram and class diagram for tech spec


# Technical-Spec

# who did what:
1. Aidan Spies: created data model and class diagram, cleaned up the doc
2. Madhav Thankasala: created sequence diagram
3. Ayush Mithbawkar: helped with sequence diagram and class diagram for tech spec


# Functional-Spec

# who did what:
1. Aidan Spies: separated the pages into different java files, added objective, references, problem statement, scope,
functional & non functional requirements, cleaned up the doc
2. Madhav Thankasala: created use case
3. Ayush Mithbawkar: created mockup ui
