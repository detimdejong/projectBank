/*------------------------------------------------------------------------
  code voor gelddispenser/bonprinter
  ------------------------------------------------------------------------*/



#include "Adafruit_Thermal.h"
#include "adalogo.h"
#include "adaqrcode.h"
//#include <DateTime.h>
//#include <DateTimeStrings.h>
#include <Servo.h>

#define TIME_MSG_LEN  11   // time sync to PC is HEADER and unix time_t as ten ascii digits
#define TIME_HEADER  255   // Header tag for serial time sync message



#include "SoftwareSerial.h"
#define TX_PIN 6 // Arduino transmit  YELLOW WIRE  labeled RX on printer
#define RX_PIN 5 // Arduino receive   GREEN WIRE   labeled TX on printer

SoftwareSerial mySerial(RX_PIN, TX_PIN); // Declare SoftwareSerial obj first
Adafruit_Thermal printer(&mySerial);     // Pass addr to printer constructor


Servo servo1;
Servo servo2;
Servo servo3;



char input;
char input1;
char input2;
char input3;
String biljetten;
int aantalBil;
String bedrag;
String reknum;
boolean printen = false;
int temp = 0;
int pos = 0;

//byte input[256];


void setup() {

  Serial.begin(9600);
  servo1.attach(3);
  servo2.attach(9);
  servo3.attach(10);
//   for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
//      // in steps of 1 degree
//      servo1.write(pos);              // tell servo to go to position in variable 'pos'
//      delay(8);                       // waits 15ms for the servo to reach the position
//    }
//    for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
//      servo1.write(pos);              // tell servo to go to position in variable 'pos'
//      delay(5);                       // waits 15ms for the servo to reach the position
//    }
//     for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
//      // in steps of 1 degree
//      servo2.write(pos);              // tell servo to go to position in variable 'pos'
//      delay(8);                       // waits 15ms for the servo to reach the position
//    }
//    for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
//      servo2.write(pos);              // tell servo to go to position in variable 'pos'
//      delay(5);                       // waits 15ms for the servo to reach the position
//    }
//     for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
//      // in steps of 1 degree
//      servo3.write(pos);              // tell servo to go to position in variable 'pos'
//      delay(8);                       // waits 15ms for the servo to reach the position
//    }
//    for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
//      servo3.write(pos);              // tell servo to go to position in variable 'pos'
//      delay(5);                       // waits 15ms for the servo to reach the position
//    }

}

void loop() {
  bedrag = "";
  reknum = "";
  while (Serial.available() > 0) {   //input uit USB lezen
    input = Serial.read();


    if (input == '$') {            //$ is voor het aangeven dat het over de 10Roebel biljetten gaat

      while (Serial.available() > 0) {
        input1 = Serial.read();

        biljetten += input1;

      }
      aantalBil = biljetten.toInt();
      biljetten = "";
      servo10(aantalBil);
      return;

    }

    if (input == '%') {        //% is voor het aangeven dat het over de 20Roebel biljetten gaat


      while (Serial.available() > 0) {
        input2 = Serial.read();

        biljetten += input2;

      }
      aantalBil = biljetten.toInt();
      biljetten = "";
      servo20(aantalBil);
      return;
    }

    if (input == '&') {        //& is voor het aangeven dat het over de 50Roebel biljetten gaat

      while (Serial.available() > 0) {
        input3 = Serial.read();

        biljetten += input3;

      }
      aantalBil = biljetten.toInt();
      biljetten = "";
      servo50(aantalBil);
      return;
    }

    if (input == '>') {

      while (Serial.available() > 0) {
        input = Serial.read();

        reknum += input;
        if (input == '!') {
          break;
        }
      }
      break;
    }
    bedrag += input;

    printen = true;



    if (input == '!') {
      break;
    }



    printen = true;
  }




  if (printen) {
    print();
  }


  delay(100);
}


void print() {

  mySerial.begin(9600);  // Initialize SoftwareSerial
  printer.begin();        // Init printer (same regardless of serial type)
  printer.write(20);      //lege bon printen
  printer.justify('C');   //justify is plek op de bon Left Center Right
  printer.setSize('S');   // Set type size, accepts 'S', 'M', 'L'
  printer.println("*** LansKrona Bank ***");
  printer.justify('l');
  printer.write(5);
  printer.setSize('S');
  printer.println("Opgenomen bedrag: ");
  printer.setSize('M');
  printer.justify('C');
  printer.println("ROEBL " + bedrag);
  printer.justify('L');
  printer.println("klant " + reknum);
  printer.setSize('S');
  printer.justify('C');
  printer.println("***   Tot ziens   ***");
  printer.println("");
  printer.println("");
  printer.println("");

  printer.write(10);
  printer.sleep();      // Tell printer to sleep
  delay(3000L);         // Sleep for 3 seconds
  printer.wake();       // MUST wake() before printing again, even if reset
  printer.setDefault(); // Restore printer to defaults

  printen = false;
  bedrag = "";
  reknum = "";

}


void servo10(int aantalKeren) {
  temp = aantalKeren;
  for (int i = 0; i < temp; i++) {
    for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
      // in steps of 1 degree
      servo1.write(pos);              // tell servo to go to position in variable 'pos'
      delay(8);                       // waits 15ms for the servo to reach the position
    }
    for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
      servo1.write(pos);              // tell servo to go to position in variable 'pos'
      delay(5);                       // waits 15ms for the servo to reach the position
    }
  }

}

void servo20(int aantalKeren) {
  temp = aantalKeren;
  for (int i = 0; i < temp; i++) {
    for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
      // in steps of 1 degree
      servo2.write(pos);              // tell servo to go to position in variable 'pos'
      delay(8);                       // waits 15ms for the servo to reach the position
    }
    for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
      servo2.write(pos);              // tell servo to go to position in variable 'pos'
      delay(5);                       // waits 15ms for the servo to reach the position
    }
  }



}

void servo50(int aantalKeren) {

  temp = aantalKeren;
  for (int i = 0; i < temp; i++) {
    for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
      // in steps of 1 degree
      servo3.write(pos);              // tell servo to go to position in variable 'pos'
      delay(8);                       // waits 15ms for the servo to reach the position
    }
    for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
      servo3.write(pos);              // tell servo to go to position in variable 'pos'
      delay(5);                       // waits 15ms for the servo to reach the position
    }
  }

}
