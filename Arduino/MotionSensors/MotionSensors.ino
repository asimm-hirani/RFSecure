int dists1[20];
int dists2[20];
int i = 0;
const String sensorID1 = "Office 1";
const String sensorID2 = "Office 2";
const int distSensor1 = A0;
const int distSensor2 = A2;
boolean pass2;
boolean pass1;


void setup(void) {
  for (int j = 0; j < 20; j++) {
    dists1[j] = 0;
    dists2[j] = 0;
  }
  #ifndef ESP8266
    while (!Serial); // for Leonardo/Micro/Zero
  #endif
  Serial.begin(115200);
}


void loop(void) {
  dists1[i] = analogRead(distSensor1);
  dists2[i] = analogRead(distSensor2);
  i++;
  if (i == 20) {
    i = 0;
  }
  
  int dist1L = 0;
  int dist2L = 0;
  int dist1R = 0;
  int dist2R = 0;
  for (int j = 0; j < 10; j++) {
    dist1L += dists1[j];
    dist2L += dists2[j];
  }
  for (int j = 10; j < 20; j++) {
    dist1R += dists1[j];
    dist2R += dists2[j];
  }
  pass1 = dist1L * 1.5 < dist1R;
  pass2 = dist2L * 1.5 < dist2R;
  Serial.print(sensorID1);
  Serial.print('&');
  Serial.print((int)pass1, DEC);
  Serial.print('&');
  Serial.print(sensorID2);
  Serial.print('&');
  Serial.print((int)pass2, DEC);
  Serial.println(); 
  delay(10);
}

