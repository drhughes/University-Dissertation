#Timetable Application
###The data CD contains all of the source code written by the author.

Example_Timetable_Printout.pdf is an example of the printout that the timetable application generates for a student ID

Dissertation contains electronic copies of the report as docx and pdf file formats

Java_Source_Code
Contains 6 java files, these are the files that build the Java Applet.

CFusion_Server 
Contains a folder called FinalYrProject. This folder contains all of the files and folders handled by the ColdFusion Server. The list below details them:

Folder _mmServerScripts contains files that the server uses. 
Folder Halesowen College - Intranet_files contains images that are displayed on the Intranet page. 

Application.cfm is a file that stores global settings for the FinalYrProject root of the server.
clear_sessions.cfm deletes SESSION variables staff and student when run.
GenerateData.cfm holds the code that produces an XML document

These next three are the same page intranet page but they set different SESSION variables to simulate users logging in.
Halesowen College - Intranet_ben07071174.cfm
Halesowen College - Intranet_dee07068776.cfm 
Halesowen College � Intranet_staff.cfm
IntranetIndex.cfm is a list of links to the above three pages to aid the user. 
infoBanner.jpg, script.js, timetableStyle.css are all pages that the web pages access. 
timetable.jar is a JAR file containing all source relating to the Java Applet.
Timetable_App.cfm � this is the page that the Java Applet is embedded within.
Timetable_Search.cfm � this is the staff search facility page.

SQL_Server_2005
Contains a detailed database schema spreadsheet, and a folder Stored_Procedures that contains text files showing the stored procedures that should exist in the SQL server for data gathering. 

To run the code, ColdFusion MX and SQL Server 2005 must first be installed. 

The FinalYrProject folder within the CFusion_Server folder must be copied to the wwwroot folder of the server directory. 

In the ColdFusion administrator two data sources must be created linking the ColdFusion server to the SQL Server databases. 
The Java Applet also needs to be added so it can be referred to by name in the ColdFusion code. 

The SQL Server 2005 databases must be created with the readers own data following the database schema supplied in the folder SQL_Server_2005. This is due to Halesowen College not allowing the data to be passed on due to data protection laws. 

The stored procedures provided as text files must be created in the stored procedures folder of the offline_celcat database. 
A new user role must be created called FYP with SELECT only access to the two databases. 

The Intranet pages can then be opened and the timetable link clicked.
