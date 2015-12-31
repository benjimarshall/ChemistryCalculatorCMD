package io.github.benjimarshall.chem;

import java.util.HashMap;

public class Molecule {
    public Molecule() {

    }

    public Molecule(String formula) throws NotationInterpretationException {
        // A more explanatory error for finding symbol characters
        if (!formula.matches("[a-zA-Z0-9]*")) {
            throw new NotationInterpretationException("Formula contained non-letter, non-digit characters");
        }

        // Check to see if the formula is in a valid format
        if (!formula.matches("([A-Z][a-z]?\\d*)+")) {
            throw new NotationInterpretationException("Formula did meet notation standards of capital letters, lower" +
                    "case letters and numbers");
        }

        this.formula = formula;

        // Split the formula by capital letters, beginnings of new elements
        String[] stringElements = this.formula.split("(?=\\p{Upper})");

        // An array to temporarily store the symbol and quantity of each element
        String[] stringParts = {null, null};
        int quantity;
        try {
            // Loop through the list of elements
            for (String stringElement : stringElements) {
                // Split the element into its symbol and quantity
                stringParts = stringElement.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                // If the one is implicit, like carbon in CO2, then assign the quantity as one
                if (stringParts.length == 1) {
                    quantity = 1;
                }
                else {
                    quantity = Integer.parseInt(stringParts[1]);
                }

                // Feed the symbol and quantity into the element map
                // If the element has already been entered, increment the quantity
                if (this.elementMap.containsKey(new Element(stringParts[0], Element.SYMBOL))) {
                    this.elementMap.put(new Element(stringParts[0], Element.SYMBOL),
                            this.elementMap.get(new Element(stringParts[0], Element.SYMBOL)) + quantity);
                }
                // Else if the element hasn't already been entered, enter it with its current quantity
                else {
                    this.elementMap.put(new Element(stringParts[0], Element.SYMBOL), quantity);
                }
            }

            // Calculate the relative formula mass by adding up the relative masses multiplied by the number of atoms
            this.relativeFormulaMass = 0;
            for (HashMap.Entry<Element, Integer> entry : this.elementMap.entrySet()) {
                this.relativeFormulaMass += entry.getValue() * entry.getKey().getMassNumber();
            }
        }
        catch (FlagException e) {
            System.out.println("Add unexpected flag exception occurred: " + e.getMessage());
        }
        catch (NotationInterpretationException e) {
            throw new NotationInterpretationException(e.getMessage() + ": " + stringParts[0]);
        }
        catch (IndexOutOfBoundsException e) {
            throw new NotationInterpretationException("Capital letter expected after number");
        }
    }

    // Getters for the molecule
    public String getFormula() {
        return formula;
    }

    public HashMap<Element, Integer> getElementMap() {
        return elementMap;
    }

    public double getRelativeFormulaMass() {
        return relativeFormulaMass;
    }

    // Fields of the class
    private String formula;
    private HashMap<Element, Integer> elementMap = new HashMap<>();
    private double relativeFormulaMass;
}
