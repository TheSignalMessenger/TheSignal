# TheSignal
End-to-end encrypted server-free chat application with group support

## Notes

### Maven

* https://www.eclipse.org/m2e/download/

### TheSignal source

* git clone
* Right click in Project Manager
* New, Maven, New Maven project
* Project name: e.g. TheSignal
* Uncheck "Use default location" and give the path of your clone
* "Next" "Finish"
* Right click on Project, "Configure", "Convert to Maven project"
* Right click on Project, "Maven", "Update"

### Deployment

* In eclipse
* Right click on project
* Export
* Java -> Runnable JAR file
* select correct launch configuration
* Extract required libraries into generated JAR

# Developer Documentation

| DHT-Langauge  | TheSignal Language |
| ------------- |:------------------:|
| location      | receiver (TSGroup) |
| domain        | sender (TSPeer)    |
| entry         | random             |
