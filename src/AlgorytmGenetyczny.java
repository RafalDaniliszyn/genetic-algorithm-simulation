package pl.rafal;

import java.awt.*;
import java.util.Random;

public class AlgorytmGenetyczny implements Runnable{

    Populacja populacja;
    Populacja nowaPopulacja;

    int generacja;
    double najlepszaOcena;

    Random random = new Random();

    int liczbaPopulacji;
    int stopienSelekcji;
    int pBmutacji;
    int noweKroki; //ilosc dodawanych krokow(genow) co ileś generacji lub po osiagnieciu celu
    int kiedyDodacKroki = 10; // zlicza w ktorej kolejnej generacji dodac kroki
    int kiedyDodawacKroki = 10; //zmienna do sumowania juz dodanych krokow
    int czestotliwoscZakretow; //czym wyzsza tym rzadziej


    int ileDodano = 0;

    boolean koniecGeneracji = false;

    public AlgorytmGenetyczny(int liczbaPopulacji, int dlugoscGenu, int stopienSelekcji, int pBmutacji, int czestotliwoscZakretow, double predkosc) {
        this.czestotliwoscZakretow = czestotliwoscZakretow;
        this.pBmutacji = pBmutacji;
        this.stopienSelekcji = stopienSelekcji;
        this.liczbaPopulacji = liczbaPopulacji;
        this.noweKroki = dlugoscGenu;

        populacja = new Populacja(liczbaPopulacji, dlugoscGenu, predkosc);
        nowaPopulacja = new Populacja(liczbaPopulacji, dlugoscGenu, predkosc);
    }




    //selekcja
    public void selekcja(){
        for (int i = 0; i < stopienSelekcji; i++) {
            double najwyzsza = populacja.najlepszy();
            for (int j = 0; j < liczbaPopulacji; j++) {
                if (populacja.osobniks[j].ocena == najwyzsza){
                    for (int k = 0; k < populacja.osobniks[0].gen.length; k++) {
                        nowaPopulacja.osobniks[i].gen[k] = populacja.osobniks[j].gen[k];
                        populacja.osobniks[j].ocena = 1000000;
                    }
                    break;
                }else {
                    populacja.osobniks[j].color = Color.black;
                }
            }
        }
    }


    //tworzenie brakujacych osobnikow po selekcji
    public void reprodukcja(){
        for (int i = stopienSelekcji; i < nowaPopulacja.osobniks.length; i++) {


            //losowanie osobnikow do reprodukcji
           int wybraniec1 = random.nextInt(liczbaPopulacji);
           int wybraniec2 = random.nextInt(liczbaPopulacji);


            //losowanie wybranych osobnikow w przypadku gdy poprzednie losowanie wybralo tych samych
            while (wybraniec1 == wybraniec2) {
                wybraniec1 = random.nextInt(liczbaPopulacji);
                wybraniec2 = random.nextInt(liczbaPopulacji);
            }


            //losowanie miejsca preciecia genu z pominieciem przedzialu
            // ktory zostaje zachowany w przypadku dodania nowej czesci genu
            int miejscePrzeciecia = random.nextInt(populacja.osobniks[0].gen.length - ileDodano) + ileDodano;


            //zamiana pierwszej czesci genu pomijajac czesc genu ktory zostaje zachowany
            for (int j = ileDodano; j < miejscePrzeciecia; j++) {
                nowaPopulacja.osobniks[i].gen[j] = populacja.osobniks[wybraniec1].gen[j];
                nowaPopulacja.osobniks[i].genPredkosci[j] = populacja.osobniks[wybraniec1].genPredkosci[j];
            }


            //zamiana drugiej czesci genu
            for (int j = miejscePrzeciecia; j < populacja.osobniks[0].dlugoscGenu; j++) {
                nowaPopulacja.osobniks[i].gen[j] = populacja.osobniks[wybraniec2].gen[j];
                nowaPopulacja.osobniks[i].genPredkosci[j] = populacja.osobniks[wybraniec2].genPredkosci[j];
            }


            //losowa mutacja
            mutacja(i, this.pBmutacji);
        }


        //tworzenie populacji z nowej populacji
        for (int i = 0; i < populacja.osobniks.length; i++) {
            for (int j = 0; j < populacja.osobniks[0].dlugoscGenu; j++) {
                populacja.osobniks[i].gen[j] = nowaPopulacja.osobniks[i].gen[j];
                populacja.osobniks[i].genPredkosci[j] = nowaPopulacja.osobniks[i].genPredkosci[j];
            }
        }
    }


    void zerujOcene(Populacja populacjaDoWyzerowania){
        for (int i = 0; i < populacjaDoWyzerowania.osobniks.length; i++) {
            populacjaDoWyzerowania.osobniks[i].ocena = 0;
            populacjaDoWyzerowania.osobniks[i].ocenaDoDodania = 0;
        }
    }


    void mutacja(int i, int pBmutacji){
        if (random.nextInt(100) < pBmutacji){ //prawdopodobienstwo mutacji
            for (int j = ileDodano; j < populacja.osobniks[0].dlugoscGenu; j++) {
                populacja.osobniks[i].gen[j] = random.nextInt(360);
                populacja.osobniks[i].genPredkosci[j] = random.nextInt(populacja.osobniks[0].predkosc)+1;
            }
        }
    }


    public boolean celOsiagniety(double najlepszaOcena){

        if (najlepszaOcena < 300){
            return true;
        }else {
            return false;
        }

    }


    public void pokazPoOsiagnieciuCelu(double najlepszaOcena){

            for (int i = 0; i < populacja.osobniks.length; i++) {
                if (populacja.osobniks[i].ocena == najlepszaOcena){

                    System.out.println(populacja.osobniks[i].x + " " + populacja.osobniks[i].y);
                    System.out.println(najlepszaOcena);

                    for (int j = 0; j < populacja.osobniks.length; j++) {
                        if (j!=i){
                            for (int k = 0; k < populacja.osobniks[0].gen.length; k++) {
                                populacja.osobniks[j].gen[k] = populacja.osobniks[i].gen[k];
                                populacja.osobniks[j].genPredkosci[k] = populacja.osobniks[i].genPredkosci[k];
                            }
                        }
                    }
                    break;
                }
            }
    }

    @Override
    public void run() {
        int ktoryGen = 0;


        while (true){


            //reset zmiennej ktoryGen i zakonczenie generacji
            if (ktoryGen == populacja.osobniks[0].gen.length){
                ktoryGen = 0;
                koniecGeneracji = true;
            }


            //procedura zakonczenia generacji i tworzenie nowej w przypadku gdy nie dodajemy nowych krokow (genow)
            if (koniecGeneracji == true && generacja != kiedyDodacKroki){
                for (int i = 0; i < populacja.osobniks.length; i++) {
                    populacja.osobniks[i].zycie = true;
                    populacja.osobniks[i].ocen(1000,100);
                }


                //nadawanie koloru najlepszemu osobnikowi
                for (int i = 0; i < populacja.osobniks.length; i++) {
                    populacja.najlepszy();
                    if (populacja.osobniks[i].najlepszy){
                        populacja.osobniks[i].color = Color.red;
                        najlepszaOcena = populacja.osobniks[i].ocena;
                    }
                }



                selekcja();
                reprodukcja();




                zerujOcene(populacja);
                zerujOcene(nowaPopulacja);
                populacja.resetujPozycje();

                populacja.sciany[0].reset(200,200);
                populacja.sciany[1].reset(300,20);
                populacja.sciany[2].reset(600,100);
                populacja.sciany[3].reset(450,20);

                koniecGeneracji = false;
                generacja+=1;

            }


            //procedura zakonczenia generacji w przypadku gdy dodajemy nowe kroki (genomy)
            if (koniecGeneracji == true && generacja == kiedyDodacKroki){
                kiedyDodacKroki += kiedyDodawacKroki;

                System.out.println("dodanie krokow");

                for (int i = 0; i < populacja.osobniks.length; i++) {
                    populacja.osobniks[i].ocen(1000,100);
                }

                //wyznaczenie najlepszego osobnika
                populacja.najlepszy();


                //dodawanie nowych genow do tablicy przy zachowaniu czesci z najlepszego genomu
                for (int i = 0; i < populacja.osobniks.length; i++) {


                    //dodanie wszystkim osobnikom nowych genow z zachowaniem poczatku genomu od najlepszego osobnika
                    if (populacja.osobniks[i].najlepszy == true){
                        populacja.osobniks[i].color = Color.red;

                        for (int j = 0; j < populacja.osobniks.length; j++) {
                            //pominiecie najlepszego osobnika
                            if (j != i) {
                                populacja.osobniks[j].dodajKroki(populacja.osobniks[i].gen, populacja.osobniks[i].genPredkosci, noweKroki);
                            }
                        }

                        //zwiekszanie dlugosci genu najlepszemu osobnikowi
                        populacja.osobniks[i].dodajKroki(populacja.osobniks[i].gen, populacja.osobniks[i].genPredkosci, noweKroki);


                        //zwiekszanie tablicy nowej populacji
                        for (int j = 0; j < populacja.osobniks.length; j++) {
                            nowaPopulacja.osobniks[j].dodajKroki(populacja.osobniks[i].gen, populacja.osobniks[i].genPredkosci, noweKroki);
                        }

                    }

                    populacja.osobniks[i].zycie = true;
                }


                zerujOcene(populacja);
                zerujOcene(nowaPopulacja);
                populacja.resetujPozycje();

                populacja.sciany[0].reset(200,200);
                populacja.sciany[1].reset(300,20);
                populacja.sciany[2].reset(600,100);

                ileDodano +=noweKroki;
                koniecGeneracji = false;
                generacja +=1;
            }





            for (int i = 0; i < populacja.osobniks.length; i++) {
                populacja.osobniks[i].obecnyKat = populacja.osobniks[i].gen[ktoryGen];
                populacja.osobniks[i].obecnyGenPredkosci = populacja.osobniks[i].genPredkosci[ktoryGen];
            }


            //zmienna ktoryGen zlicza ktory gen zostal juz uzyty
            ktoryGen+=1;

            try {
                Thread.sleep(this.czestotliwoscZakretow);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
