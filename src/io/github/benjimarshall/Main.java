package io.github.benjimarshall;

import io.github.benjimarshall.chem.*;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try {
            Element e = new Element("Sodium", Element.NAME);
            Element e2 = new Element("Sodium", Element.NAME);
            System.out.println("Two elements constructed from \"Sodium\" through .equals(): " + e.equals(e2));
            System.out.println("Element generated from \"Sodium\": " + e.getSymbol() + " " + e.getName() + " " +
                    e.getAtomicNumber() + " " + e.getMassNumber());

            Molecule molecule = new Molecule("Al2(CO3)3");
            System.out.println("Molecule from: \"Al2(CO3)3\"");
            for (HashMap.Entry<Element, Integer> entry : molecule.getElementMap().entrySet()) {
                System.out.println(entry.getKey().getName() + ": " + entry.getValue());
            }
            System.out.println("RFM: " + molecule.getRelativeFormulaMass());

            Equation equation = new Equation("C2H6 + O2 -> CO2 + H2O");
            Equation equation2 = new Equation("N2 + 3H2 -> 2NH3");
            Equation equation3 = new Equation("C + O2 -> CO2");
        }
        catch (FlagException | NotationInterpretationException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
