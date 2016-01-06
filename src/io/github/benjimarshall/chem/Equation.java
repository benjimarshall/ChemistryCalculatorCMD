package io.github.benjimarshall.chem;

import java.util.ArrayList;
import java.util.HashMap;

public class Equation {
    public Equation(String equation) throws NotationInterpretationException {
        // Clean out the whitespace
        equation = equation.replace(" ", "");
        // Simplify the equation's arrow
        equation = equation.replace("->", ">");

        System.out.println("Hi: " + equation);

        // Check that the equation is in a valid format
        // Add coefficients to numbers
        if (!equation.matches("(\\d*([\\(]?[A-Z][a-z]?\\d*(\\)\\d*)?)+)(\\+(\\d*([\\(]?[A-Z][a-z]?\\d*(\\)\\d*)?)+))*>" +
                "(\\d*([\\(]?[A-Z][a-z]?\\d*(\\)\\d*)?)+)(\\+(\\d*([\\(]?[A-Z][a-z]?\\d*(\\)\\d*)?)+))*")) {
            throw new NotationInterpretationException("Equation did not meet notation standards");
        }

        // Split the equation up by side
        String[] equationSides = equation.split(">");

        // Make the Map for each side with given values
        this.reactants = makeChemicalMap(equationSides[0]);
        this.products = makeChemicalMap(equationSides[1]);

        System.out.println("Is balanced: " + isBalanced(this.reactants, this.products));
    }

    private static HashMap<Molecule, Integer> makeChemicalMap(String equationSide) throws NotationInterpretationException {
        // The map of chemicals to be returned
        HashMap<Molecule, Integer> chemicals = new HashMap<>();
        // A temporary variable for each chemicals amount
        int quantity = -1;
        // A flag for when the coefficient has been collected or not
        boolean finishedNumber;
        // A string of the coefficient of the chemical, added to until the number has finished, to then be converted
        // to an int
        String stringQuantity;

        // Split the chemical list into individual chemicals
        for (String chemical: equationSide.split("\\+")) {
            // If the chemical has a coefficient
            if (chemical.matches("\\d+.*")){
                // Clean out the string which will be used to collect the coefficient's digit
                stringQuantity = "";
                finishedNumber = false;
                // Loop through the characters of the chemical until the number finishes
                for (Character c: chemical.toCharArray()) {
                    // If the character is a digit, then append it to the string for the number
                    if (Character.isDigit(c) && !finishedNumber) {
                        stringQuantity += c;
                    }
                    // If the number has finished, collect the rest of the characters to form the actual molecule
                    else if (finishedNumber) {
                        stringQuantity += c;
                    }
                    // If the character is not a digit, then the number has finished so stop looping though the chemical
                    else {
                        // Parse the integer collected from the beginning of the chemical's string
                        quantity = Integer.parseInt(stringQuantity);
                        // Start collecting the rest of the chemical's characters
                        stringQuantity = Character.toString(c);
                        finishedNumber = true;
                    }
                }
                // Put the molecule into the map
                chemicals.put(new Molecule(stringQuantity), quantity);
            }
            // If the chemical doesn't have a coefficient, assume that it is 1
            else {
                // Put the molecule into the map with an assumed quantity of 1
                chemicals.put(new Molecule(chemical), 1);
            }
        }
        return chemicals;
    }

    private static boolean isBalanced(HashMap<Molecule, Integer> reactants, HashMap<Molecule, Integer> products) {
        // Break out function so it makes an elementMap for each side
        try {
            HashMap<Element, Integer> reactantElementMap = makeElementMap(reactants);
            HashMap<Element, Integer> productElementMap = makeElementMap(products);

            if (reactantElementMap.size() != productElementMap.size()) {
                return false;
            }

            for (HashMap.Entry<Element, Integer> elementEntry : reactantElementMap.entrySet()) {
                if (!productElementMap.get(elementEntry.getKey()).equals(elementEntry.getValue())) {
                    return false;
                }
            }
        }
        catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private static HashMap<Element, Integer> makeElementMap(HashMap<Molecule, Integer> moleculeMap) {
        HashMap<Element, Integer> elementMap = new HashMap<>();
        // For every value in the reactants
        for (HashMap.Entry<Molecule, Integer> molecule: moleculeMap.entrySet()) {
            // Break the molecule up into individual elements
            for (HashMap.Entry<Element, Integer> elementEntry: molecule.getKey().getElementMap().entrySet()) {
                // If the element is already present, add to it
                if (elementMap.containsKey(elementEntry.getKey())) {
                    elementMap.put(elementEntry.getKey(),
                            elementMap.get(elementEntry.getKey())
                                    + (elementEntry.getValue() * molecule.getValue()));
                }
                // If the element is not present, make a new entry with the element and its quantity
                else {
                    elementMap.put(elementEntry.getKey(), elementEntry.getValue() * molecule.getValue());
                }
            }
        }

        return elementMap;
    }

    private ArrayList<Integer> balance(String equation) {
        return null;
    }

    private HashMap<Molecule, Integer> reactants = new HashMap<>();
    private HashMap<Molecule, Integer> products = new HashMap<>();
}
