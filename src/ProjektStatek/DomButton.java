package ProjektStatek;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

class DomButton extends JButton implements ActionListener {

    Zmienne zmi;
    private StartPanel startPanel;
    private final JFrame frame;
    DomButton(StartPanel startPanel, Zmienne zmi, JFrame frame) {
        super("Domyslne ustawienia");
        this.zmi=zmi;
        this.frame=frame;
        addActionListener(this);
        this.startPanel = startPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e2) {
        FileReader reader = null;
        try {
            reader = new FileReader("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties p = new Properties();
        try {
            p.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        zmi.lPasazerow=Integer.parseInt(p.getProperty("lPasazerow"));
        System.out.println("Zmienna: "+zmi.lPasazerow);
        zmi.K=Integer.parseInt(p.getProperty("pojMostek"));
        zmi.N=Integer.parseInt(p.getProperty("pojStatek"));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.getContentPane().removeAll();
                Main main = new Main(zmi);
                main.start();
                try {
                    main.join();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
    }
}

