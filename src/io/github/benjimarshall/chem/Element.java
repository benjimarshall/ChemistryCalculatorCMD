package io.github.benjimarshall.chem;

public class Element {
    public Element() {

    }

    public Element(String name, int flag) {
        /*
        SYMBOL = 0
        NAME = 1
        */

    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getMassNumber() {
        return massNumber;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    private String symbol;
    private String name;
    private double massNumber;
    private int atomicNumber;

    public final static int SYMBOL = 0;
    public final static int NAME = 1;


}
