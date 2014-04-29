Get started with h2o
-----------------------------------
Welcome to Java Web Starter application!

This sample application demonstrates how to write a Java Web application (powered by WebSphere Liberty) and deploy it on BlueMix.

1. [Install the cf command-line tool](https://www.ng.bluemix.net/docs/redirect.jsp?name=cf-instructions).
2. [Download the starter application package](https://ace.ng.bluemix.net:443/rest/../rest/apps/11c61151-8ad6-4675-a8e0-27c37182d1d0/starter-download)
3. Extract the package and `cd` to it.
4. Connect to BlueMix:

		cf api https://api.ng.bluemix.net

5. Log into BlueMix:

		cf login -u ramprakash.rammohan@sjsu.edu
		cf target -o ramprakash.rammohan@sjsu.edu -s dev
				
6. Compile the JAVA code and generate the war package using ant.
7. Deploy your app:

		cf push h2o -p webStarterApp.war

8. Access your app: [http://h2o.ng.bluemix.net](http://h2o.ng.bluemix.net)
