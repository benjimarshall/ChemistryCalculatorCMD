package io.github.benjimarshall.chem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;

/**
 * Chemical Molecule. A {@code Molecule} object consists of its constituent {@code Element} objects, and their rations,
 * and its relative formula mass
 *
 * @author Benji Marshall
 * @since 2016-2-4
 */
public class Molecule {
    private Molecule() {

    }

    /**
     * Make a {@code Molecule} object using a string representation of a formula
     * @param formula written representation of the formula (eg. {@code HNO3}
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     */
    public Molecule(String formula) throws NotationInterpretationException {
        // A more explanatory error for finding symbol characters
        if (!formula.matches(ACCEPTED_LETTERS_AND_SYMBOLS_REGEX)) {
            throw new NotationInterpretationException("Formula contained non-letter, non-digit characters");
        }

        // Check to see if the formula is in a valid format
        if (!formula.matches(MOLECULE_REGEX)) {
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
            this.relativeFormulaMass = BigDecimal.ZERO;
            for (HashMap.Entry<Element, Integer> entry : this.elementMap.entrySet()) {
                this.relativeFormulaMass = this.relativeFormulaMass.add(entry.getKey().getMassNumber().multiply(
                        new BigDecimal(BigInteger.valueOf(entry.getValue()))));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new NotationInterpretationException("Capital letter expected after number");
        }
    }

    /**
     * Parses a formula string into a map of {@code Element} objects
     * @param formula formula string to parse into a map of {@code Element} objects
     * @return the map of {@code Element} objects
     * @throws NotationInterpretationException when the formula cannot be interpreted, such as an invalid
     *         {@code Element}
     * @see #formula
     * @see #elementMap
     */
    private static HashMap<Element, Integer> parseFormula(String formula) throws NotationInterpretationException {
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
        String word = formula.substring(0, 1);
        // Iterate over each character, skipping the first character which is already in the word variable
        try {
            for (Character c : formula.substring(1).toCharArray()) {
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
                        for (HashMap.Entry<Element, Integer> entry : receivedMap.entrySet()) {
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
                        enteredResultYet = false;
                        word = Character.toString(c);
                    }
                } // Continue because the brackets don't match
                else {
                    word += c;
                }
            }


        } catch (FlagException e) {
            System.out.println("An unexpected (and probably impossible) flag exception occurred: " + e.getMessage());
        }
        return elementMap;
    }

    /**
     * Find the number of sets of brackets in a string
     * @param s the string to count the number of brackets in
     * @return the number of sets of brackets in the parameter
     */
    private static int getNumberOfBrackets(String s) {
        return s.replaceAll("[^\\(\\)]", "").length();
    }

    /**
     * Checks whether there are an equal number of closing and opening brackets in a string
     * @param s the string to compare the number of opening and closing brackets in
     * @return whether there are an equal number of closing and opening brackets in a string
     */
    private static boolean isMatchingNumberOfBrackets(String s) {
        return s.replaceAll("[^\\(]", "").length() == s.replaceAll("[^\\)]", "").length();
    }

    /**
     * A custom equals method, to compare elements
     * @param o the object to compare to this {@code Molecule} object
     * @return whether the two {@code Molecule} objects are equal
     */
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

        return molecule.getRelativeFormulaMass().compareTo(getRelativeFormulaMass()) == 0 &&
                getFormula().equals(molecule.getFormula());
    }

    /**
     * Generates the hash code of this {@code Molecule} object
     * @return the hash code of this {@code Molecule} object
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFormula().hashCode();
        temp = Double.doubleToLongBits(getRelativeFormulaMass().doubleValue());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Gets the {@code Molecule} object's {@link #formula}
     * @return the {@code Molecule} object's {@link #formula}
     */
    @Override
    public String toString() {
        return getFormula();
    }

    /**
     * Checks whether the {@code Molecule} object contains a given {@code Element} object
     * @param element the {@code Element} object to see if this {@code Molecule} object contains
     * @return whether the {@code Molecule} object contains the given {@code Element} object
     */
    public boolean contains(Element element) {
        return elementMap.containsKey(element);
    }

    /**
     * Gets the {@code Molecule} object's set of {@code Element} objects from the {@link #elementMap}
     * @return the {@code Molecule} object's set of {@code Element} objects from the {@link #elementMap}
     */
    public Set<Element> getElements() {
        return elementMap.keySet();
    }

    /**
     * Gets quantity of a given {@code Element} object in the {@code Molecule} object in the {@link #elementMap}
     * @param element the {@code Element} object find the quantity of in this {@code Molecule} object
     * @return quantity of a given {@code Element} object in the {@code Molecule} object in the {@link #elementMap}
     */
    public int getElementQuantity(Element element) {
        return contains(element) ? elementMap.get(element) : 0;
    }

    /**
     * Gets the {@link #formula} of the {@code Molecule} object
     * @return the {@link #formula} of the {@code Molecule} object
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Gets the {@link #elementMap} of the {@code Molecule} object
     * @return the {@link #elementMap} of the {@code Molecule} object
     */
    public HashMap<Element, Integer> getElementMap() {
        return elementMap;
    }

    /**
     * Gets the {@link #relativeFormulaMass} of the {@code Molecule} object
     * @return the {@link #relativeFormulaMass} of the {@code Molecule} object
     */
    public BigDecimal getRelativeFormulaMass() {
        return relativeFormulaMass;
    }

    /**
     * A regex pattern of the accepted letters and symbols in the {@code String} representation a {@code Molecule}
     * object.
     * @see java.util.regex.Pattern
     */
    public static final String ACCEPTED_LETTERS_AND_SYMBOLS_REGEX = "[a-zA-Z0-9\\(\\)]*";

    /**
     * A regex pattern of the accepted format of the {@code String} representation a {@code Molecule} object.
     * @see java.util.regex.Pattern
     */
    public static final String MOLECULE_REGEX = "([\\(]?" + Element.ELEMENT_REGEX + "\\d*(\\)\\d*)?)+";

    /**
     * The {@code Molecule} object's formula. For example: {@code HNO3}
     */
    protected String formula;

    /**
     * The {@code Molecule} object's {@link HashMap} of elements. For example:
     * <code>{H=1, N=1, O=3}</code>
     */
    protected HashMap<Element, Integer> elementMap = new HashMap<>();

    /**
     * The {@code Molecule} object's relative formula mass. For example: {@code HNO3} has a relative formula mass of
     * {@code 63.0}
     */
    protected BigDecimal relativeFormulaMass;
}

