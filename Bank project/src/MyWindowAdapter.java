/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindowAdapter extends WindowAdapter {
    private Frame f;
    MyWindowAdapter(Frame f) {
        this.f = f;
    }
    public void windowClosing(WindowEvent e) {
        f.dispose();
        System.exit(0);
    }
}