package lab.base;

import java.util.ArrayList;
import java.util.List;

public class BaseStructure {
    public static void main(String[] args) {
        System.out.println("Starting Java programing test - part Base Structure.");
        BaseStructure baseStructure = new BaseStructure();
        baseStructure.varArgTask();
    }

    /**
     *  Zavolejte metodu calcSum s alespoň dvěmi parametry.
     *  Výsledek vypište do konzole.
     */
    public void varArgTask() {
        System.out.println(calcSum(5,3,-8, 0, 100, -69));
    }

    /**
     * Vytvořte veřejnou metodu calcSum, která vrací řetězec a má
     * jako argument celá čísla (libovolný počet).
     * Metoda čísla sečte a do řetězce který bude vracet vypíše
     * rovnici pro součet čísel. Např:
     * 2 + 8 + 7 + (-9) = 8
     */
    //TODO
    public String calcSum(int... params) {
        String s_sum = "";
        int sum = 0;
//        for (int param : params) {
//            s_sum = s_sum + (!s_sum.isEmpty() ? " + " : "") + (param > 0 ? param : ("(" + param + ")"));
//            sum += param;
//        }
//        s_sum = s_sum + " = " + sum;
//        return s_sum;

        List<String> strNumbers = new ArrayList<>();
        for (int param : params) {
//            strNumbers.add(String.valueOf(param));
//            strNumbers.add(param + "");
            if(param < 0) {
//                strNumbers.add("(" + Integer.toString(param) + ")");
                strNumbers.add(String.format("(%d)", param));
            } else {
                strNumbers.add(Integer.toString(param));
            }
            sum += param;
        }
        return String.join(" + ", strNumbers) + " = " + sum;


    }

}
