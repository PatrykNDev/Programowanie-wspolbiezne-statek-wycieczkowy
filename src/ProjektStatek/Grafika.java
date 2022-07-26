package ProjektStatek;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.locks.ReentrantLock;

public class Grafika {
    private final ReentrantLock lock = new ReentrantLock(true);
    private JFrame frame;
    private Graphics2D g2;

    public Grafika() {
        //Create and set up the window.
        frame = new JFrame("Programowanie współbieżne Projekt Statek");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(640, 480));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        emptyLabel.setBackground(Color.WHITE);
        //Display the window.
        frame.pack();
        frame.setVisible(true);

        g2 = (Graphics2D) frame.getGraphics();
        g2.setPaint(Color.red);
        g2.setStroke(new BasicStroke(8));
        Rectangle2D ekran = new Rectangle2D.Double(0, 0, 640, 480);
        paint(ekran, Color.WHITE);
    }

    public void paint(Shape _shape, Color _clr) {
        lock.lock();
        try {
            g2.setPaint(_clr);
            g2.fill(_shape);
            g2.draw(_shape);
            g2.setPaint(Color.red);
        } finally {
            lock.unlock();
        }
    }

    public void paint(Shape _shape) {
        lock.lock();
        try {
            g2.fill(_shape);
            g2.draw(_shape);
        } finally {
            lock.unlock();
        }
    }

    public void printText(String str, int x, int y) {
        lock.lock();
        try {
            g2.setColor(Color.black);

            g2.drawString(str, x, y);

            g2.setColor(Color.red);
        } finally {
            lock.unlock();
        }
    }

    public void clearRect(int x, int y, int w, int h) {
        lock.lock();
        try {
            g2.clearRect(x, y, w, h);
            Rectangle2D whiteCls = new Rectangle2D.Double(x, y+3, 80, 40);
            paint(whiteCls, Color.WHITE);
        } finally {
            lock.unlock();
        }
    }

}
