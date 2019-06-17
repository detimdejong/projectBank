/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;

public class SerialOut {

    //usb port van gelddispenser
    private static SerialPort serialPort = SerialPort.getCommPort("/dev/cu.wchusbserial14120");



    //verbinding openen
    static void open(){

        serialPort.setBaudRate(9600);

        //open the port
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        //even wachten om verbinding tot stand te laten komen
        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        System.out.println("port opened succesfully");


    }

    //funtie om strings te versturen naar arduino, voor uitgeven van geld en bon
    static void sendToArduino(String output) {


        try {

            serialPort.getOutputStream().write(output.getBytes());
            serialPort.getOutputStream().flush();

        } catch (IOException e) {

            e.printStackTrace();
        }


    }












}