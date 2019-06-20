/*------------------------------------------------------------------------
  code voor gelddispenser/bonprinter
  ------------------------------------------------------------------------*/



#include "Adafruit_Thermal.h"
#include "adalogo.h"
#include "adaqrcode.h"
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


String commands[4];
int index = 0;


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

String date;
String tijd;

#define INPUT_SIZE 30

//byte input[256];


void setup() {

  Serial.begin(9600);
  servo1.attach(3);
  servo2.attach(9);
  servo3.attach(10);

  servo1.write(0);
  servo2.write(0);
  servo3.write(0);
}

void loop() {
  bedrag = "";
  reknum = "***********";
  aantalBil = 0;
  date = "";
  tijd = "";

// Get next command from Serial (add 1 for final 0)
  char input[INPUT_SIZE + 1];
  byte size = Serial.readBytes(input, INPUT_SIZE);
// Add the final 0 to end the C string
    input[size] = 0;
// Read each command pair 
    
    char* command = strtok(input, ">");
    String content = String(command);
   // Serial.println(command);
      
  if(content.startsWith("&") > 0 || content.startsWith("%") > 0 || content.startsWith("$") > 0)
  {
    String aantal = content;
    aantal.remove(0, 1);
    
    if(content.startsWith("$") > 0){
      dispenseBill(servo1, aantal.toInt());
    }else if(content.startsWith("%") > 0){
      //command = strtok(0, "%");
      dispenseBill(servo2, aantal.toInt());
    }else if(content.startsWith("&") > 0){
      //command = strtok(0, "%");
      dispenseBill(servo3, aantal.toInt());
    }
  }
  else{
      //Serial.println("bonnetje");
      
      while (command != 0)
      {
        //Serial.print(index);
        //Serial.println(command);
        commands[index] = command;
        index++;
          // Split the command in two values
    
          // Find the next command in input string
          command = strtok(0, ">");
      }
    
      //Serial.println(commands);
      // > + <
    
      if(sizeof(commands) >= 4){

        if(commands[0] != ""){
          bedrag = commands[0];
          reknum += commands[1];
          date = commands[2];
          tijd = commands[3];
          printen = true;
        }

        
      }


      if(printen){
        print();
      }
  }

  

  
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
  printer.println("ROEBEL " + bedrag);
  printer.justify('L');
  printer.println("klant " + reknum);
  printer.setSize('S');
  printer.justify('L');
  printer.println("Datum: " + date);
  printer.println("Tijd: " + tijd);
  printer.justify('C');
  printer.println("***   Tot ziens   ***");
  printer.println("");
  printer.println("");
  //printer.println("");

  printer.write(10);
  printer.sleep();      // Tell printer to sleep
  delay(3000);         // Sleep for 3 seconds
  printer.wake();       // MUST wake() before printing again, even if reset
  printer.setDefault(); // Restore printer to defaults

  printen = false;
  bedrag = "";
  reknum = "";

  for(int i =0; i< 3; i++){
    commands[i] = "";
  }
  index = 0;

}

void dispenseBill(Servo pServo, int pAmount){
  for (int i = 0; i < pAmount; i++) {
    for (pos = 0; pos <= 165; pos += 1) { // goes from 0 degrees to 180 degrees
      // in steps of 1 degree
      pServo.write(pos);              // tell servo to go to position in variable 'pos'
      delay(8);                       // waits 15ms for the servo to reach the position
    }
    for (pos = 165; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
      pServo.write(pos);              // tell servo to go to position in variable 'pos'
      delay(5);                       // waits 15ms for the servo to reach the position
    }
  }
}
