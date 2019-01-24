void manageReportUpdate(int _interval) {
  // Calculate how much time has passed
  int passedTime = millis() - savedTime;
  // Has five seconds passed?
  if (passedTime > _interval) {
    println("New report available");
    checkForNewObservations(URL);
    savedTime = millis(); // Save the current time to restart the timer!
  }
}

void manageInformationTransitions(int _interval) {
  if (isInformationAvailable) {
    // Calculate how much time has passed
    int passedScreenTime = millis() - savedScreenTime;
    if (passedScreenTime > _interval) {
      infoOne.exit();
      savedScreenTime = millis(); // Save the current time to restart the timer!
    }
  }
}

void updateInfo() {
  infoOne.setName(weatherReport.get(position)[0]);
  infoOne.setValue(weatherReport.get(position)[1]);
  String[] tempMetaData = {" ", "Last updated: " + dateTime}; 
  infoOne.setMetaData(tempMetaData);

  position++;
  if (position >= weatherReport.size()) {
    position = 0;
  }
}