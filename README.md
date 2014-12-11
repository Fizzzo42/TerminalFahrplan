# TerminalFahrplan
## Idea
The idea is to have a Terminal Application which shows the timeline of a chosen public transport station. It will refresh every ***X*** seconds so you have an always up-to-date view of the outgoing and incoming public transport in your area.  
The service will be limited to switzerland only. Reason for that is the [chosen API](http://transport.opendata.ch "Swiss public transport API").
## Current State
State: **In Progress** (about 80% finished)
## Build Project

Build and run application.

```
./gradlew run
```

Create an executable JAR

```
./gradlew installApp
```
## How to use
* Option 1: Development Purpose - Show Stationboard from offline File (zuerichHB.json)
	* Run without any parameters  
		* ```
		./gradlew installApp
		```	
		* ```
		build/install/TerminalFahrplan/bin/TerminalFahrplan
		```	
* Option 2: Stationboard View with auto Update
	* Run with 1 Parameter
		* ```
		./gradlew installApp
		```	
		* ```
		build/install/TerminalFahrplan/bin/TerminalFahrplan Rapperswil
		```	
* Option 3: Coming soon...
		
## Edit project

Generate Eclipse project files:

```
./gradlew eclipse
```

## How to setup
1. Open a Terminal and locate to the project path
2. Run following Command:  
`javac -cp "java-json.jar" src/TerminalFahrplan/Main.java`
3. ***Coming soon...***

## Known issues
- When loosing connection on entering station, you are prompted to reenter instead of auto connection retry.

## Used Technology
Coding language: **Java**  
API format:	**JSON**
## Main Goal
Main goal is to create an Android Service Only Application which sends you push notifications when your train/bus/etc. is too late.  
This project is more of a small test to get into working with API's.



