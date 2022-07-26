package ProjektStatek;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    private JTextField lWatkowField, statekField, mostekField;
    private final JFrame frame;
    private Zmienne zmi;

    public String getLWatkow() {
        return lWatkowField.getText();
    }

    public String getPojStatku() {
        return statekField.getText();
    }

    public String getPassword() {

        return mostekField.getText();
    }

    public StartPanel(Zmienne zmi, JFrame frame) {
        super();
        this.zmi = zmi;
        // ustawianie
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        gridBag.setConstraints(this, constraints);
        setLayout(gridBag);
        this.frame = frame;
        createComponents();
    }

    private void createComponents() {
        JLabel name = new JLabel("Liczba watkow typu Pasazer do utworzenia: ");
        JLabel ship = new JLabel("Pojemnosc statku: ");
        JLabel bridge = new JLabel("Maksymalna pojemnosc mostku: ");
        lWatkowField = new JTextField();
        statekField = new JTextField();
        mostekField = new JTextField();
        DomButton domButton = new DomButton(this, this.zmi, this.frame);
        ZatButton zatButton = new ZatButton(this, this.zmi, this.frame);

        //tworzenie panelu do wprowadzenia danych
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(name);
        inputPanel.add(lWatkowField);
        inputPanel.add(ship);
        inputPanel.add(statekField);
        inputPanel.add(bridge);
        inputPanel.add(mostekField);
        inputPanel.add(domButton);
        inputPanel.add(zatButton);

        // panel do wyśrodkowania elementów
        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(inputPanel, BorderLayout.CENTER);
        parentPanel.setBackground(Color.WHITE);
        this.add(parentPanel);
    }
}

class ZatButton extends JButton implements ActionListener {

    Zmienne zmi;
    private StartPanel startPanel;
    private final JFrame frame;

    ZatButton(StartPanel startPanel, Zmienne zmi, JFrame frame) {
        super("Zatwierdz");
        this.zmi = zmi;
        this.frame = frame;
        addActionListener(this);
        this.startPanel = startPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        zmi.lPasazerow = Integer.parseInt(startPanel.getLWatkow());
        zmi.K = Integer.parseInt(startPanel.getPassword());
        zmi.N = Integer.parseInt(startPanel.getPojStatku());
        System.out.println("Zmienna: " + zmi.lPasazerow);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.getContentPane().removeAll();
                frame.setVisible(false);
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

