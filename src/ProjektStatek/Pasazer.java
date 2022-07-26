package ProjektStatek;

import java.util.Random;

public class Pasazer extends Thread{
    Monitor monitor;
    int id,x;
    Random generator;

    public Pasazer(String num, Monitor monitor,int id){
        super(num);
        generator = new Random();
        this.monitor=monitor;
        this.id=id;
        this.x=0;
    }

    public void run(){
        int czasSpania;

        while(true){
            czasSpania= 5 + generator.nextInt(11);

            try {
                sleep(czasSpania);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(!monitor.wejdzNaMostek(id)) {
                czasSpania= 10 + generator.nextInt(22);
                try {
                    sleep(czasSpania);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            czasSpania= 1300 + generator.nextInt(500);
            try {
                sleep(czasSpania);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.wejdzNaStatek(id);
            monitor.zejdzZeStatku(id);
        }
    }
}
