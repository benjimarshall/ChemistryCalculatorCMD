package io.github.benjimarshall;

import io.github.benjimarshall.chem.Element;
import io.github.benjimarshall.chem.FlagException;
import io.github.benjimarshall.chem.Molecule;
import io.github.benjimarshall.chem.NotationInterpretationException;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        try {
            Element e = new Element("Sodium", Element.NAME);
            System.out.println(e.getSymbol() + " " + e.getName() + " " + e.getAtomicNumber() + " " + e.getMassNumber());
            Molecule molecule = new Molecule("Ca1C1O3");
            for (HashMap.Entry<Element, Integer> entry : molecule.getElementList().entrySet()) {
                System.out.println(entry.getKey().getName() + ": " + entry.getValue());
            }
            System.out.println(molecule.getRelativeFormulaMass());
        }
        catch (FlagException | NotationInterpretationException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
