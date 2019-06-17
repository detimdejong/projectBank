/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/



public class Keypad extends HardwareElement implements InputDevice {



    private static String input ="";

    Keypad(String naam) {

        super(naam); //superclass aanroepen

    }

    @Override
    public String getInput() {
        input = null; //clear input
        Serial2.listenSerial(); //listen for input from the arduino
        return input; //return the input from setInput
    }

    public static void setInput(String data){

        input = data; //store the value in the field input
    }

}
