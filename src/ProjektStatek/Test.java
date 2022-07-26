package ProjektStatek;

import java.awt.*;
//@Patryk Nakielny

public class Test {

    public static void main(String args[]) throws Exception {
        Zmienne zmienna = new Zmienne();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frame(zmienna);
            }
        });
    }
}