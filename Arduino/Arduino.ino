/**************************************************************************/
/*! 
    @file     readMifareClassic.pde
    @author   Adafruit Industries
	@license  BSD (see license.txt)

    This example will wait for any ISO14443A card or tag, and
    depending on the size of the UID will attempt to read from it.
   
    If the card has a 4-byte UID it is probably a Mifare
    Classic card, and the following steps are taken:
   
    Reads the 4 byte (32 bit) ID of a MiFare Classic card.
    Since the classic cards have only 32 bit identifiers you can stick
	them in a single variable and use that to compare card ID's as a
	number. This doesn't work for ultralight cards that have longer 7
	byte IDs!
   
    Note that you need the baud rate to be 115200 because we need to
	print out the data and read from the card at the same time!

This is an example sketch for the Adafruit PN532 NFC/RFID breakout boards
This library works with the Adafruit NFC breakout 
  ----> https://www.adafruit.com/products/364
 
Check out the links above for our tutorials and wiring diagrams 
These chips use SPI to communicate, 4 required to interface

Adafruit invests time and resources providing this open source code, 
please support Adafruit and open-source hardware by purchasing 
products from Adafruit!
*/
/**************************************************************************/
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_PN532.h>

// If using the breakout with SPI, define the pins for SPI communication.
#define PN532_SCK  (2)
#define PN532_MOSI (3)
#define PN532_SS   (4)
#define PN532_MISO (5)
#define PN532_SCK2  (6)
#define PN532_MOSI2 (7)
#define PN532_SS2   (8)
#define PN532_MISO2 (9)

// If using the breakout or shield with I2C, define just the pins connected
// to the IRQ and reset lines.  Use the values below (2, 3) for the shield!
#define PN532_IRQ   (2)
#define PN532_RESET (3)  // Not connected by default on the NFC Shield

int dists1[100];
int dists2[100];
int i = 0;
const String sensorID1 = "Office 1";
const String sensorID2 = "Office 2";
const int distSensor1 = A1;
const int distSensor2 = A3;
boolean pass2;         // running average of pitchPot
boolean pass1;

// Uncomment just _one_ line below depending on how your breakout or shield
// is connected to the Arduino:

// Use this line for a breakout with a SPI connection:
Adafruit_PN532 nfc(PN532_SCK, PN532_MISO, PN532_MOSI, PN532_SS);
Adafruit_PN532 nfc2(PN532_SCK2, PN532_MISO2, PN532_MOSI2, PN532_SS2);

// Use this line for a breakout with a hardware SPI connection.  Note that
// the PN532 SCK, MOSI, and MISO pins need to be connected to the Arduino's
// hardware SPI SCK, MOSI, and MISO pins.  On an Arduino Uno these are
// SCK = 13, MOSI = 11, MISO = 12.  The SS line can be any digital IO pin.
//Adafruit_PN532 nfc(PN532_SS);

// Or use this line for a breakout or shield with an I2C connection:
//Adafruit_PN532 nfc(PN532_IRQ, PN532_RESET);

#if defined(ARDUINO_ARCH_SAMD)
// for Zero, output on USB Serial console, remove line below if using programming port to program the Zero!
// also change #define in Adafruit_PN532.cpp library file
   #define Serial SerialUSB
#endif

void setup(void) {
  for (int j = 0; j < 100; j++) {
    dists1[j] = 0;
    dists2[j] = 0;
  }
  #ifndef ESP8266
    while (!Serial); // for Leonardo/Micro/Zero
  #endif
  Serial.begin(115200);

  nfc.begin();
  nfc2.begin();

  uint32_t versiondata = nfc.getFirmwareVersion();
  if (! versiondata) {
    Serial.print("Didn't find PN53x board");
    while (1); // halt
  }
  // Got ok data, print it out!
  //Serial.print("Found chip PN5"); Serial.println((versiondata>>24) & 0xFF, HEX); 
  //Serial.print("Firmware ver. "); Serial.print((versiondata>>16) & 0xFF, DEC); 
  //Serial.print('.'); Serial.println((versiondata>>8) & 0xFF, DEC);
  
  // configure board to read RFID tags
  nfc.SAMConfig();
  nfc2.SAMConfig();
  
  //Serial.println("Waiting for an ISO14443A Card ...");
}


void loop(void) {
  dists1[i] = analogRead(distSensor1);
  dists2[i] = analogRead(distSensor2);
  i++;
  if (i == 100) {
    i = 0;
  }
  
  int dist1L = 0;
  int dist2L = 0;
  int dist1R = 0;
  int dist2R = 0;
  for (int j = 0; j < 50; j++) {
    dist1L += dist1L[j];
    dist2L += dist2L[j];
  }
  for (int j = 50; j < 100; j++) {
    dist1R += dist1R[j];
    dist2R += dist2R[j];
  }
  pass1 = dist1L * 5 < dist1R;
  pass2 = dist2L * 5 < dist2R;
  Serial.print("%d&%d&", pass1, pass2);

  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };  // Buffer to store the returned UID
  uint8_t uidLength;                        // Length of the UID (4 or 7 bytes depending on ISO14443A card type)
  uint8_t success2;
  uint8_t uid2[] = { 0, 0, 0, 0, 0, 0, 0 };  // Buffer to store the returned UID
  uint8_t uidLength2;                        // Length of the UID (4 or 7 bytes depending on ISO14443A card type)
    
  // Wait for an ISO14443A type cards (Mifare, etc.).  When one is found
  // 'uid' will be populated with the UID, and uidLength will indicate
  // if the uid is 4 bytes (Mifare Classic) or 7 bytes (Mifare Ultralight)
  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);
  success2 = nfc2.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);
  
  if (success) {
    // Display some basic information about the card
    //Serial.println("Found an ISO14443A card");
    //Serial.print("  UID Length: ");Serial.print(uidLength, DEC);Serial.println(" bytes");
    //Serial.print("  UID Value: ");
    nfc.PrintHex(uid, uidLength);
    
    if (uidLength == 4)
    {
      // We probably have a Mifare Classic card ... 
      uint32_t cardid = uid[0];
      cardid <<= 8;
      cardid |= uid[1];
      cardid <<= 8;
      cardid |= uid[2];  
      cardid <<= 8;
      cardid |= uid[3]; 
      //Serial.print("Seems to be a Mifare Classic card #");
      Serial.print("%s&", cardid);
    }
    
    if (success2) {
    // Display some basic information about the card
    nfc2.PrintHex(uid, uidLength);
    
    if (uidLength2 == 4){
      // We probably have a Mifare Classic card ... 
      uint32_t cardid = uid2[0];
      cardid <<= 8;
      cardid |= uid2[1];
      cardid <<= 8;
      cardid |= uid2[2];  
      cardid <<= 8;
      cardid |= uid2[3]; 
      Serial.print("%s", cardid);
    }
    Serial.println("");
    }
  }
}

