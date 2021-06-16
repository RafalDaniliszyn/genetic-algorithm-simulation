package pl.rafal;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Panel extends JPanel {
    AlgorytmGenetyczny algorytmGenetyczny;
    Random random = new Random();

    public Panel(AlgorytmGenetyczny algorytmGenetyczny) {
        this.algorytmGenetyczny = algorytmGenetyczny;

    }

    int generacja = 0;
    int gen = 0;//zlicza obroty petli paintcomponent i ustawia poruszanie kolejnego genu
    double najlepszaOcena;

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int liczbaOsobnikow = algorytmGenetyczny.populacja.osobniks.length;
        for (int i = 0; i < algorytmGenetyczny.populacja.osobniks.length; i++) {
            if (algorytmGenetyczny.populacja.osobniks[i].zycie == true) {
                g.setColor(algorytmGenetyczny.populacja.osobniks[i].color);
                algorytmGenetyczny.populacja.osobniks[i].wyswietl(g);
                g.setColor(Color.black);
                repaint();
            }else {
                liczbaOsobnikow -=1;
            }
        }
        g.drawString(String.valueOf(liczbaOsobnikow), 20, 600);

//        algorytmGenetyczny.populacja.sciany[0].ruch(0, 0, 0, 350);
//        algorytmGenetyczny.populacja.sciany[1].ruch(0, 0, 0, 350);
//        algorytmGenetyczny.populacja.sciany[2].ruch(0, 0, 0, 350);
//        algorytmGenetyczny.populacja.sciany[3].ruch(0, 0, 0, 350);

        for (int i = 0; i < algorytmGenetyczny.populacja.sciany.length; i++) {
            algorytmGenetyczny.populacja.sciany[i].wyswietl(g);
        }


        g.drawString("generacja: "+ algorytmGenetyczny.generacja + " najlepsza ocena: " + algorytmGenetyczny.najlepszaOcena + "dlugosc genu: "+ algorytmGenetyczny.populacja.osobniks[0].gen.length, 20, 20);

    }
}
