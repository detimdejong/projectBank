/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/

import com.fazecast.jSerialComm.*;


public class Serial2 {

    public static void listenSerial() {

        //usb port voor pinmodule
        SerialPort comPort = SerialPort.getCommPort("/dev/cu.wchusbserial14110");

        //set the baud rate to 9600 (same as the Arduino)
        comPort.setBaudRate(9600);

        //open the port
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        System.out.println("port opened succesfully");

        //create a listener and start listening
        comPort.addDataListener(new SerialPortDataListener() {
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    CardReader.setInput(null);
                    return; //wait until we receive data
                }

                byte[] newData = new byte[comPort.bytesAvailable()]; //receive incoming bytes
                comPort.readBytes(newData, newData.length); //read incoming bytes
                //convert bytes to string
                String serialData = new String(newData);
                serialData = serialData.replaceAll("\\s+",""); //Removes all whitespaces and non-visible characters
                if(serialData.length() == 1){//als input 1 char lang is => getal vanuit keypad
                    Keypad.setInput(serialData);
                }else if(serialData.length() == 14){//als input 14 chars lang is, Iban vanuit cardreader
                    CardReader.setInput(serialData);
                }
            }
        });




    }






}