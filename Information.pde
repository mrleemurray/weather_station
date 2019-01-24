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

  void setName(String _name) {
    name = _name;
  }

  void setValue(String _value) {
    value = _value;
  }
  
  void setMetaData(String[] _data){
    metaData = _data; 
  }

  void setNamePosition(float _x, float _y) {
  }

  void setValuePosition(float _x, float _y) {
  }

  void setNameOpacity(float _opacity) {
  }

  void setValueOpacity(float _opacity) {
  }

  void setActiveState(boolean _isActive) {
  }

  boolean getActiveState() {
    return isActive;
  }

  void resetPosition() {
    valueYPos = height + valueSize;
  }

  void enter() {
    resetPosition();
    updateInfo(); 
    Ani.to(this, 1.5, "valueYPos", 400, Ani.EXPO_IN_OUT);
    Ani.to(this, 1.0, 0.5, "valueOpacity", 255, Ani.EXPO_IN_OUT);
    Ani.to(this, 1.0, "nameOpacity", 255, Ani.EXPO_IN_OUT);
  }

  void exit() {
    Ani.to(this, 1.5, "valueYPos", -valueSize, Ani.EXPO_IN_OUT, "onEnd:enter");
    Ani.to(this, 1.0, "valueOpacity", 0, Ani.EXPO_IN_OUT);
    Ani.to(this, 1.0, 0.5, "nameOpacity", 0, Ani.EXPO_IN_OUT);
  }

  void drawInformation() {
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

  void drawMetaData() {
    textAlign(RIGHT);
    textFont(metaFont);
    textSize(metaSize);
    fill(metaR, metaG, metaB, metaOpacity);
    text(metaData[0], metaXPos, metaYPos);
    text(metaData[1], metaXPos, metaYPos + metaSize);
  }
}