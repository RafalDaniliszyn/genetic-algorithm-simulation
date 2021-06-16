package pl.rafal;

import java.awt.*;
import java.util.Random;

public class Osobnik extends Rectangle{
    Random random;
    public long[] gen;
    public long[] genPredkosci;
    public int dlugoscGenu;
    double ocena = 0;
    double ocenaDoDodania = 0;
    boolean zycie = true;
    int predkosc = 5;


    public int x = 80;
    public int y = 80;
    public int width;
    public int height;
    public long obecnyKat;
    public long obecnyGenPredkosci;

    boolean najlepszy = false;

    Color color;

    public Osobnik(int dlugoscGenu) {
        this.dlugoscGenu = dlugoscGenu;
        gen = new long[dlugoscGenu];
        genPredkosci = new long[dlugoscGenu];
        random = new Random();


        //nadaje poczatkowe ruchy
        for (int i = 0; i < dlugoscGenu; i++) {
            this.gen[i] = random.nextInt(360);
            this.genPredkosci[i] = random.nextInt(this.predkosc)+1;
        }

        this.width = 20;
        this.height = 20;
    }


    //nalezy podac najlepszy gen
    public void dodajKroki(long[] staryGen, long[] staryGenPredkosci, int noweKroki){
        this.gen = new long[dlugoscGenu+noweKroki];
        this.genPredkosci = new long[dlugoscGenu+noweKroki];


        //przeniesienie starego genu do nowego
        for (int i = 0; i < staryGen.length; i++) {
            this.gen[i] = staryGen[i];
            this.genPredkosci[i] = staryGenPredkosci[i];
        }


        //dopelnienie nowego genu losowymi liczbami
        for (int i = staryGen.length; i < this.gen.length; i++) {
            gen[i] = random.nextInt(360);
            genPredkosci[i] = random.nextInt(this.predkosc)+1;
        }


        //zwiekszenie dlugosci genu o nowe dodane geny
        dlugoscGenu +=noweKroki;

    }


    //ocenianie
    public void ocen(int celX, int celY){
        this.ocena = odlegloscOdCelu(celX, celY) + this.ocenaDoDodania;
    }


    //mierzenie odleglosci od celu
    public double odlegloscOdCelu(int celX, int celY){
        double a = this.x - celX;
        double b = this.y - celY;
        double c = Math.sqrt((a*a) + (b*b));
        return c;
    }


    void poruszaj(Sciana[] sciany){

        //obliczanie ruchu z wybranego kąta
        this.x = x + (int)Math.round(Math.cos(Math.toRadians(this.obecnyKat))*this.obecnyGenPredkosci);
        this.y = y + (int)Math.round(Math.sin(Math.toRadians(this.obecnyKat))*this.obecnyGenPredkosci);


        //zatrzymywanie osobnikow w polu
        if (this.x <= 0){
            this.x += obecnyGenPredkosci+4;
            this.ocenaDoDodania +=10;
        }
        if (this.y <= 20){
            this.y += obecnyGenPredkosci+4;
            this.ocenaDoDodania +=10;
        }
        if (this.y > 350){
            this.y -= obecnyGenPredkosci+4;
            this.ocenaDoDodania +=10;
        }

        //sprawdzanie kolizji i blokowanie
        for (int i = 0; i < sciany.length; i++) {

            if (this.kolizja(sciany[i]) == true){
                this.ocenaDoDodania +=100000;
                this.zycie = false;
            }

//            if (this.kolizja(sciany[i])== true){
//
//                this.ocena +=10;
//
//                if (this.x > sciany[i].x + (sciany[i].width-15)){
//                    this.x +=obecnyGenPredkosci+6;
//                }
//                if (this.x < sciany[i].x +10){
//                    this.x -=obecnyGenPredkosci+4;
//                }
//                if (this.y > sciany[i].y + sciany[i].height-15){
//                    this.y +=obecnyGenPredkosci+4;
//                }
//                if (this.y < sciany[i].y +10){
//                    this.y -=obecnyGenPredkosci+4;
//                }
//            }
        }
    }


    //wyswietlanie
    void wyswietl(Graphics g){
        g.fillRect(this.x, this.y, this.width, this.height);
    }


    //funkcja kolizji
    public boolean kolizja(Rectangle r) {
        int tw = this.width;
        int th = this.height;
        int rw = r.width;
        int rh = r.height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = r.x;
        int ry = r.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
              //overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }
}
