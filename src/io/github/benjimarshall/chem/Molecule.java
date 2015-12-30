package io.github.benjimarshall.chem;

import java.util.HashMap;

public class Molecule {
    public Molecule() {

    }

    public Molecule(String formula) throws NotationInterpretationException {
        // NEED A RAZOR FOR SYMBOLS AS DOES ELEMENT FOR SYMBOLS AND NUMBERS AND MULTIPLE CAPS
        this.formula = formula;
        String[] stringElements = this.formula.split("(?=\\p{Upper})");
        String[] stringPart = {null, null};
        try {
            for (String stringElement : stringElements) {
                stringPart = stringElement.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                this.elementList.put(new Element(stringPart[0], Element.SYMBOL), Integer.parseInt(stringPart[1]));
            }
            this.relativeFormulaMass = 0;
            for (HashMap.Entry<Element, Integer> entry : this.elementList.entrySet()) {
                this.relativeFormulaMass += entry.getValue() * entry.getKey().getMassNumber();
            }
        }
        catch (FlagException e) {
            System.out.println("Add unexpected flag exception occurred: " + e.getMessage());
        }
        catch (NotationInterpretationException e) {
            throw new NotationInterpretationException(e.getMessage() + ": " + stringPart[0]);
        }
        catch (IndexOutOfBoundsException e) {
            throw new NotationInterpretationException("Capital letter expected after number");
        }
    }

    public String getFormula() {
        return formula;
    }

    public HashMap<Element, Integer> getElementList() {
        return elementList;
    }

    public double getRelativeFormulaMass() {
        return relativeFormulaMass;
    }

    private String formula;
    private HashMap<Element, Integer> elementList = new HashMap<>();
    private double relativeFormulaMass;
}
