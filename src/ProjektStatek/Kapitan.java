package ProjektStatek;

import java.util.Random;

public class Kapitan extends Thread {
    Monitor monitor;
    Random generator;

    public Kapitan(String num, Monitor monitor) {
        super(num);
        generator = new Random();
        this.monitor = monitor;
    }

    public void run() {
        int czasSpania;

        while (true) {
            czasSpania = 5300 + generator.nextInt(4000);
            try {
                sleep(czasSpania);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.odjedzStatkiem();
            System.out.println("\nStatek ruszyl na wycieczke");
            czasSpania = 1300 + generator.nextInt(100);
            try {
                sleep(czasSpania);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.podplynDoPortu();
        }
    }
}
