import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.looksgood.ani.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Weather_station extends PApplet {



//variables for map data
String mapURL = "https://api.mapbox.com/v4/mapbox.emerald/";
float lat, lon;
int zoom = 10;
String token = "pk.eyJ1IjoibXJsZWVtdXJyYXkiLCJhIjoiY2ptbDZiY3E2MDUwNDNrcDVlNDN1c2c1aiJ9.8vcpEa5rS2_mStuc8_Z6jA";
PImage map;

//variables for weather data
String URL = "http://www.bom.gov.au/fwo/IDN60801/IDN60801.95767.json";
JSONObject report;

//constant information
String location;
String dateTime;

//rotating information
String airTemperature;
String pressure;
String relativeHumidity;

ArrayList<String[]> weatherReport;
int position;

//variables for timer to update the weather report (new report every 30 minutes)
int savedTime;
int totalTime = (1000 * 60) * 30; //30 minutes

//variables for timer to update on screen information (every 5 seconds?)
int savedScreenTime;
int totalScreenTime = 1000 * 10; //5 seconds

boolean isInformationAvailable = false;
Information infoOne;


public void setup() {
  //size(1000, 800, P2D);
  
  Ani.init(this);
  weatherReport = new ArrayList<String[]>();
  String[] tempData = {" ", " "};
  infoOne = new Information("NAME", "VALUE", tempData, true);
  checkForNewObservations(URL);
  map = generateMap(mapURL, lon+0.24f, lat-0.05f, zoom, width/2, height/2, token); 
}

public void draw() {
  background(0);
  image(map, 0, 0);
  manageInformationTransitions(totalScreenTime);
  infoOne.drawInformation();
  infoOne.drawMetaData();
  if (isTransitionComplete()) {
    manageReportUpdate(totalTime);
  }
}

public void checkForNewObservations(String _url) {
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
  }
}

public void updateInformation(JSONObject _JSON) {
  weatherReport = new ArrayList<String[]>();
  addWeatherItem("Air Temperature", str(_JSON.getFloat("air_temp"))+"\u00b0C");
  addWeatherItem("Feels Like", str(_JSON.getFloat("apparent_t"))+"\u00b0C");
  addWeatherItem("Dew Point", str(_JSON.getFloat("dewpt"))+"\u00b0C");
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

public void addWeatherItem(String _key, String _value) {
  String[] tempData = new String[2];
  tempData[0] = _key;
  tempData[1] = _value;
  weatherReport.add(tempData);
}

public String checkForS(int _value, String _unit){
  if(_value == 1){
    return _unit.substring(0, _unit.length()-1);
  }
  return _unit;
}

class Information {
  PFont nameFont, valueFont, metaFont;
  String name, value;
  String[] metaData;
  float nameR, nameG, nameB;
  float valueR, valueG, valueB;
  float metaR, metaG, metaB;
  float nameOpacity, valueOpacity, metaOpacity;
  float nameSize, valueSize, metaSize;
  float nameXPos, nameYPos;
  float valueXPos, valueYPos;
  float metaXPos, metaYPos;
  boolean isActive;

  Information(String _name, String _value, String[] _metaData, boolean _isActive) {
    nameFont = loadFont("Gotham-Book-50.vlw");
    valueFont = loadFont("Gotham-Medium-150.vlw");
    metaFont = loadFont("Gotham-Medium-24.vlw");

    name = _name;
    value = _value;
    metaData = _metaData;
    isActive = _isActive;

    nameR = 0;
    nameG = 61;
    nameB = 102;
    nameOpacity = 255;

    valueR = 255;
    valueG = 255;
    valueB = 255;
    valueOpacity = 255;
    
    metaR = 255;
    metaG = 255;
    metaB = 255;
    metaOpacity = 255;

    nameSize = 50;
    valueSize = 150;
    metaSize = 24;

    nameXPos = width - 50;
    nameYPos = 450;
    valueXPos = width - 50;
    valueYPos = 400;
    metaXPos = width - 50;
    metaYPos = height - 50;
  }

  public void setName(String _name) {
    name = _name;
  }

  public void setValue(String _value) {
    value = _value;
  }
  
  public void setMetaData(String[] _data){
    metaData = _data; 
  }

  public void setNamePosition(float _x, float _y) {
  }

  public void setValuePosition(float _x, float _y) {
  }

  public void setNameOpacity(float _opacity) {
  }

  public void setValueOpacity(float _opacity) {
  }

  public void setActiveState(boolean _isActive) {
  }

  public boolean getActiveState() {
    return isActive;
  }

  public void resetPosition() {
    valueYPos = height + valueSize;
  }

  public void enter() {
    resetPosition();
    updateInfo(); 
    Ani.to(this, 1.5f, "valueYPos", 400, Ani.EXPO_IN_OUT);
    Ani.to(this, 1.0f, 0.5f, "valueOpacity", 255, Ani.EXPO_IN_OUT);
    Ani.to(this, 1.0f, "nameOpacity", 255, Ani.EXPO_IN_OUT);
  }

  public void exit() {
    Ani.to(this, 1.5f, "valueYPos", -valueSize, Ani.EXPO_IN_OUT, "onEnd:enter");
    Ani.to(this, 1.0f, "valueOpacity", 0, Ani.EXPO_IN_OUT);
    Ani.to(this, 1.0f, 0.5f, "nameOpacity", 0, Ani.EXPO_IN_OUT);
  }

  public void drawInformation() {
    textAlign(RIGHT);
    textFont(nameFont);
    textSize(nameSize);
    fill(nameR, nameG, nameB, nameOpacity);
    text(name, nameXPos, nameYPos);

    textFont(valueFont);
    textSize(valueSize);
    fill(valueR, valueG, valueB, valueOpacity);
    text(value, valueXPos, valueYPos);
  }

  public void drawMetaData() {
    textAlign(RIGHT);
    textFont(metaFont);
    textSize(metaSize);
    fill(metaR, metaG, metaB, metaOpacity);
    text(metaData[0], metaXPos, metaYPos);
    text(metaData[1], metaXPos, metaYPos + metaSize);
  }
}

public PImage generateMap(String _URL, float _lon, float _lat, int _zoom, int _width, int _height, String _token) {
  PImage tempImage;
  String url = _URL + str(_lon) + "," + str(_lat) + "," + _zoom + "/" + str(_width) + "x" + str(_height) + "@2x.png?access_token=" + _token;
  tempImage = loadImage(url, "png");
  if (tempImage != null) {
    return tempImage;
  } else {
    println("Error generating map");
    return loadImage("black.png");
  }
}

public void manageReportUpdate(int _interval) {
  // Calculate how much time has passed
  int passedTime = millis() - savedTime;
  // Has five seconds passed?
  if (passedTime > _interval) {
    println("New report available");
    checkForNewObservations(URL);
    savedTime = millis(); // Save the current time to restart the timer!
  }
}

public void manageInformationTransitions(int _interval) {
  if (isInformationAvailable) {
    // Calculate how much time has passed
    int passedScreenTime = millis() - savedScreenTime;
    if (passedScreenTime > _interval) {
      infoOne.exit();
      savedScreenTime = millis(); // Save the current time to restart the timer!
    }
  }
}

public void updateInfo() {
  infoOne.setName(weatherReport.get(position)[0]);
  infoOne.setValue(weatherReport.get(position)[1]);
  String[] tempMetaData = {" ", "Last updated: " + dateTime}; 
  infoOne.setMetaData(tempMetaData);

  position++;
  if (position >= weatherReport.size()) {
    position = 0;
  }
}
public boolean isTransitionComplete(){
  return true;
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "Weather_station" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
