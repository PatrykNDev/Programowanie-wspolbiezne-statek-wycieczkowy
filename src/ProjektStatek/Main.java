package ProjektStatek;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Thread {

    public static Thread[] tabPasazerow;
    public static String[] imiona;
    public static Grafika grafika;
    public Zmienne zm;

    Main(Zmienne zm) {
        super();
        this.zm = zm;
    }

    public void run() {
        int lPasazerow, K, N;
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
        K = zm.K;
        N = zm.N;
        lPasazerow = zm.lPasazerow;
        tabPasazerow = new Thread[lPasazerow];
        imiona = new String[]{"Natalka", "Patryk", "Ewelina", "Adrian", "Marietta", "Julia", "Klaudia", "Pawel", "Basia", "Adas", "Mariusz", "Gosia", "Jerzy",
                "Tomek", "Klaudia", "Krzysztof", "Robert", "Zenon", "Arczi", "Jughead", "Zuzanna", "Lucja", "Edmund", "Piotr", "Krystyna", "Jacek", "Mateusz", "Tata",
                "Benedykt", "Andrzej", "Jaroslaw", "Kaja", "Magda", "Wojciech", "Koala", "Dorota", "Lara", "Leona", "Diana", "Darius", "Draven",
                "Jhin", "Varus", "Gragas", "Yasuo", "Zed", "Chobert", "Franciszek", "Tadeusz", "Cyntia", "Kasandra", "Fabian", "Ireneusz", "Dragomir", "JohnSnow", "Meargery", "Arya",
                "Rob", "Sam", "Fetor", "Zygfryd", "Rita", "Roch", "Karol", "Marcin", "Konrad", "Maggie", "Glen", "Rick", "Carol", "Deryl"};
        grafika = new Grafika();
        Monitor monitor = new Monitor(grafika, lPasazerow, K, N);
        grafika.printText("Liczba wywolanych watkow typu Pasazer: " + lPasazerow, 300, 40);

        for (int i = 0; i < lPasazerow; i++) {
            tabPasazerow[i] = new Pasazer(imiona[i], monitor,  i + 1);
        }
        Thread k1 = new Kapitan("K-1", monitor);

        for (int i = 0; i < lPasazerow; i++) {
            tabPasazerow[i].start();
        }
        k1.start();

        for (int i = 0; i < lPasazerow; i++) {
            try {
                tabPasazerow[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            k1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nKoniec programu");
    }
}
