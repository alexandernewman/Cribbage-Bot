package hand;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by AlexNewman on 2/12/17.
 */
class CardSelection {
    private Set handi;
    private double[] score = new double[15];
    private Set<String> cuts;
    private Set cutcombos;


    CardSelection(String[] input) {
        this.handi = new HashSet<>(Arrays.asList(input));
        this.cuts = new HashSet<String>();

        String[] suit = new String[4];
        suit[0] = "D";
        suit[1] = "S";
        suit[2] = "C";
        suit[2] = "H";

        for (String v : suit) {
            for (int num = 2; num < 11; num++) {
                cuts.add(num + v);
            }
            cuts.add("A" + v);
            cuts.add("J" + v);
            cuts.add("Q" + v);
            cuts.add("K" + v);
        }
    }


    private void cutcard(Set hands) {
        for (String s : cuts) {
            if (!handi.contains(s)) {
                cutcombos.add(s);
            }
        }
    }

    private static void combinations(String[] vals) {
        String[] subset = new String[4];
        processLargerSubsets(vals, subset, 0, 0);
    }

    private static void processLargerSubsets(String[] vals, String[] subset, int subsetSize, int nextIndex) {
        if (subsetSize != subset.length) {
            for (int j = nextIndex; j < vals.length; j++) {
                subset[subsetSize] = vals[j];
                processLargerSubsets(vals, subset, subsetSize + 1, j + 1);
            }
        }
    }

    private void fifteen(Set handcombo) {
    }

    private void pairs(Object[] handcombo) {
    }

    private void flush(Object[] handcombo) {
    }

    private void cut(Object[] handcombo) {
    }



    private void main(String args[]) {
        cutcard(handi); //creates list of potential cut cards
        combinations((String[]) handi.toArray());
        //fifteen(cutcombos); //checks how each hand adds up to 15- average
        //pairs(args[0]);
        // runs(args[0]);
        // flush(args[1]);
        // cut(args[0], args[1]);
        //System.out. print.ln(arrayfind(score)); //maybe not this?
    }

}
