package ProjektStatek;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    Zmienne zmi;
    public Frame(Zmienne zmi) {
        super("Panel startowy programu Statek, autor: Nakielny Patryk");
        this.zmi=zmi;
        JPanel loginPanel = new StartPanel( zmi, this);
        add(loginPanel);
        setPreferredSize(new Dimension(600, 400));
        loginPanel.setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
