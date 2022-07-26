package ProjektStatek;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    final Lock dostep;
    final Condition pustyMostek, mostek, statek, koniecWycieczki, statekPusty;
    public final int K, N;
    int miejsceNaMostkuWolne, miejsceNaStatku, miejsceWPoczekalni;
    Grafika grafika;
    int liczPasaMost, pasaPocz, liczPasaStat, kolejkaNaStatek;
    boolean czyWszyscyWyszliZeStatku, pelnaKolumnaStat, czyCzasOdjezdzac;
    public static int[] zapamietPoloze;

    public Monitor(Grafika graf, int lPasazerow, int pojMostek, int pojStatek) {
        dostep = new ReentrantLock();
        pustyMostek = dostep.newCondition();
        mostek = dostep.newCondition();
        statek = dostep.newCondition();
        koniecWycieczki = dostep.newCondition();
        statekPusty = dostep.newCondition();
        K = pojMostek;
        N = pojStatek;
        liczPasaMost = 0;
        pasaPocz = 0;
        liczPasaStat = 0;
        kolejkaNaStatek = 0;
        this.grafika = graf;
        czyWszyscyWyszliZeStatku = true;
        miejsceNaMostkuWolne = 1;
        miejsceNaStatku = 0;
        miejsceWPoczekalni = 1;
        drawShip();
        drawBridge();
        zapamietPoloze = new int[lPasazerow];
        for (int i = 0; i < lPasazerow; i++) {
            zapamietPoloze[i] = 0;
        }
        pelnaKolumnaStat = false;
        czyCzasOdjezdzac = false;
    }

    public void draw(int id, Color color) {
        int x = 440;
        int y = (miejsceWPoczekalni + 1) * 40 + 10;

        Rectangle2D pasazer2D = new Rectangle2D.Double(x, y, 40, 20);
        grafika.paint(pasazer2D, color);
        grafika.printText("P-" + id, x, y + 12);
        zapamietPoloze[id - 1] = y;
        miejsceWPoczekalni++;
        miejsceWPoczekalni %= (N + 1);
        // Test.grafika.printText("STAN. " + id, x, y + 12);
    }

    public void clear(int x, int y, int w, int h, Color color) {
        Rectangle2D pasazerClear = new Rectangle2D.Double(x, y, w, h);
        grafika.paint(pasazerClear, color);
    }

    public void clear(int x, int y, int w, int h) {
        grafika.clearRect(x, y, w, h);
    }

    public void drawOnBridge(int id) throws InterruptedException {
        int finalX = 220;
        int finalY = zapamietPoloze[id - 1];
        int x = 440;
        int y = miejsceNaMostkuWolne * 70 + 110;
        int bridgeX = 290;
        //ilosc osob na mostku komunikat
        clear(finalX + 10, 42, 40, 32, Color.YELLOW);
        grafika.printText("" + liczPasaMost + "/" + K, finalX + 10, 52);
        while (bridgeX != x) {

            x = x - 2;
            Rectangle2D pasazer2D = new Rectangle2D.Double(x, finalY, 40, 20);
            grafika.paint(pasazer2D, Color.CYAN);
            grafika.printText("P-" + id, x, finalY + 12);
            clear(x, finalY - 10, 50, 40);
        }
        clear(x - 10, finalY - 10, 60, 40);
        Rectangle2D pasazerNaMostku = new Rectangle2D.Double(finalX, y, 40, 20);
        grafika.paint(pasazerNaMostku, Color.CYAN);
        grafika.printText("P-" + id, finalX, y + 12);
        zapamietPoloze[id - 1] = miejsceNaMostkuWolne;
        miejsceNaMostkuWolne++;
        //zachowanie spojnosci grafiki
        miejsceNaMostkuWolne %= 5;
        Thread.sleep(1000);
    }

    public void drawBridge() {
        int x = 200;
        int y = 40;

        Rectangle2D mostek = new Rectangle2D.Double(x, y, 70, 400);
        grafika.paint(mostek, Color.YELLOW);
        grafika.printText("Mostek", x, y + 12);

        // Test.grafika.printText("STAN. " + id, x, y + 12);
    }

    public void drawShip() {
        int x = 20, y = 50;
        Ellipse2D statek = new Ellipse2D.Double(x, y, 170, 400);
        grafika.paint(statek, Color.green);
    }

    public void drawOnShip(int id) {
        int finalX = 120;
        int finalX2 = 70;
        int y = miejsceNaStatku * 70 + 110;

        //drawBridge();
        if (pelnaKolumnaStat) {
            Rectangle2D pasazer2D = new Rectangle2D.Double(finalX2, y, 40, 20);
            grafika.paint(pasazer2D, Color.MAGENTA);
            grafika.printText("P-" + id, finalX2, y + 12);
        } else {
            Rectangle2D pasazer2D = new Rectangle2D.Double(finalX, y, 40, 20);
            grafika.paint(pasazer2D, Color.MAGENTA);
            grafika.printText("P-" + id, finalX, y + 12);
        }
        clear(230, 42, 40, 32, Color.YELLOW);
        grafika.printText("" + liczPasaMost + "/" + K, 230, 52);
        clear(0, 10, 80, 32, Color.GREEN);
        grafika.printText("Statek: " + liczPasaStat + "/" + N, 10, 40);
        miejsceNaStatku++;
        miejsceNaStatku %= 5;
        if (miejsceNaStatku == 0) {
            pelnaKolumnaStat = true;
        }
    }

    public void panelInfo(String str) {
        //clear(280, 440, 300, 100);
        Rectangle2D pasazer2D = new Rectangle2D.Double(20, 450, 300, 100);
        grafika.paint(pasazer2D, Color.cyan);
        grafika.printText(str, 20, 480);
    }

    boolean wejdzNaMostek(int id) {
        dostep.lock();
        boolean czyWejdzieNaStatek = false;
        try {
            if (liczPasaStat + liczPasaMost + pasaPocz + kolejkaNaStatek < N && czyWszyscyWyszliZeStatku && !czyCzasOdjezdzac) {
                if (liczPasaMost == 0)
                    drawBridge();
                draw(id, Color.GREEN);
                panelInfo("Statek czeka na pasazerow");
                if (liczPasaMost == K) {
                    pasaPocz++;
                    mostek.await();
                    pasaPocz--;
                }
                if (!czyCzasOdjezdzac) {
                    liczPasaMost++;
                    drawOnBridge(id);
                    System.out.println(">>> [" + Thread.currentThread().getName() + ", " + pasaPocz + ", "
                            + liczPasaMost + ", " + liczPasaStat + "]");
                    czyWejdzieNaStatek = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dostep.unlock();
        }
        return czyWejdzieNaStatek;
    }

    public void wejdzNaStatek(int id) {
        dostep.lock();
        try {
            //Jezeli nie ma nikogo na statku to mozesz go odswiezyc
            if (liczPasaStat == 0)
                drawShip();
            liczPasaStat++;
            liczPasaMost--;
            //Rysowanie paszera na statku i czyszczenie "starej" pozycji
            clear(210, zapamietPoloze[id - 1] * 70 + 110, 60, 50, Color.YELLOW);
            drawOnShip(id);
            //
            if (liczPasaMost == 0) {
                pustyMostek.signal();
                mostek.signal();
            } else {
                mostek.signal();
            }
            System.out.println("--> [" + Thread.currentThread().getName() + ", " + pasaPocz + ", "
                    + liczPasaMost + ", " + liczPasaStat + "]");
            koniecWycieczki.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dostep.unlock();
        }
    }

    public void zejdzZeStatku(int id) {
        dostep.lock();
        try {
            if (liczPasaMost == K) {
                mostek.await();
            }
            liczPasaStat--;
            System.out.println("<-- [" + Thread.currentThread().getName() + ", " + pasaPocz + ", "
                    + liczPasaMost + ", " + liczPasaStat + "]");
            //Graficzna reprezentacja zejscia ze statku
            clear(110, id * 70 + 20, 70, 50);
            clear(0, 10, 80, 32, Color.GREEN);
            grafika.printText("Statek: " + liczPasaStat + "/" + N, 10, 40);
            //
            if (liczPasaStat == 0) {
                statekPusty.signalAll();
                System.out.println("Wszyscy pasazerowie wyszli ze statku");
                drawShip();
            } else {
                mostek.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dostep.unlock();
        }
    }

    public void odjedzStatkiem() {
        dostep.lock();
        try {
            czyCzasOdjezdzac = true;
            panelInfo("Kapitan sprawdza czy mostek jest pusty");
            if (liczPasaMost > 0) {
                panelInfo("Kapitan czeka na ludzi wchodzacych przez mostek");
                pustyMostek.await();
            }
            panelInfo("Statek odplywa na wycieczke");
            czyWszyscyWyszliZeStatku = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dostep.unlock();
        }
    }

    synchronized void podplynDoPortu() {
        dostep.lock();
        try {
            int czasSpania;
            //clear(280, 400, 160, 100);
            panelInfo("Statek podplynal do portu");
            //grafika.printText("Statek podplynal do portu", 280, 480);
            System.out.println("Statek podplynal do portu");

            koniecWycieczki.signalAll();
            if (liczPasaStat > 0) {
                statekPusty.await();
                System.out.println("Teraz na statek moga probowac wejsc kolejni chetni");
            }
            panelInfo("Wszycy wyszli ze statku");
            czasSpania=1000;
            Thread.sleep(czasSpania);
            czyWszyscyWyszliZeStatku = true;
            pelnaKolumnaStat = false;
            czyCzasOdjezdzac = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dostep.unlock();
        }
    }


}
