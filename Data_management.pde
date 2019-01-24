boolean checkForNewObservations(String _url) {
  try {
    JSONObject json = loadJSONObject(_url);
    JSONObject observations = json.getJSONObject("observations");
    JSONArray observation_data = observations.getJSONArray("data");
    report = observation_data.getJSONObject(0);

    //println(report);

    updateInformation(report);
    isInformationAvailable = true;
  }
  catch (Exception e) {
    println("Error retrieving weather data");
    e.printStackTrace();
    String[] tempMetaData = {"Error", "No Internet Connection"}; 
    infoOne.setMetaData(tempMetaData);
    return false;
  }
  return true;
}

void updateInformation(JSONObject _JSON) {
  weatherReport = new ArrayList<String[]>();
  addWeatherItem("Air Temperature", str(_JSON.getFloat("air_temp"))+"°C");
  addWeatherItem("Feels Like", str(_JSON.getFloat("apparent_t"))+"°C");
  addWeatherItem("Dew Point", str(_JSON.getFloat("dewpt"))+"°C");
  addWeatherItem("Humidity", str(_JSON.getInt("rel_hum"))+"%");
  addWeatherItem("Pressure", str(_JSON.getInt("press"))+"mb");
  addWeatherItem("Wind Direction", _JSON.getString("wind_dir"));
  addWeatherItem("Wind Speed", str(_JSON.getInt("wind_spd_kt"))+" " + checkForS(_JSON.getInt("wind_spd_kt"), "knots"));
  addWeatherItem("Gusts", str(_JSON.getInt("gust_kt"))+" " + checkForS(_JSON.getInt("gust_kt"), "knots"));

  location = _JSON.getString("name").split(" - ")[0];
  dateTime = _JSON.getString("local_date_time").split("/")[1];
  lat = _JSON.getFloat("lat");
  lon = _JSON.getFloat("lon");
}

void addWeatherItem(String _key, String _value) {
  String[] tempData = new String[2];
  tempData[0] = _key;
  tempData[1] = _value;
  weatherReport.add(tempData);
}

String checkForS(int _value, String _unit){
  if(_value == 1){
    return _unit.substring(0, _unit.length()-1);
  }
  return _unit;
}