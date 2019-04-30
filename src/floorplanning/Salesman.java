/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanning;

import cellularevoalg.PopConfig;
import cellularevoalg.Population;
import java.util.Random;

/**
 *
 * @author J_Vestfal
 */
public class Salesman {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here

        String indPath = "out\\";

        PopConfig cfg = PopConfig.getInstance();

        // Custom parameter - number of squares
        cfg.reg.newInt("cities");
        cfg.reg.newInt("scwidth");
        cfg.reg.newInt("scheight");
        
        cfg.reg.newFloat("shifprob");
        cfg.reg.newFloat("revprob");

        cfg.LoadConfig("config.txt");

        SaData tstData = new SaData("squares.txt", cfg);

        SaIndividual tstInd = new SaIndividual(tstData.scWidth, tstData.scHeight, tstData.x.length);

        /*tstInd.randomize(tstData, new Random());
        
        System.out.println(tstInd.toString(tstData));
        tstInd.reversePart(10, 5);
        System.out.println(tstInd.toString(tstData));
        tstInd.reversePart(10, 5);
        System.out.println(tstInd.toString(tstData));
        */
        /*
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            int from = rnd.nextInt(tstInd.cities.length);
            int to = rnd.nextInt(tstInd.cities.length);
            
            int num=rnd.nextInt(tstInd.cities.length);

            
            
            
            
            if(from==to || num==0)
            {
                i--;
                continue;
            }
            
            String before = tstInd.toString(tstData);

            tstInd.shiftMultiple(from, to, true,num);
            tstInd.shiftMultiple(to, from, false,num);
            String after = tstInd.toString(tstData);

            if (!before.equals(after)) {
                
                System.out.println(from +"\t" + to+"\t"+num);
                //System.out.println("Error, FROM: " + from +", TO:"+ to+", NUM:"+num);
                //System.out.println(before);
                //System.out.println(after);
            }
        }
*/

       

        
        Population pop = new Population(tstInd, tstData, cfg);
        pop.Randomize(new Random());

        SaIndividual bestOne = (SaIndividual) pop.getBest();

        while (true) {
            SaIndividual bestInd = (SaIndividual) pop.getBest();

            if (bestInd.fitness > bestOne.fitness) {

                bestOne = (SaIndividual) bestInd.clone();

                bestOne.Draw(tstData, indPath + "ALLBEST" + ".png");
                bestInd.Draw(tstData, indPath + "IGEN" + Integer.toString(pop.getGen()) + ".png");

                if (cfg.drawpop) {
                    pop.paintPop(indPath + "GEN" + Integer.toString(pop.getGen()) + ".png");
                }

            }

            System.out.println("GENERATION " + pop.getGen());
            System.out.println(bestInd.toString(tstData));
            System.out.println("GEN BEST FITNESS: " + (bestInd.fitness));
            System.out.println("AVG FITNESS: " + (pop.avgFitness()));
            System.out.println("ALLBEST FITNESS: " + (bestOne.fitness));

            pop.nextGen();
        }
       
    }

}
