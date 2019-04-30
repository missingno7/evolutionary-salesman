/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesman;

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

        String indPath = "out\\";

        PopConfig cfg = PopConfig.getInstance();

        // Custom parameter
        cfg.reg.newInt("cities");
        cfg.reg.newInt("scwidth");
        cfg.reg.newInt("scheight");
        cfg.reg.newFloat("shifprob");
        cfg.reg.newFloat("revprob");

        cfg.LoadConfig("config.txt");

        SaData tstData = new SaData("squares.txt", cfg);

        SaIndividual tstInd = new SaIndividual(tstData.scWidth, tstData.scHeight, tstData.x.length);

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
