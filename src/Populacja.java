package pl.rafal;


public class Populacja implements Runnable{
    public Osobnik[] osobniks;
    public Sciana[] sciany;
    double predkosc;

    public Populacja(int liczbaPopulacji, int dlugoscGenu, double predkosc) {

        this.predkosc = predkosc;
        sciany = new Sciana[4];

        sciany[0] = new Sciana(200,200,30,100);
        sciany[1] = new Sciana(300,20,30,100);
        sciany[2] = new Sciana(600,150,30,200);
        sciany[3] = new Sciana(450,20,30,100);


        osobniks = new Osobnik[liczbaPopulacji];
        for (int i = 0; i < liczbaPopulacji; i++) {
            osobniks[i] = new Osobnik(dlugoscGenu);
        }
    }

    public double najlepszy(){
        double najwyzszaOcena = 100000;
        for (int i = 0; i < osobniks.length; i++) {
            if (osobniks[i].ocena < najwyzszaOcena){
                najwyzszaOcena = osobniks[i].ocena;
                osobniks[i].najlepszy = true;
                for (int j = 0; j < osobniks.length; j++) {
                    if (j!=i){
                        osobniks[j].najlepszy = false;
                    }

                }
            }
        }
        return najwyzszaOcena;
    }

    public void resetujPozycje(){
        for (int i = 0; i < osobniks.length; i++) {
            osobniks[i].x = 80;
            osobniks[i].y = 80;
        }
    }


    double a = 0;

    @Override
    public void run() {
        int ktoryOsobnik = 0;
        while (true) {
            if (ktoryOsobnik == osobniks.length){
                ktoryOsobnik = 0;
            }

            while (a<1000){
                a+=predkosc;
            }
            a = 0;
            osobniks[ktoryOsobnik].poruszaj(sciany);


            ktoryOsobnik +=1;
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
