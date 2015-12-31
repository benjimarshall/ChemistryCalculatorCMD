package io.github.benjimarshall;

import io.github.benjimarshall.chem.Element;
import io.github.benjimarshall.chem.FlagException;
import io.github.benjimarshall.chem.Molecule;
import io.github.benjimarshall.chem.NotationInterpretationException;

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
        }
        catch (FlagException | NotationInterpretationException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
