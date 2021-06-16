package pl.rafal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();

        AlgorytmGenetyczny algorytmGenetyczny = new AlgorytmGenetyczny(30, 5,3 , 5, 500,0.002);//stopien selekcji jest stosunkiem do liczby populacji(nie jest wyrazony w %)


        Panel panel = new Panel(algorytmGenetyczny);
        frame.add(panel);
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread watekPopulacji = new Thread(algorytmGenetyczny.populacja);
        watekPopulacji.start();


        Thread watekZmianyKierunku = new Thread(algorytmGenetyczny);
        watekZmianyKierunku.start();

    }
}
