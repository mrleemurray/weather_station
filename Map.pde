PImage generateMap(String _URL, float _lon, float _lat, int _zoom, int _width, int _height, String _token) {
  PImage tempImage;
  String url = _URL + str(_lon) + "," + str(_lat) + "," + _zoom + "/" + str(_width) + "x" + str(_height) + "@2x.png?access_token=" + _token;
  tempImage = loadImage(url, "png");
  if (tempImage != null) {
    isMapGenerated = true;
    return tempImage;
  } else {
    println("Error generating map");
    isMapGenerated = false;
    return loadImage("black.png");
  }
}