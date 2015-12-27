package io.github.benjimarshall;

import io.github.benjimarshall.chem.Element;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");
        Element e = new Element ("Sodium", Element.NAME);
        System.out.println(e.getSymbol() + " " + e.getName() + " " + e.getAtomicNumber() + " " + e.getMassNumber());
    }
}
