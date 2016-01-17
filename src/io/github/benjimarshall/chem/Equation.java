package io.github.benjimarshall.chem;

import org.apache.commons.lang3.math.Fraction;

import java.util.*;

public class Equation {
    public Equation(String equation) throws NotationInterpretationException {
        // Clean out the whitespace
        equation = equation.replace(" ", "");
        // Simplify the equation's arrow
        equation = equation.replace("->", ">");

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


        if (!isElementsPresent(reactants, products)) {
            throw new NotationInterpretationException("There are different elements on either side of the reaction");
        }

        balance(this.reactants, this.products);

        StringBuilder equationBuilder = new StringBuilder();

        for (HashMap.Entry<Molecule, Integer> reactant: this.reactants.entrySet()) {
            String coefficient = Integer.toString(reactant.getValue());
            if (coefficient.equals("0")) {
                continue;
            }
            else if (coefficient.equals("1")) {
                coefficient = "";
            }
            equationBuilder.append(coefficient).append("").append(reactant.getKey()).append(" + ");
        }

        equationBuilder.replace(equationBuilder.length() - 3, equationBuilder.length(), " -> ");

        for (HashMap.Entry<Molecule, Integer> product: this.products.entrySet()) {
            String coefficient = Integer.toString(product.getValue());
            if (coefficient.equals("0")) {
                continue;
            }
            else if (coefficient.equals("1")) {
                coefficient = "";
            }
            equationBuilder.append(coefficient).append("").append(product.getKey()).append(" + ");
        }

        equationBuilder.replace(equationBuilder.length() - 3, equationBuilder.length(), "");
        this.equation = equationBuilder.toString();
    }

    private static HashMap<Molecule, Integer> makeChemicalMap(String equationSide)
            throws NotationInterpretationException {
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

    private static boolean isElementsPresent(HashMap<Molecule, Integer> reactants, HashMap<Molecule, Integer> products) {
        HashMap<Element, Integer> reactantElementMap = makeElementMap(reactants);
        HashMap<Element, Integer> productElementMap = makeElementMap(products);

        if (reactantElementMap.size() != productElementMap.size()) {
            return false;
        }

        for (HashMap.Entry<Element, Integer> reactant: reactantElementMap.entrySet()) {
            if (!productElementMap.containsKey(reactant.getKey())) {
                return false;
            }
        }

        return true;
    }

    private static boolean isBalanced(HashMap<Molecule, Integer> reactants, HashMap<Molecule, Integer> products) {
        // Make a map of elements and quantities for each side of the reaction
        HashMap<Element, Integer> reactantElementMap = makeElementMap(reactants);
        HashMap<Element, Integer> productElementMap = makeElementMap(products);

        // Iterate over each element and see if the quantities are the same on both sides
        for (HashMap.Entry<Element, Integer> elementEntry : reactantElementMap.entrySet()) {
            if (!productElementMap.get(elementEntry.getKey()).equals(elementEntry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private static HashMap<Element, Integer> makeElementMap(HashMap<Molecule, Integer> moleculeMap) {
        HashMap<Element, Integer> elementMap = new HashMap<>();
        // For every value in the reactants
        for (HashMap.Entry<Molecule, Integer> molecule: moleculeMap.entrySet()) {
            // Break the molecule up into individual elements
            for (Element element: molecule.getKey().getElements()) {
                // If the element is already present, add to it
                if (elementMap.containsKey(element)) {
                    elementMap.put(element,
                            elementMap.get(element)
                                    + (molecule.getKey().getElementQuantity(element) * molecule.getValue()));
                }
                // If the element is not present, make a new entry with the element and its quantity
                else {
                    elementMap.put(element, molecule.getKey().getElementQuantity(element) * molecule.getValue());
                }
            }
        }

        return elementMap;
    }

    private void balance(HashMap<Molecule, Integer> reactants, HashMap<Molecule, Integer> products)
            throws NotationInterpretationException {
        Set<Element> elementSet = makeElementMap(reactants).keySet();

        // Make maps of the coefficients, for molecules and for the fractional value of the coefficients
        HashMap<String, Molecule> moleculeCoefficients = new HashMap<>();
        HashMap<String, Fraction> coefficientFractionValue = new HashMap<>();

        // A coefficient name generator
        AlphabeticSequence as = new AlphabeticSequence();
        // A temporary variable because each name can only be called once
        String coefficientString;

        // Generate the maps for the reactants
        for (HashMap.Entry<Molecule, Integer> moleculeEntry: reactants.entrySet()) {
            coefficientString = as.next();
            moleculeCoefficients.put(coefficientString, moleculeEntry.getKey());
            coefficientFractionValue.put(coefficientString, Fraction.getFraction(0));
        }

        String middle = as.next();

        // Continue to generate the maps now using the products
        for (HashMap.Entry<Molecule, Integer> moleculeEntry: products.entrySet()) {
            coefficientString = as.next();
            moleculeCoefficients.put(coefficientString, moleculeEntry.getKey());
            coefficientFractionValue.put(coefficientString, Fraction.getFraction(0));
        }

        // Generate the algebraic equations
        ArrayList<AlgebraicEquation> algEquations = new ArrayList<>();
        String equation;
        // Generate the equation
        for (Element element: elementSet) {
            equation = "";
            // For each reactant
            for (HashMap.Entry<Molecule, Integer> moleculeEntry: reactants.entrySet()) {
                // If the element is present in this molecule
                if (moleculeEntry.getKey().contains(element)) {
                    // Get the variable name of the molecule
                    for (HashMap.Entry<String, Molecule> coefficientEntry: moleculeCoefficients.entrySet()) {
                        // Check if the molecule in the coefficient entry is the molecule being searched for
                        if (coefficientEntry.getKey().compareTo(middle) < 0 &&
                               coefficientEntry.getValue().equals(moleculeEntry.getKey())) {
                           // Put the coefficient and name into the equation

                           // Don't append a plus if there are no other variables yet
                           if (equation.length() != 0) {
                               equation += "+";
                           }

                           // Append the variable and its coefficient to the equation
                           equation += moleculeEntry.getKey().getElementQuantity(element) + coefficientEntry.getKey();
                           break;
                        }
                    }
                }
            }

            equation += "=";

            // For each product
            for (HashMap.Entry<Molecule, Integer> moleculeEntry: products.entrySet()) {
                // If the element is present in this molecule
                if (moleculeEntry.getKey().contains(element)) {
                    // Get the variable name of the molecule
                    for (HashMap.Entry<String, Molecule> coefficientEntry: moleculeCoefficients.entrySet()) {
                        // Check if the molecule in the coefficient entry is the molecule being searched for
                        if (coefficientEntry.getKey().compareTo(middle) > 0 &&
                                coefficientEntry.getValue().equals(moleculeEntry.getKey())) {
                            // Put the coefficient and name into the equation

                            // Don't add a plus if the last character was equals
                            if (equation.charAt(equation.length() - 1) != '=') {
                                equation += "+";
                            }

                            // Append the variable and its coefficient to the equation
                            equation += moleculeEntry.getKey().getElementQuantity(element) + coefficientEntry.getKey();
                            break;
                        }
                    }
                }
            }

            algEquations.add(new AlgebraicEquation(equation, AlgebraicEquation.WARN_ON_MULTIPLE_OCCURRENCES));
        }

        ArrayList<String> balancedVariables;

        // Find a suitable equation to start setting values
        HashMap<String, Fraction> copyOfCoefficentFractionValue =
                (HashMap<String, Fraction>) coefficientFractionValue.clone();
        boolean finished = false;

        // Balance the equation, loop through equations to find a starting equation:
        for (AlgebraicEquation startingEq: algEquations) {
            // Reset the coefficients to be 0, and empty the list of known terms
            coefficientFractionValue = (HashMap<String, Fraction>) copyOfCoefficentFractionValue.clone();
            balancedVariables = new ArrayList<>();
            // If the equation doesn't have two terms, it is really not very nice to set variables for
            if (!startingEq.hasTwoTerms()) {
                continue;
            }

            // Grab one of the variables and let it be one
            coefficientFractionValue.put(startingEq.getTerms().get(0), Fraction.getFraction(1));
            balancedVariables.add(startingEq.getTerms().get(0));
            // Solve an SSEq to find out the other value
            startingEq.solveSimpleSubstitution(coefficientFractionValue, balancedVariables);

            balancedVariables.add(startingEq.getTerms().get(1));
            finished = attemptToSolveChemEquation(algEquations, balancedVariables, coefficientFractionValue);

            if (finished) {
                break;
            }
        }

        // Did the equation successfully balance?
        if (finished) {
            ArrayList<HashMap<Molecule, Integer>> sides = coefficientsToEquation(moleculeCoefficients,
                    AlgebraicEquation.simplifyCoefficients(coefficientFractionValue));

            for (HashMap.Entry<Molecule, Integer> mol : sides.get(0).entrySet()) {
                this.reactants.put(mol.getKey(), mol.getValue());
            }
            for (HashMap.Entry<Molecule, Integer> mol : sides.get(1).entrySet()) {
                this.products.put(mol.getKey(), mol.getValue());
            }
        }
        // If it didn't, throw an error
        else {
            throw new NotationInterpretationException("Equation could not be balanced");
        }
    }

    private boolean attemptToSolveChemEquation (ArrayList<AlgebraicEquation> algEquations,
                                                ArrayList<String> balancedVariables,
                                                HashMap<String, Fraction> coefficientFractionValue) {
        boolean doneSomethingThisTime = true;
        boolean finished = false;
        ArrayList<AlgebraicEquation> foundSimultaneousEquations;
        while (doneSomethingThisTime && !finished) {
            doneSomethingThisTime = false;
            foundSimultaneousEquations  = new ArrayList<>();

            // Try to deduce values from each equation
            for (AlgebraicEquation alEq : algEquations) {
                // If there are two unknown variables, it could be solved by simultaneous equations so note it
                if (alEq.getUnknownTerms(balancedVariables).size() == 2) {
                    foundSimultaneousEquations.add(alEq);
                }
                // If one term is known, it can be solved by simple substitution
                else if (alEq.getUnknownTerms(balancedVariables).size() == 1) {
                    alEq.solveSimpleSubstitution(coefficientFractionValue, balancedVariables);
                    balancedVariables.add(alEq.getUnknownTerms(balancedVariables).get(0));
                    doneSomethingThisTime = true;
                }
                // Else if all terms have been found, or there are more than 2 unknown terms, do nothingt
            }

            // If all of the values of the variables have been found, the process has finished
            if (balancedVariables.size() == this.reactants.size() + this.products.size()) {
                finished = true;
            }

            boolean doneSimultaneousSolving = false;

            // If some simultaneous equations have been found, solve them
            if (!doneSomethingThisTime && foundSimultaneousEquations.size() >=2 ) {
                for (AlgebraicEquation eq1: foundSimultaneousEquations) {
                    for (AlgebraicEquation eq2 : foundSimultaneousEquations) {
                        // If they share terms (and aren't the same equation!), continue
                        if (eq1 == eq2 || !eq1.getUnknownTerms(balancedVariables).equals(
                                eq2.getUnknownTerms(balancedVariables))) {
                            continue;
                        }
                        try {
                            eq1.solveSimultaneousEquations(eq2, balancedVariables, coefficientFractionValue);
                            doneSimultaneousSolving = true;
                            break;
                        }
                        // If the equations were equivalent: give up and try again
                        catch (ArithmeticException ignored) {
                        }
                    }

                    // Break out of the outer loop as well if simultaneous equations have been solved
                    if (doneSimultaneousSolving) {
                        doneSomethingThisTime = true;
                        break;
                    }
                }
            }

        }
        // Return whether the attempt has been successful
        return finished;
    }

    private ArrayList<HashMap<Molecule, Integer>> coefficientsToEquation(HashMap<String, Molecule> moleculeCoefficients,
                                                                        HashMap<String, Integer> coefficientValues) {
        ArrayList<HashMap<Molecule, Integer>> sides = new ArrayList<>();
        sides.add(new HashMap<>());

        // Add determine each coefficient of the reactants
        for (Molecule mol: this.reactants.keySet()) {
            // Find the value, searching for the molecule's String name, and using it to call its value
            for (HashMap.Entry<String, Molecule> molEntry: moleculeCoefficients.entrySet()) {
                if (mol.equals(molEntry.getValue())) {
                    sides.get(0).put(mol, coefficientValues.get(molEntry.getKey()));
                    break;
                }
            }
        }

        sides.add(new HashMap<>());

        // Add determine each coefficient of the reactants
        for (Molecule mol: this.products.keySet()) {
            // Find the value, searching for the molecule's String name, and using it to call its value
            for (HashMap.Entry<String, Molecule> molEntry: moleculeCoefficients.entrySet()) {
                if (mol.equals(molEntry.getValue())) {
                    sides.get(1).put(mol, coefficientValues.get(molEntry.getKey()));
                    break;
                }
            }
        }

        return sides;
    }

    @Override
    public String toString() {
        return this.getEquation();
    }

    public HashMap<Molecule, Integer> getReactants() {
        return reactants;
    }

    public HashMap<Molecule, Integer> getProducts() {
        return products;
    }

    public String getEquation() {
        return equation;
    }

    private String equation;
    private HashMap<Molecule, Integer> reactants = new HashMap<>();
    private HashMap<Molecule, Integer> products = new HashMap<>();
}
