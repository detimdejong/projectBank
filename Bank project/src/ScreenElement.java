/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/

import java.awt.Point;
import java.awt.Container;


public abstract class ScreenElement extends ATMElement{

     private Point pos;

    ScreenElement(String naam, Point position){

        super(naam); //superclass aanroepen

        this.pos  = position; //positie opslaan
    }

    abstract void setContainer( Container a);
}
