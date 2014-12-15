# TerminalFahrplan
## Idea
The idea is to have a Terminal Application which shows the timeline of a chosen public transport station. It will refresh every ***X*** seconds so you have an always up-to-date view of the outgoing and incoming public transport in your area.  
The service will be limited to switzerland only. Reason for that is the [chosen API](http://transport.opendata.ch "Swiss public transport API").
## Current State
State: **In Progress** (about 80% finished)  
![Main Stationboard View](img/mainView2.png "Stationboard")
## Build Project

Build and run application.

```
./gradlew run
```

Create an executable JAR

```
./gradlew installApp
```
## How to install
```
./gradlew installApp
```
## How to use
### Option 1: Development Purpose
Run without any parameters

```
build/install/TerminalFahrplan/bin/TerminalFahrplan
```	
What it does?  

* Shows Stationboard from offline File (zuerichHB.json)

### Option 2: Stationboard View with auto Update
Run with 1 Parameter (example: Rapperswil)

```
build/install/TerminalFahrplan/bin/TerminalFahrplan Rapperswil
```	
### Option 3: Under development...
		
## Edit project

Generate Eclipse project files:

```
./gradlew eclipse
```
## Known issues
- What happens when loosing connection while looking up station name? ;)

## Used Technology
Coding language: **Java**  
API format:	**JSON**
## Main Goal
Main goal is to create an Android Service Only Application which sends you push notifications when your train/bus/etc. is too late.  
This project is more of a small test to get into working with API's.



