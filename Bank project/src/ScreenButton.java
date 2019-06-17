/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ScreenButton extends ScreenElement implements InputDevice, ActionListener {


    private Button button;
    private boolean inputAvailable = false;

    ScreenButton(String name, Point pos){

        super(name, pos); //superclass aanroepen
        button = new Button(name);
        button.setBounds(pos.x, pos.y, 10 + 15 * name.length(), 25);
        button.addActionListener(this);
    }

    @Override
    public String getInput() {

        if(inputAvailable){
            inputAvailable = false;
            return this.name;
        }
        return null;
    }

    @Override
    void setContainer(Container a) {

        a.add(button);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        inputAvailable = true;
    }
}
