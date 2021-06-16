package pl.rafal;

import java.awt.*;

public class Sciana extends Rectangle {

    boolean ruchUkonczony;

    public Sciana(int x, int y, int width, int height) {
       this.x = x;
       this.y = y;
       this.width = width;
       this.height = height;
    }

    public void wyswietl(Graphics g){
        g.fillRect(this.x, this.y, this.width, this.height);
    }


    //poruszanie scianami
    public void ruch(int poczatekX,int koniecX, int poczatekY, int koniecY){

        if (this.y <= koniecY && ruchUkonczony == false){
            this.y +=2;
        }
        if (this.y >= koniecY-10){
            ruchUkonczony = true;
        }
        if (this.y >= poczatekY && ruchUkonczony == true){
            this.y -=2;
        }
        if (this.y == poczatekY){
            ruchUkonczony = false;
        }

    }


    public void reset(int x, int y){
        this.x = x;
        this.y = y;
    }

}
