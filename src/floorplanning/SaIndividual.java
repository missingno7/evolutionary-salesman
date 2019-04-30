/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanning;

import cellularevoalg.IndData;
import cellularevoalg.Individual;
import cellularevoalg.Other;
import cellularevoalg.PopConfig;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author J_Vestfal
 */
public class SaIndividual extends Individual {

    SaIndividual(int scWidth, int scHeight, int citiesCnt) {
        this.scWidth = scWidth;
        this.scHeight = scHeight;
        cities = new int[citiesCnt];

    }

    @Override
    public Individual makeBlank() {
        return new SaIndividual(scWidth, scHeight, cities.length);
    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar, int from, Random rnd) {

        for (int i = from; i < ar.length - 1; i++) {
            int index = i + 1 + rnd.nextInt(ar.length - i - 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }

    }

    @Override
    public void randomize(IndData data, Random rnd) {

        int maxX = scWidth;
        int maxY = scHeight;

        SaData fpData = (SaData) data;

        for (int i = 0; i < cities.length; i++) {
            cities[i] = i;
        }
        shuffleArray(cities, 0, rnd);

    }

    @Override
    public Individual fromFile(String filename
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void toFile(String filename
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void reversePart(int from, int to) {
        if (from == to) {
            return;
        }

        int frmi = from;
        int toi = to;

        do {

            int tmp = cities[frmi];
            cities[frmi] = cities[toi];
            cities[toi] = tmp;

            frmi++;
            toi--;

            if (toi < 0) {
                toi = cities.length - 1;
            }

            if (frmi >= cities.length) {
                frmi = 0;
            }

        } while ((frmi - 1) != toi && frmi != toi);

    }

    public void shiftMultiple(int from, int to, boolean forward, int cnt) {

        if (forward) {
            for (int i = cnt - 1; i >= 0; i--) {

                int frmi = (from + i) % cities.length;
                int toi = (to + i) % cities.length;

                //System.out.println(frmi + " + " + toi);
                shift(frmi, toi, true);
            }
        } else {
            // backwards
            for (int i = 0; i < cnt; i++) {

                int frmi = (from + i) % cities.length;
                int toi = (to + i) % cities.length;

                shift(frmi, toi, false);

            }

        }

    }

    public void shift(int from, int to, boolean forward) {

        if (from == to) {
            return;
        }

        if (forward) {

            int i = from;
            do {

                int next = i + 1;
                if (next >= cities.length) {
                    next = 0;
                }

                int tmp = cities[i];
                cities[i] = cities[next];
                cities[next] = tmp;

                i = next;
            } while (i != to);

        } else {
            // backwards

            int i = from;
            do {

                int next = (i - 1);
                if (next < 0) {
                    next = cities.length - 1;
                }

                int tmp = cities[i];
                cities[i] = cities[next];
                cities[next] = tmp;

                i = next;
            } while (i != to);

        }
    }

    
    /*
    // Faster
    
    @Override
    public void mutate(float amount, float probability, Random rnd) {

        float shiftprob = PopConfig.getInstance().reg.getFloat("shifprob")[0];
        float revprob = PopConfig.getInstance().reg.getFloat("revprob")[0];

        if (rnd.nextFloat() < probability) {
            // Swapping
            int swaps = (int) amount + 1;

            swaps = (rnd.nextInt(swaps));

            if (swaps == 0) {
                swaps = 1;
            }

            for (int i = 0; i < swaps; i++) {
                int a = rnd.nextInt(cities.length);
                int b = rnd.nextInt(cities.length);

                // Simple swap
                int tmp = cities[a];
                cities[a] = cities[b];
                cities[b] = tmp;
            }
        }
        if (rnd.nextFloat() < shiftprob) {
            // Shifting

            int cnttoshift = rnd.nextInt(cities.length);
            if (cnttoshift == 0) {
                cnttoshift = 1;
            }

            shiftMultiple(rnd.nextInt(cities.length), rnd.nextInt(cities.length), rnd.nextBoolean(), cnttoshift);
        }

        if (rnd.nextFloat() < revprob) {
            reversePart(rnd.nextInt(cities.length), rnd.nextInt(cities.length));
        }

    }
     */
 
    //Slower
    @Override
    public void mutate(float amount, float probability, Random rnd) {

        float shiftprob = PopConfig.getInstance().reg.getFloat("shifprob")[0];
        float revprob = PopConfig.getInstance().reg.getFloat("revprob")[0];

        int swapWith;
        for (int i = 0; i < cities.length; i++) {

            if (rnd.nextFloat() < probability) {
                swapWith = rnd.nextInt(cities.length);
                if (swapWith == i) {
                    continue;
                }
                // Simple swap
                int tmp = cities[i];
                cities[i] = cities[swapWith];
                cities[swapWith] = tmp;
            }

        }

        if (rnd.nextFloat() < shiftprob) {
            // Shifting

            int cnttoshift = rnd.nextInt(cities.length);
            if (cnttoshift == 0) {
                cnttoshift = 1;
            }

            shiftMultiple(rnd.nextInt(cities.length), rnd.nextInt(cities.length), rnd.nextBoolean(), cnttoshift);
        }

        if (rnd.nextFloat() < revprob) {
            reversePart(rnd.nextInt(cities.length), rnd.nextInt(cities.length));
        }

    }
     
    public void fixIndividual() {

        System.out.println("FIXING INDIVIDUAL");
        //String before=toString(null);
        boolean[] myarray = new boolean[cities.length];
        Arrays.fill(myarray, false);

        for (int i = 0; i < cities.length; i++) {

            if (myarray[cities[i]]) {
                // Is already used
                System.out.println("USED");
                for (int j = 0; j < cities.length; j++) {
                    if (!myarray[j]) {

                        cities[i] = j;
                        myarray[j] = true;
                        System.out.println("SUBSTITUED");
                        break;

                    }
                }
            }

            myarray[cities[i]] = true;
        }

        //String after=toString(null);
        //System.out.println("--- BEFORE: "+before + " --- " + after);
    }

    @Override
    public void crossoverTo(Individual secondOne, Individual ind, Random rnd) {
        SaIndividual cInd = (SaIndividual) ind;
        SaIndividual cFirstOne = this;
        SaIndividual cSecondOne = (SaIndividual) secondOne;

        //cInd.cities[0] = 0;
        boolean[] myarray = new boolean[cities.length];
        //Arrays.fill(myarray, false);

        // System.out.println("STARTFOR");
        int crossPoint = rnd.nextInt(cities.length);
        int i = crossPoint;

        do {
            //System.out.println(i);
            //for (int i=0; i<cities.length ; i++) {

            if ((!myarray[cFirstOne.cities[i]] && !myarray[cSecondOne.cities[i]])) {
                if (rnd.nextBoolean()) {
                    cInd.cities[i] = cFirstOne.cities[i];
                } else {
                    cInd.cities[i] = cSecondOne.cities[i];
                }

            } else if (myarray[cFirstOne.cities[i]] && myarray[cSecondOne.cities[i]]) {

                //int ic=0;
                do {
                    cInd.cities[i] = rnd.nextInt(cities.length);
                    //ic++;
                } while (myarray[cInd.cities[i]]);
                //System.out.println(ic);

            } else if (myarray[cFirstOne.cities[i]] && !myarray[cSecondOne.cities[i]]) {
                cInd.cities[i] = cSecondOne.cities[i];

            } else if (!myarray[cFirstOne.cities[i]] && myarray[cSecondOne.cities[i]]) {
                cInd.cities[i] = cFirstOne.cities[i];
            }
            myarray[cInd.cities[i]] = true;

            i = (i + 1) % cities.length;
        } while ((i % cities.length) != crossPoint);

        //for (int i=startpoint; ((i+1)%cities.length)!=startpoint ; i=((i+1)%cities.length)) {
        //System.out.println(i);
        //System.out.println("ENDFOR");

        /*
        if(rnd.nextBoolean())
        {
             SaIndividual tmp=cFirstOne;
             cFirstOne=cSecondOne;
             cSecondOne=tmp;
        }
        
        int crossPoint = 1 + rnd.nextInt(cities.length - 2);

        for (int i = 0; i <= crossPoint; i++) {
            cInd.cities[i]=cFirstOne.cities[i];
        }

        for (int i = crossPoint; i < cities.length; i++) {
            cInd.cities[i]=cSecondOne.cities[i];
            // cInd.cities
        }

         */
        //cInd.fixIndividual();
    }

    @Override
    public void mutateTo(float amount, float probability, Individual ind, Random rnd) {
        copyTo(ind);
        ind.mutate(amount, probability, rnd);
    }

    @Override
    public void copyTo(Individual ind
    ) {
        SaIndividual fpInd = (SaIndividual) ind;
        System.arraycopy(cities, 0, fpInd.cities, 0, cities.length);

        fpInd.fitness = fitness;
        fpInd.colX = colX;
        fpInd.colY = colY;
        /*fpInd.mutamount=mutamount;
        fpInd.mutprob=mutprob;
        fpInd.mutprobflip=mutprobflip;
        fpInd.mutprobswitch=mutprobswitch;*/

    }

    float distance(float x1, float y1, float x2, float y2) {
        float x = (x2 - x1);
        float y = (y2 - y1);
        return (x * x) + (y * y);

    }

    @Override
    public void countFitness(IndData data) {
        // TODO COUNT FITNESS
        SaData fpData = (SaData) data;

        fitness = 0;

        for (int i = 0; i < cities.length - 1; i++) {

            float x1 = fpData.x[cities[i]];
            float x2 = fpData.x[cities[i + 1]];
            float y1 = fpData.y[cities[i]];
            float y2 = fpData.y[cities[i + 1]];
            fitness -= distance(x1, y1, x2, y2);

        }

        float x1 = fpData.x[cities[0]];
        float x2 = fpData.x[cities[cities.length - 1]];
        float y1 = fpData.y[cities[0]];
        float y2 = fpData.y[cities[cities.length - 1]];
        fitness -= distance(x1, y1, x2, y2);

    }

    @Override
    protected Individual clone() {
        SaIndividual fpInd = new SaIndividual(scWidth, scHeight, cities.length);
        copyTo(fpInd);

        return fpInd;
    }

    @Override
    public String toString(IndData data) {
        String res = "";

        for (int i = 0; i < cities.length; i++) {
            res += cities[i] + "->";
        }

        return res;
    }

    public void Draw(IndData data, String filename) {
        SaData fpData = (SaData) data;

        BufferedImage image = new BufferedImage(scWidth, scHeight, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        g.setColor(Color.red);

        for (int i = 0; i < cities.length - 1; i++) {
            float x1 = fpData.x[cities[i]];
            float x2 = fpData.x[cities[i + 1]];
            float y1 = fpData.y[cities[i]];
            float y2 = fpData.y[cities[i + 1]];

            g.setColor(Color.white);
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            g.setColor(Color.red);
            g.drawRect((int) x1 - 1, (int) y1 - 1, 3, 3);

        }
        float x1 = fpData.x[cities[0]];
        float x2 = fpData.x[cities[cities.length - 1]];
        float y1 = fpData.y[cities[0]];
        float y2 = fpData.y[cities[cities.length - 1]];
        g.setColor(Color.white);
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        g.setColor(Color.red);
        g.drawRect((int) x2 - 1, (int) y2 - 1, 3, 3);

        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(SaIndividual.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    int[] cities;
    int scWidth, scHeight;

    //float mutamount, mutprob,mutprobflip,mutprobswitch;
    @Override
    public void countColor() {
        colX = 0;
        colY = 0;

        int maxSum = (cities.length - 1) * (cities.length / 2);

        for (int i = 0; i < cities.length; i++) {
            if (i % 2 == 0) {
                if (i % 4 < 2) {
                    colX += cities[i];
                } else {
                    colX -= cities[i];
                }

            } else {
                if (i % 4 < 2) {
                    colY += cities[i];
                } else {
                    colY -= cities[i];
                }
            }
        }

        colX = Other.tanh(colX / maxSum);
        colY = Other.tanh(colY / maxSum);

    }
}
