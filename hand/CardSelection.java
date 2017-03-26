//package hand;


import java.util.*;

/**
 * Created by AlexNewman on 2/12/17.
 */
class CardSelection {
    private ArrayList handi;
    private float[] score;
    private List<String> cuts;
    private ArrayList<String> cutcombos;
    private String[][] subsets;

    private CardSelection(String[] input) {
        this.handi = new ArrayList(Arrays.asList(input));
        this.cuts = new ArrayList<>(); //Don't use this after you get cutcombos
        this.cutcombos = new ArrayList<>();
        this.subsets = new String[15][4];
        this.score = new float[15];

        String[] suit = {"D", "S", "C", "H"};

        for (String v : suit) {
            for (int num = 2; num < 11; num++) {
                cuts.add(num + v);
            }
            cuts.add("A" + v);
            cuts.add("J" + v);
            cuts.add("Q" + v);
            cuts.add("K" + v);
        }
        cutcard(this);
        combinations(this);
    }


    private static void cutcard(CardSelection cards) {
        for (String s : cards.cuts) {
            if (!cards.handi.contains(s)) {
                cards.cutcombos.add(s); //creates list of potential cut cards
            }
        }
    }

    /* Finds all combos of hands and puts them in array called subset*/
    private static void combinations(CardSelection hands) {
        Object[] objs = hands.handi.toArray();
        String[] handi = new String[6];
        for (int i = 0; i < handi.length; i++) {
            String someString = (String) objs[i];
            handi[i] = someString;
        }
        String[][] newhand = {
                {handi[0], handi[1], handi[2], handi[3]},
                {handi[0], handi[1], handi[2], handi[4]},
                {handi[0], handi[1], handi[2], handi[5]},
                {handi[0], handi[1], handi[3], handi[4]},
                {handi[0], handi[1], handi[3], handi[5]},
                {handi[0], handi[1], handi[4], handi[5]},
                {handi[0], handi[2], handi[3], handi[4]},
                {handi[0], handi[2], handi[3], handi[5]},
                {handi[0], handi[2], handi[4], handi[5]},
                {handi[0], handi[3], handi[4], handi[5]},
                {handi[1], handi[2], handi[3], handi[4]},
                {handi[1], handi[2], handi[3], handi[5]},
                {handi[1], handi[2], handi[4], handi[5]},
                {handi[1], handi[3], handi[4], handi[5]},
                {handi[2], handi[3], handi[4], handi[5]}};
        hands.subsets = newhand;

    }

    private void flush(CardSelection cards) {
        for (int i = 0; i < 15; i++) { //For every combo
            String[] combo = this.subsets[i]; //Get combo
            char firstsuit = combo[0].charAt(1); //Get first suit; eg. D H C S
            String firstsuitstring = Character.toString(firstsuit);
            int numsame = 0;
            for (String y : combo) {
                if (y.contains(firstsuitstring)) {
                    numsame++;
                }
            }
            /* If all cards in hand are same suit */
            if (numsame == 4) {
                int x = 0;
                /* Adds potential for cut card matching suit*/
                for (String s : cards.cutcombos) {
                    String[] val = s.split("");
                    if (val[1].equals(firstsuitstring)) {
                        x += 1;
                    }
                }
                double y = 1.0 * x / 46;
                score[i] += 4 + y;
            }
        }
    }


    private int fifteensplit(String card) {

        String[] valarray = card.split("");
        if (valarray[0].equals("K") || valarray[0].equals("Q") || valarray[0].equals("J") || valarray.length == 3) {
            return 10;
        } else if (valarray[0].equals("A")) {
            return 1;
        } else {
            return Integer.parseInt(valarray[0]);
        }
    }

    private void fifteen(CardSelection cards) {
        for (int i = 0; i < cards.subsets.length - 1; i++) { // for all potential 4-card hands
            String[] j = cards.subsets[i]; //Get cards

            ArrayList<Integer> nums = new ArrayList<>();
            /* Converts Face/ace cards to correct numeric values*/
            for (String z : j) {
                nums.add(fifteensplit(z));
            }

            /* Takes into account cut card */
            for (String x : cards.cutcombos) {
                double total = 0.0;
                /* Same conversion for cut card */
                nums.add(fifteensplit(x));

                if (nums.get(0) + nums.get(1) + nums.get(2) + nums.get(3) + nums.get(4) == 15) {
                    total += 2;
                }

                /** Rewrite to be more like pairs */

                for (int s = 0; s < nums.size(); s++) {
                    for (int t = s + 1; t < nums.size(); t++) {
                        for (int u = t + 1; u < nums.size(); u++) {
                            for (int v = u + 1; v < nums.size(); v++) {
                                if (nums.get(s) + nums.get(t) + nums.get(u) + nums.get(v) == 15) {
                                    total += 2;
                                }
                            }
                            if (nums.get(s) + nums.get(t) + nums.get(u) == 15) {
                                total += 2;
                            }
                        }
                        if (nums.get(s) + nums.get(t) == 15) {
                            total += 2;
                        }
                    }
                }
                score[i] += 1.0 * (total / 46);
            }
        }
    }

    private String pairsplit(String card) {
        String[] valarray = card.split("");
        return valarray[0];
    }

    private int pairchecker(ArrayList<String> nums) {

        /*Mayyyyyybe a problem if theres a pair and then a 3 of a kind --- check later */
        ArrayList<String> vals = new ArrayList<>();
        for (String ind : nums) {
            vals.add(ind);
        }
        int pairtotal = 0;
        for (int s = 0; s < vals.size(); s++) {
            String x = vals.get(s);
            for (int t = s + 1; t < vals.size(); t++) {
                String y = vals.get(t);
                if (vals.get(s).equals(vals.get(t))) {
                    pairtotal += 2;
                    for (int u = t + 1; u < vals.size(); u++) {
                        String z = vals.get(u);
                        if (vals.get(s).equals(vals.get(u))) {
                            pairtotal += 1;
                            for (int v = u + 1; v < vals.size(); v++) {
                                if (vals.get(s).equals(vals.get(v))) {
                                    return 4; //No other pairs can exist if 4 of a kind
                                }
                            }
                            vals.remove(z);
                            break;
                        }
                    }
                    vals.remove(x);
                    vals.remove(y);
                    for (int first = 0; first < vals.size(); first++) {
                        for (int sec = first + 1; sec < vals.size(); sec++) {
                            if (vals.get(first).equals(vals.get(sec))) {
                                pairtotal += 2;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return pairtotal;
    }

    private void pairs(CardSelection cards) {
        for (int i = 0; i < cards.subsets.length - 1; i++) { // for all potential 4-card hands
            String[] j = cards.subsets[i]; //Get cards
            ArrayList<String> values = new ArrayList<>();

            for (String z : j) {
                values.add(pairsplit(z));
            }
            for (String x : cards.cutcombos) { //Adds cut card
                double total = 0.0;
                /* Same conversion for cut card */
                String val = pairsplit(x);
                values.add(val);

                total += pairchecker(values);

            }
        }
    }

    private int runsplit(String card) {

        String[] valarray = card.split("");
        if (valarray.length == 3) {
            return 10;
        } else if (valarray[0].equals("J")) {
            return 11;
        } else if (valarray[0].equals("Q")) {
            return 12;
        } else if (valarray[0].equals("K")) {
            return 13;
        } else if (valarray[0].equals("A")) {
            return 1;
        } else {
            return Integer.parseInt(valarray[0]);
        }
    }

    private int runchecker(ArrayList<Integer> nums) {
        /* Not finished; still need to account for order*/
        ArrayList<Integer> vals = new ArrayList<>();
        for (int ind : nums) {
            vals.add(ind);
        }
        int runtotal = 0;
        for (int s = 0; s < vals.size(); s++) {
            int x = vals.get(s);
            for (int t = s + 1; t < vals.size(); t++) {
                int y = vals.get(t);
                if (x - 1 == y || x + 1 == y) {
                    int lowest = x;
                    int highest = y;
                    if (x > y) {
                        lowest = y;
                        highest = x;
                    }
                    for (int u = t + 1; u < vals.size(); u++) {
                        int z = vals.get(u);
                        if (lowest - 1 == z || highest + 1 == z) {
                            if (z < lowest) {
                                lowest = z;
                            } else {
                                highest = z;
                            }
                            runtotal += 3;
                            for (int v = u + 1; v < vals.size(); v++) {
                                int alpha = vals.get(v);
                                if (lowest - 1 == alpha || highest + 1 == alpha) {
                                    if (alpha < lowest) {
                                        lowest = z;
                                    } else {
                                        highest = alpha;
                                    }
                                    runtotal += 1;
                                    if (v != vals.size() - 1 && (lowest - 1 == vals.get(v + 1) || highest + 1 == vals.get(v + 1))) {
                                        return 5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return runtotal;
    }


    private void runs(CardSelection cards) {
        for (int i = 0; i < cards.subsets.length - 1; i++) { // for all potential 4-card hands
            String[] j = cards.subsets[i]; //Get cards

            ArrayList<Integer> nums = new ArrayList<>();
            /* Converts Face/ace cards to correct numeric values*/
            for (String z : j) {
                nums.add(runsplit(z));
            }

            /* Takes into account cut card */
            for (String x : cards.cutcombos) {
                double total = 0.0;
                /* Same conversion for cut card */
                int val = runsplit(x);
                nums.add(val);
                total += runchecker(nums);
                score[i] += 1.0 * (total / 46);

                /** Note: without casting, Java removes corresponding index! So subtle!*/
                nums.remove((Object) val);
            }
        }
    }

    private void findscore(){
        int maxIndex = 0;
        for (int i = 1; i < score.length; i++) {
            int newnumber = i;
            if (score[newnumber] > score[maxIndex]) {
                maxIndex = i;
            }
        }

        StringBuilder builder = new StringBuilder();

        for (String x : subsets[maxIndex]){
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(x);
        }
        System.out.print(builder);
    }

    public static void main(String[] args) {
        String[] x = {"2H", "3C", "4S", "7S", "8C", "AC"};
        CardSelection init = new CardSelection(x);

        // TODO: reformat functions to not take init is arg
        init.flush(init); //Special case
        init.fifteen(init); //checks how each hand adds up to 15- average
        init.pairs(init);
        //init.runs(init);
        init.findscore();
    }

}
