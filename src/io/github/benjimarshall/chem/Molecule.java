package io.github.benjimarshall.chem;

import java.util.HashMap;
import java.util.Set;

public class Molecule {
    private Molecule() {

    }

    public Molecule(String formula) throws NotationInterpretationException {
        // A more explanatory error for finding symbol characters
        if (!formula.matches("[a-zA-Z0-9\\(\\)]*")) {
            throw new NotationInterpretationException("Formula contained non-letter, non-digit characters");
        }

        // Check to see if the formula is in a valid format
        if (!formula.matches("([\\(]?[A-Z][a-z]?\\d*(\\)\\d*)?)+")) {
            throw new NotationInterpretationException("Formula did meet notation standards of capital letters, lower" +
                    "case letters and numbers");
        }

        // Check there are the same number of open brackets as close brackets
        if (!isMatchingNumberOfBrackets(formula)) {
            throw new NotationInterpretationException("Number of open brackets doesn't match the number of close " +
                    "brackets");
        }

        this.formula = formula;

        try {
            this.elementMap = parseFormula(formula);

            // Calculate the relative formula mass by adding up the relative masses multiplied by the number of atoms
            this.relativeFormulaMass = 0;
            for (HashMap.Entry<Element, Integer> entry : this.elementMap.entrySet()) {
                this.relativeFormulaMass += entry.getValue() * entry.getKey().getMassNumber();
            }
        }
        catch (FlagException e) {
            System.out.println("Add unexpected flag exception occurred: " + e.getMessage());
        }
        catch (IndexOutOfBoundsException e) {
            throw new NotationInterpretationException("Capital letter expected after number");
        }
    }

    private static HashMap<Element, Integer> parseFormula(String formula) throws FlagException,
            NotationInterpretationException {
        HashMap<Element, Integer> elementMap = new HashMap<>();
        HashMap<Element, Integer> receivedMap = new HashMap<>();
        int quantity;
        int multiplier;
        boolean enteredResultYet = true;
        String[] stringParts;

        // If the formula is wrapped in brackets: strip them
        if (formula.charAt(0) == '(' && formula.charAt(formula.length() - 1) == ')') {
            formula = formula.substring(1, formula.length() - 1);
        }
        // Add an end of word character
        formula += "*";

        // Make an initial word to work with
        String word = formula.substring(0,1);
        // Iterate over each character, skipping the first character which is already in the word variable
        for (Character c: formula.substring(1).toCharArray()) {
            // If a bracket has just been expanded, look for the number after the bracket
            if (!enteredResultYet) {
                // If the number has finished
                if (!Character.isDigit(c) || word.matches("[\\D]*")) {
                    // Check for implied 1 at the end of the bracket
                    if (word.matches("[\\D]")) {
                        multiplier = 1;
                    }
                    // Otherwise find the multiplier at the end of the bracket
                    else {
                        multiplier = Integer.parseInt(word);
                        word = "";
                    }

                    // Add the found quantities to the map
                    for (HashMap.Entry<Element, Integer> entry: receivedMap.entrySet()) {
                        // If that element is already mapped, add the new quantity to the original
                        if (elementMap.containsKey(entry.getKey())) {
                            elementMap.put(entry.getKey(),
                                    elementMap.get(entry.getKey()) + (entry.getValue() * multiplier));
                        }
                        // If the element is not already mapped, add it to the map with its quantity
                        else {
                            elementMap.put(entry.getKey(), entry.getValue() * multiplier);
                        }
                    }

                    enteredResultYet = true;
                }
                // If the next character is a number, then the number has not finished, so record it and continue
                else {
                    word += c;
                }
            }
            // Parsing the formula
            if (isMatchingNumberOfBrackets(word)) {  // If there aren't matching brackets, you can't parse
                // If they aren't contained, they might not be a full word
                if (getNumberOfBrackets(word) == 0)
                    if (Character.isUpperCase(c) || c == '(' || c == ')' || c == '*') { // At the end of a word
                        if (word.equals("")) {
                            word += c;
                            continue;
                        }
                        // Parse the word (without any brackets)
                        stringParts = word.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        // If the one is implied, like carbon in CO2, then assign the quantity as one
                        if (stringParts.length == 1) {
                            quantity = 1;
                        } else {
                            quantity = Integer.parseInt(stringParts[1]);
                        }
                        // Feed the symbol and quantity into the element map
                        // If the element has already been entered, increment the quantity
                        if (elementMap.containsKey(new Element(stringParts[0], Element.SYMBOL))) {
                            elementMap.put(new Element(stringParts[0], Element.SYMBOL),
                                    elementMap.get(new Element(stringParts[0], Element.SYMBOL)) + quantity);
                        }
                        // Else if the element hasn't already been entered, enter it with its current quantity
                        else {
                            elementMap.put(new Element(stringParts[0], Element.SYMBOL), quantity);
                        }
                        // Reset the word to be the next character
                        word = Character.toString(c);
                    } else { // The next character is part of this word
                        word += c;
                    }
                else { // Matching brackets, must be a block, so parse
                    // Recurse to parse the block in the brackets
                    receivedMap = parseFormula(word);
                    enteredResultYet  = false;
                    word = Character.toString(c);
                }
            } // Continue because the brackets don't match
            else {
                word += c;
            }
        }

        return elementMap;
    }

    private static int getNumberOfBrackets(String s) {
        return s.replaceAll("[^\\(\\)]", "").length();
    }

    private static boolean isMatchingNumberOfBrackets(String s) {
        return s.replaceAll("[^\\(]", "").length() == s.replaceAll("[^\\)]", "").length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Molecule molecule = (Molecule) o;

        for (HashMap.Entry<Element, Integer> elementEntry : this.elementMap.entrySet()) {
            Integer quantity = ((Molecule) o).getElementMap().get(elementEntry.getKey());
            if (quantity == null || !quantity.equals(elementEntry.getValue())) {
                return false;
            }
        }

        return Double.compare(molecule.getRelativeFormulaMass(), getRelativeFormulaMass()) == 0 &&
                getFormula().equals(molecule.getFormula());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFormula().hashCode();
        temp = Double.doubleToLongBits(getRelativeFormulaMass());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return getFormula();
    }

    public boolean contains(Element element) {
        return elementMap.containsKey(element);
    }

    public Set<Element> getElements() {
        return elementMap.keySet();
    }

    public int getElementQuantity(Element element) {
        return contains(element) ? elementMap.get(element) : 0;
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
