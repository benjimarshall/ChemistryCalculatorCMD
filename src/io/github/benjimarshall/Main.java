package io.github.benjimarshall;

import io.github.benjimarshall.chem.Element;
import io.github.benjimarshall.chem.FlagException;
import io.github.benjimarshall.chem.NotationInterpretationException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        try {
            Element e = new Element("Sodium", Element.NAME);
            System.out.println(e.getSymbol() + " " + e.getName() + " " + e.getAtomicNumber() + " " + e.getMassNumber());
        }
        catch (FlagException | NotationInterpretationException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
