# atbank

Prerequisite :-
Java 1.8
Maven (Any version)
MySql installed
atbank database created (username = root, password= password)

Steps to run the server :- 
Clone the project from https://github.com/nayankakati/atbank
Go to the folder atbank
“mvn clean install”  to run test cases.
“mvn package”
This would create a JAR in “<$PROJECT_PATH>/atbank/atbank-0.0.1-SNAPSHOT.war
Run the jar “java -jar atbank-0.0.1-SNAPSHOT.war”
It shall run the DB migration with all the tables created.
This should run the server on http://localhost:8080

Steps to use the application :-

After the server is up go to http://localhost:8080 :-
	A login page would be displayed with an option to sign up as well.
 	
2)   If you have not signed up then click on Sign Up  button. This would redirect you to sign up page
3) Sign up with your username and password

4) Now login into your account from login page

5) This would redirect you to the dashboard for account detai


  
