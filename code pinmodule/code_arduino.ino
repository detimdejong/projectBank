/*
 *
 * All the resources for this project: https://www.hackster.io/Aritro
 * Modified by Tim de jong & Diederik van LInden
 *
 *
 */

#include <Keypad.h>
#include <SPI.h>
#include <MFRC522.h>


#define SS_PIN 10
#define RST_PIN 9
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.
MFRC522::MIFARE_Key key;

char a = ' ';
byte readBackBlock[14];


const byte ROWS = 4;
const byte COLS = 4;


char customKey;

char hexaKeys[ROWS][COLS] = {   //keypad definieren
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};



String enteredPassword = "";

byte rowPins[ROWS] = {3, 2, 8, 0}; //connect to the row pinouts of the keypad
byte colPins[COLS] = {7, 6, 5, 4}; //connect to the column pinouts of the keypad


Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

void setup()
{
  


  Serial.begin(9600);   // Initiate a serial communication
  SPI.begin();      // Initiate  SPI bus
  mfrc522.PCD_Init();   // Initiate MFRC522
 
  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }

  String content = "";

}



void loop()
{
 
  
  
  String content = "";
  byte letter;
  enteredPassword = "";


customKey = customKeypad.getKey();
if(customKey!= NO_KEY){

         
          enteredPassword += customKey;//ingetoetste toets opslaan
              
        Serial.println(enteredPassword); //toets printen
}
        
        
     
     
      if (!mfrc522.PICC_IsNewCardPresent()){
    //Serial.println("geen kaart");
    return;
  } else {
     
     if (!mfrc522.PICC_ReadCardSerial()){
      
    return;
  }

   for (byte index = 0; index < 6; index++) {
      key.keyByte[index] = 0xFF;
    }


    // Read the block
    readBlock(1, readBackBlock);

//   
//       for (byte i = 0; i < mfrc522.uid.size; i++)//leest de tag en print het UID
//     {
//    Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? "0" : " ");
//    Serial.print(mfrc522.uid.uidByte[i], HEX);
//    content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? "0" : " "));
//    content.concat(String(mfrc522.uid.uidByte[i], HEX));
//    }
//    Serial.println();

String content = "";
    for (int index = 0; index < sizeof(readBackBlock); index++) {
      content += char(readBackBlock[index]);
    }
    Serial.println(content);

    // End authentication
    mfrc522.PICC_HaltA();
    mfrc522.PCD_StopCrypto1();
  }
  
  //Serial.println();
  
  content.toUpperCase();
 
     
  }
  void readBlock(int blockNumber, byte arrayAddress[]) {
  authenticateBlockAction(1);

  byte buffersize = 18;

  byte status = mfrc522.MIFARE_Read(blockNumber, arrayAddress, &buffersize); 
  if (status != MFRC522::STATUS_OK) {
   // Serial.println("Read failed");
    return;
  }
}

void authenticateBlockAction(int blockNumber) {
  int largestModulo4Number = blockNumber / 4 * 4;
  int trailerBlock = largestModulo4Number + 3;

  byte status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    //Serial.println("Authentication failed");
    return;
  }
}
