import de.looksgood.ani.*;

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


void setup() {
  //size(1000, 800, P2D);
  fullScreen();
  Ani.init(this);
  weatherReport = new ArrayList<String[]>();
  String[] tempData = {" ", " "};
  infoOne = new Information("NAME", "VALUE", tempData, true);
  checkForNewObservations(URL);
  map = generateMap(mapURL, lon+0.24, lat-0.05, zoom, width/2, height/2, token); 
}

void draw() {
  background(0);
  image(map, 0, 0);
  manageInformationTransitions(totalScreenTime);
  infoOne.drawInformation();
  infoOne.drawMetaData();
  if (isTransitionComplete()) {
    manageReportUpdate(totalTime);
  }
}
