package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import java.util.Comparator;

/**
 * Created by arsha on 16-Feb-17.
 * 
 * Comparator for Worst-Fit scheduling algorithm.
 */
public class WorstHostCompare implements Comparator<Host> {

    @Override
    public int compare(Host h1, Host h2) {

        double cpu1 = h1.getAvailableMips();
        double cpu2 = h2.getAvailableMips();

        double ram1 = h1.getRamProvisioner().getAvailableRam();
        double ram2 = h2.getRamProvisioner().getAvailableRam();

        // weights of the two dimensions
        double x = 0.7;
        double y = 0.3;

        // linear combination
        double total1 = cpu1*y + ram1*x;
        double total2 = cpu2*y + ram2*x;

//        System.out.println("host 1  " + ram1 +" "+ cpu1 + " " + total1);
//        System.out.println("host 2  " + ram2 +" "+ cpu2 + " " + total2);
//        System.out.println("--------------------------------------------");
        return (int) (total2 - total1);
    }
}
