package hand;


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
    private HashMap<String[], List> bigcombos;


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
        bigcombos(this);
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

    private static void bigcombos(CardSelection cards) {
        cards.bigcombos = new LinkedHashMap<>();
        for (String[] s1 : cards.subsets) {
            cards.bigcombos.put(s1, cards.cutcombos);
        }
    }

    private void flush(CardSelection cards) { //NOTE: Doesn't incorporate starting suit match--minimal score impact
        for (int i = 0; i < 15; i++) {//For every combo
            String[] combo = this.subsets[i]; //Get combo
            char firstsuit = combo[0].charAt(1); //Get first suit ie D H C S
            String firstsuitstring = Character.toString(firstsuit);
            int numsame = 0;
            for (String y : combo) {
                if (y.contains(firstsuitstring)) {
                    numsame++;
                }
            }
            if (numsame == 4) {
                int x = 0;
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


    private void fifteen(CardSelection cards) {
        for (int i = 0; i < cards.subsets.length - 1; i ++){
            String[] j = cards.subsets[i];
            double total = 0.0;
            for (String x : cards.cutcombos) {
                ArrayList<Integer> nums = new ArrayList<>();
                String[] valarray;
                for (String z : j) {
                    valarray = z.split("");
                    if (valarray[0].equals("K") || valarray[0].equals("Q") || valarray[0].equals("J") || valarray.length == 3) {
                        nums.add(10);
                    } else if (valarray[0].equals("A")) {
                        nums.add(1);
                    } else {
                        nums.add(Integer.parseInt(valarray[0]));
                    }
                }

                valarray = x.split("");
                if (valarray[0].equals("K") || valarray[0].equals("Q") || valarray[0].equals("J") || valarray.length == 3) {
                    nums.add(10);
                } else if (valarray[0].equals("A")) {
                    nums.add(1);
                } else {
                    nums.add(Integer.parseInt(valarray[0]));
                }

                if (nums.get(0) + nums.get(1) + nums.get(2) + nums.get(3) + nums.get(4) == 15) {
                    total += 2;
                }
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
            }
            score[i] += 1.0 * total / 46;

        }
    }

    private void pairs() {
    }

    private void runs() {

    }


    public static void main(String args[]) {
        String[] x = {"4D", "3D", "KD", "10D", "8C", "AC"};
        CardSelection init = new CardSelection(x);
        init.flush(init); //Special case
        init.fifteen(init); //checks how each hand adds up to 15- average
        init.pairs();
        init.runs();


        //System.out. print.ln(arrayfind(score)); //maybe not this?
    }

}
