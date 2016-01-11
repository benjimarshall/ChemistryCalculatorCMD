package io.github.benjimarshall.chem;

import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;

public class AlgebraicEquation {
    public AlgebraicEquation(String equation) throws NotationInterpretationException {
        this(equation, DO_NOT_WARN_ON_MULTIPLE_OCCURRENCES);
    }

    public AlgebraicEquation(String equation, boolean flag) throws NotationInterpretationException {
        // Clean out the whitespace
        equation = equation.replace(" ", "");

        if (!equation.matches("\\d*[A-Z]+(\\+\\d*[A-Z]+)*=\\d*[A-Z]+(\\+\\d*[A-Z]+)*")) {
            throw new NotationInterpretationException("Algebraic equation didn't meet notation standards");
        }

        String[] sides = equation.split("=");
        this.firstSide = makeSideMap(sides[0], flag);
        this.secondSide = makeSideMap(sides[1], flag);
    }

    private HashMap<String, Integer> makeSideMap(String side) throws NotationInterpretationException {
        return makeSideMap(side, DO_NOT_WARN_ON_MULTIPLE_OCCURRENCES);
    }

    private HashMap<String, Integer> makeSideMap(String side, boolean flag) throws NotationInterpretationException {
        // A map of all the variables one side of the equation
        HashMap<String, Integer> sideMap = new HashMap<>();

        // Strings to to collect the coefficient and variable name from a given term
        String coefficientString;
        String varString;

        // For each term on a side
        for (String term: side.split("\\+")) {
            // Reset the strings to collect the coefficient and variable name from the given term
            coefficientString = "";
            varString = "";

            // Separate out the term into coefficient and variable
            for (Character c: term.toCharArray()) {
                // If it is a digit, add it to the coefficient string
                if (Character.isDigit(c)) {
                    coefficientString += c;
                }
                // If it is a letter, add it to the variable name string
                else if (Character.isAlphabetic(c)) {
                    varString += c;
                }
            }

            // Throw an error if a variable appears twice on one side. This is not bad algebra but will happen at the
            // user's prompting, such as in the Equation class
            if (flag && sideMap.containsKey(varString)) {
                throw new NotationInterpretationException("Variable appeared twice in one side");
            }

            // Record the variable name
            this.terms.add(varString);

            if (coefficientString.equals("")) {
                coefficientString = "1";
            }

            // Add the coefficient and variable name to the map
            sideMap.put(varString, Integer.parseInt(coefficientString));
        }
        return sideMap;
    }

    public boolean hasTwoTerms() {
        return this.terms.size() == 2;
    }

    public boolean isSolvableBySimpleSubstitution(ArrayList<String> knownTerms) {
        return getNumberOfUnknownTerms(knownTerms) <= 1;
    }

    public boolean areAllTermsKnown(ArrayList<String> knownTerms) {
        return getNumberOfUnknownTerms(knownTerms) == 0;
    }

    public ArrayList<String> findUnknownTerms(ArrayList<String> knownTerms) {
        ArrayList<String> unknownTerms = new ArrayList<>();
        for (String term: terms) {
            if (!knownTerms.contains(term)) {
                unknownTerms.add(term);
            }
        }
        return unknownTerms;
    }

    private int getNumberOfUnknownTerms(ArrayList<String> knownTerms) {
        int unknownTerms = 0;
        // Loop through each term
        for (String term: this.terms) {
            // If the term is not known, increment the number of unknown terms
            if (!knownTerms.contains(term)) {
                unknownTerms += 1;
            }
        }
        return unknownTerms;
    }

    public static HashMap<String, Integer> simplifyCoefficients(HashMap<String, Fraction> coefficientFractionValues) {
        HashMap<String, Integer> simplifiedValues = new HashMap<>();
        ArrayList<Integer> denominators = new ArrayList<>();
        // Make integers the fractions integers, so get their denominators
        for (Fraction f: coefficientFractionValues.values()) {
            denominators.add(f.getDenominator());
        }
        // Find the multiplier to make all fractions integers
        Fraction multiplier = Fraction.getFraction(lcm(denominators));
        // Multiply all the fractions by the multiplier to make the map of integers
        for (HashMap.Entry<String, Fraction> fractionVariable: coefficientFractionValues.entrySet()) {
            simplifiedValues.put(fractionVariable.getKey(),
                    fractionVariable.getValue().multiplyBy(multiplier).intValue());
        }

        // Simplify the integers, if possible
        int divisor = gcd(new ArrayList<Integer>(simplifiedValues.values()));
        for (HashMap.Entry<String, Integer> intVariable: simplifiedValues.entrySet()) {
            simplifiedValues.put(intVariable.getKey(), intVariable.getValue() / divisor);
        }

        return simplifiedValues;
    }

    public static int gcd(ArrayList<Integer> values) {
        int value = values.get(0);
        for (int i: values) {
            value = gcd(i, value);
        }

        return value;
    }

    public static int gcd(int value1, int value2) {
        int r;
        if (value2 > value1) {
            r = value2;
            value2 = value1;
            value1 = r;
        }
        while (value2 != 0) {
            r = value1 % value2;
            value1 = value2;
            value2 = r;
        }

        return value1;
    }

    public static int lcm(int value1, int value2) {
        return (value1 * value2) / gcd(value1, value2);
    }

    public static int lcm(ArrayList<Integer> values) {
        int value = values.get(0);
        for (int i: values) {
            value = lcm(i, value);
        }

        return value;
    }

    private static boolean areAllFractionsIntegers(HashMap<String, Fraction> coefficientFractionValues) {
        for (Fraction fraction: coefficientFractionValues.values()) {
            if (fraction.getDenominator() != 1) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public HashMap<String, Integer> getFirstSide() {
        return firstSide;
    }

    public HashMap<String, Integer> getSecondSide() {
        return secondSide;
    }

    public Fraction solveSimpleSubstitution(HashMap<String, Fraction> variables, ArrayList<String> knownTerms) {
        Fraction[] sides = new Fraction[]{Fraction.getFraction(0), Fraction.getFraction(0)};
        Fraction value;
        boolean isUnknownOnFirstSide = false;
        String unknownTerm = "";

        // Work out the value of the first side
        // Iterate over every term
        for (HashMap.Entry<String, Integer> term: this.firstSide.entrySet()) {
            // If the value is known, add it to the side, after multiplying it by its coefficient
            if (knownTerms.contains(term.getKey())) {
                sides[0] = sides[0].add(variables.get(term.getKey()) // The value of the variable
                        .multiplyBy(Fraction.getFraction(term.getValue()))); // Multiplied by coefficient
            }
            // If the value is unknown, the unknown is on the left side
            else {
                isUnknownOnFirstSide = true;
                unknownTerm = term.getKey();
            }
        }

        // Work out the value of the second side
        // Iterate over every term
        for (HashMap.Entry<String, Integer> term: this.secondSide.entrySet()) {
            // If the value is known, add it to the side, after multiplying it by its coefficient
            if (knownTerms.contains(term.getKey())) {
                sides[1] = sides[1].add(variables.get(term.getKey()) // The value of the variable
                        .multiplyBy(Fraction.getFraction(term.getValue()))); // Multiplied by coefficient
            }
            else {
                isUnknownOnFirstSide = false;
                unknownTerm = term.getKey();
            }
        }

        // Subtract so that x = const, and then divide by the coefficient of x
        // If x is on the first side
        if (isUnknownOnFirstSide) {
            // Subtract the first side from, the second
            value = sides[1].subtract(sides[0]);
            // Divide by the coefficient of x
            value = value.divideBy(Fraction.getFraction(firstSide.get(unknownTerm)));
        }
        // Else x is on the second side
        else {
            // Subtract the second side from, the first
            value = sides[0].subtract(sides[1]);
            // Divide by the coefficient of x
            value = value.divideBy(Fraction.getFraction(secondSide.get(unknownTerm)));
        }

        return value;
    }

    private ArrayList<String> terms = new ArrayList<>();
    private HashMap<String, Integer> firstSide = new HashMap<>();
    private HashMap<String, Integer> secondSide = new HashMap<>();

    public static final boolean WARN_ON_MULTIPLE_OCCURRENCES = true;
    public static final boolean DO_NOT_WARN_ON_MULTIPLE_OCCURRENCES = false;
}
