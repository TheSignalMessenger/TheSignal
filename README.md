h1. Maven
* https://www.eclipse.org/m2e/download/

h1. TheSignal source
* git clone ssh://USERNAME@user.nullteilerfrei.de/~born/repub/TheSignal.git
* Right click in Project Manager
* New, Maven, New Maven project
* Project name: e.g. TheSignal
* Uncheck "Use default location" and give the path of your clone
* "Next" "Finish"
* Right click on Project, "Configure", "Convert to Maven project"
* Right click on Project, "Maven", "Update"

h1. Deployment
* In eclipse
* Right click on project
* Export
* Java -> Runnable JAR file
* select correct launch configuration
* Extract required libraries into generated JAR