package io.github.benjimarshall.chem;

import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Algebraic Equation. A {@code AlgebraicEquation} object consists of a variable names and their coefficients, #
 * for each side.
 *
 * <p>WARNING: THIS CLASS WAS DESIGNED FOR {@link Equation} BALANCING, SO IT ONLY WORKS WITH VARIABLES BEING
 * ADDED ON EITHER SIDE OF THE EQUATION, WITH OPTIONAL INTEGER COEFFICIENTS OF THE VARIABLE. THERE IS NO PROVISION FOR
 * SUBTRACTION, MULTIPLICATION, DIVISION, OR NON-VARIABLE VALUES.</p>
 *
 * @author Benji Marshall
 * @since 2016-2-2
 * @see Equation
 */
public class AlgebraicEquation {
    /**
     * Constructs an {@code AlgebraicEquation} object, presuming that warning on multiple occurrences is not necessary,
     *         from a {@code String} representation of the equation. For example: {@code A + 2B = 4C + 3D}
     * @param equation the {@code String} representation of the equation
     * @throws NotationInterpretationException when the {@code equation} cannot be interpreted
     */
    public AlgebraicEquation(String equation) throws NotationInterpretationException {
        this(equation, DO_NOT_WARN_ON_MULTIPLE_OCCURRENCES);
    }

    /**
     * Constructs an {@code AlgebraicEquation} object, presuming that warning on multiple occurrences is necessary, with
     *         a flag from saying whether to throw a {@code NotationInterpretationException} exception on multiple
     *         occurrences of a variable a {@code String} representation of the equation.
     *         For example: {@code A + 2B = 4C + 3D}
     * @param equation the {@code String} representation of the equation
     * @param flag m whether to throw a {@code NotationInterpretationException} exception on multiple occurrences of a
     *         variable, see {@link #WARN_ON_MULTIPLE_OCCURRENCES} and {@link #DO_NOT_WARN_ON_MULTIPLE_OCCURRENCES}
     * @throws NotationInterpretationException when the {@code equation} cannot be interpreted, or if there are multiple
     *         occurrences of a variable and it has been asked to throw errors if multiple occurrences are found
     */
    public AlgebraicEquation(String equation, boolean flag) throws NotationInterpretationException {
        // Clean out the whitespace
        equation = equation.replace(" ", "");

        if (!equation.matches("\\d*[A-Z]+(\\+\\d*[A-Z]+)*=\\d*[A-Z]+(\\+\\d*[A-Z]+)*")) {
            throw new NotationInterpretationException("Algebraic equation didn't meet notation standards");
        }

        String[] sides = equation.split("=");
        this.firstSide = makeSideMap(sides[0], flag);
        this.secondSide = makeSideMap(sides[1], flag);
        this.equation = equation;
    }

    private HashMap<String, Integer> makeSideMap(String side, boolean flag) throws NotationInterpretationException {
        // A map of all the variables one side of the equation
        HashMap<String, Integer> sideMap = new HashMap<>();

        // Strings to to collect the coefficient and variable name from a given term
        String coefficientString;
        String varString;

        // For each term on a side
        for (String term : side.split("\\+")) {
            // Reset the strings to collect the coefficient and variable name from the given term
            coefficientString = "";
            varString = "";

            // Separate out the term into coefficient and variable
            for (Character c : term.toCharArray()) {
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

    /**
     * Finds whether the {@code AlgebraicEquation} object has two terms
     * @return whether the {@code AlgebraicEquation} object has two terms
     */
    public boolean hasTwoTerms() {
        return this.terms.size() == 2;
    }

    /**
     * Finds whether the {@code AlgebraicEquation} object is solvable by simple substitution, with a Collection of
     * known terms
     * @param knownTerms a list of which terms are already known
     * @return whether the {@code AlgebraicEquation} object is is solvable by simple substitution
     * @see #getNumberOfUnknownTerms(Collection)
     */
    public boolean isSolvableBySimpleSubstitution(Collection<String> knownTerms) {
        return getNumberOfUnknownTerms(knownTerms) <= 1;
    }

    /**
     * Finds whether all of the terms of the {@code AlgebraicEquation} object are known, if a passed Collection of terms
     * are known
     * @param knownTerms a list of which terms are already known
     * @return the number of the terms in the {@code AlgebraicEquation} object that are known
     * @see #getNumberOfUnknownTerms(Collection)
     */
    public boolean areAllTermsKnown(ArrayList<String> knownTerms) {
        return getNumberOfUnknownTerms(knownTerms) == 0;
    }

    /**
     * Finds which terms are known in the {@code AlgebraicEquation} object with a passed Collection of known terms
     * @param knownTerms a list of which terms are already known
     * @return a Collection of unknown terms
     */
    public List<String> getUnknownTerms(Collection<String> knownTerms) {
        ArrayList<String> unknownTerms = new ArrayList<>();
        for (String term : terms) {
            if (!knownTerms.contains(term)) {
                unknownTerms.add(term);
            }
        }
        return unknownTerms;
    }

    /**
     * Finds the number of unknown terms in the {@code AlgebraicEquation} object are known, if a passed Collection of
     * terms are known
     * @param knownTerms a list of which terms are already known
     * @return whether all of the terms of the {@code AlgebraicEquation} object are known
     */
    public int getNumberOfUnknownTerms(Collection<String> knownTerms) {
        int unknownTerms = 0;
        // Loop through each term
        for (String term : this.terms) {
            // If the term is not known, increment the number of unknown terms
            if (!knownTerms.contains(term)) {
                unknownTerms += 1;
            }
        }
        return unknownTerms;
    }

    /**
     * Takes a Map of terms, and simplifies their coefficients, For example: <code>{2, 10, 14, 6}</code> would simplify
     * to <code>{1, 5, 7, 3}</code>
     * @param coefficientFractionValues the Map of terms to be simplified
     * @return a Map of simplified terms
     */
    public static Map<String, Integer> simplifyCoefficients(Map<String, Fraction> coefficientFractionValues) {
        HashMap<String, Integer> simplifiedValues = new HashMap<>();
        ArrayList<Integer> denominators = new ArrayList<>();
        // Make integers the fractions integers, so get their denominators
        for (Fraction f : coefficientFractionValues.values()) {
            denominators.add(f.getDenominator());
        }
        // Find the multiplier to make all fractions integers
        Fraction multiplier = Fraction.getFraction(lcm(denominators));
        // Multiply all the fractions by the multiplier to make the map of integers
        for (HashMap.Entry<String, Fraction> fractionVariable : coefficientFractionValues.entrySet()) {
            simplifiedValues.put(fractionVariable.getKey(),
                    fractionVariable.getValue().multiplyBy(multiplier).intValue());
        }

        // Simplify the integers, if possible
        int divisor = gcd(new ArrayList<>(simplifiedValues.values()));
        for (HashMap.Entry<String, Integer> intVariable : simplifiedValues.entrySet()) {
            simplifiedValues.put(intVariable.getKey(), intVariable.getValue() / divisor);
        }

        return simplifiedValues;
    }

    /**
     * Find the Greatest Common Divisor of two integers
     * @param value1 the the first integer
     * @param value2 the the second integer
     * @return the Greatest Common Divisor
     * @see #gcd(int, int)
     */
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

    /**
     * Find the Greatest Common Divisor of a List of integers
     * @param values the list of values
     * @return the Greatest Common Divisor
     */
    public static int gcd(List<Integer> values) {
        int value = values.get(0);
        for (int i : values) {
            value = gcd(i, value);
        }

        return value;
    }

    /**
     * Find the Lowest Common Multiple of two integers
     * @param value1 the the first integer
     * @param value2 the the second integer
     * @return the Lowest Common Multiple
     * @see #gcd(int, int)
     */
    public static int lcm(int value1, int value2) {
        return (value1 * value2) / gcd(value1, value2);
    }

    /**
     * Find the Lowest Common Multiple of a List of integers
     * @param values the list of values
     * @return the Lowest Common Multiple
     * @see #lcm(int, int)
     */
    public static int lcm(List<Integer> values) {
        int value = values.get(0);
        for (int i : values) {
            value = lcm(i, value);
        }

        return value;
    }

    /**
     * Solve this equation using simple substitution with a list of known variables, and a HashMap of their values,
     * to be modified to include the solved value
     * @param variables a HashMap of their values, to be modified to include the solved value
     * @param knownTerms a list of known variables
     */
    public void solveSimpleSubstitution(HashMap<String, Fraction> variables, List<String> knownTerms) {
        Fraction[] sides = new Fraction[]{Fraction.getFraction(0), Fraction.getFraction(0)};
        Fraction value;
        boolean isUnknownOnFirstSide = false;
        String unknownTerm = "";

        // Work out the value of the first side
        // Iterate over every term
        for (HashMap.Entry<String, Integer> term : this.firstSide.entrySet()) {
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
        for (HashMap.Entry<String, Integer> term : this.secondSide.entrySet()) {
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

        variables.put(unknownTerm, value);
    }

    /**
     * Solve simultaneous equations between this {@code AlgebraicEquation} object and another {@code AlgebraicEquation}
     * object, with a list of known variables, and a HashMap of their values, to be modified to include the solved
     * values
     * @param secondEquation a second {@code AlgebraicEquation} object to balance with {@code this} object
     * @param knownTerms a list of known variables
     * @param variables a HashMap of their values, to be modified to include the solved values
     */
    public void solveSimultaneousEquations(AlgebraicEquation secondEquation, List<String> knownTerms,
                                           HashMap<String, Fraction> variables) {
        // Put all the equation to one side
        HashMap<String, Fraction> firstEq = putTermsToOneSide(this, knownTerms);
        HashMap<String, Fraction> secondEq = putTermsToOneSide(secondEquation, knownTerms);


        // Choose a variable to substitute out
        String substitutedVariable = this.getUnknownTerms(knownTerms).get(0);
        String targetVariable = this.getUnknownTerms(knownTerms).get(1);

        // Rearrange so that it is on the other side
        firstEq.put(substitutedVariable, firstEq.get(substitutedVariable).multiplyBy(
                Fraction.getFraction(-1)));
        secondEq.put(substitutedVariable, secondEq.get(substitutedVariable).multiplyBy(
                Fraction.getFraction(-1)));

        // Make the coefficient of the chosen variable for both equations be one

        // For the first equation
        // Find the coefficient of the chosen variable
        Fraction coefficient = Fraction.getFraction(firstEq.get(substitutedVariable).getNumerator(),
                firstEq.get(substitutedVariable).getDenominator());

        // Divide all other coefficients by the chosen variable's coefficient (so the chosen variable's becomes 1)
        for (HashMap.Entry<String, Fraction> term : firstEq.entrySet()) {
            firstEq.put(term.getKey(), term.getValue().divideBy(coefficient));
        }

        // For the second equation
        // Find the coefficient of the chosen variable
        coefficient = Fraction.getFraction(secondEq.get(substitutedVariable).getNumerator(),
                secondEq.get(substitutedVariable).getDenominator());

        // Divide all other coefficients by the chosen variable's coefficient (so the chosen variable's becomes 1)
        for (HashMap.Entry<String, Fraction> term : secondEq.entrySet()) {
            secondEq.put(term.getKey(), term.getValue().divideBy(coefficient));
        }

        // The equations now equal one another, so remove the chosen variable
        firstEq.remove(substitutedVariable);
        secondEq.remove(substitutedVariable);

        // Make a third equation for the equation of one side, subtracted from another
        HashMap<String, Fraction> thirdEq = new HashMap<>();
        if (shouldSubtractFirstEq(targetVariable, firstEq, secondEq)) {
            // thirdEq.put(targetVariable, firstEq.get(targetVariable).subtract(secondEq.get(targetVariable)));
            ArrayList<String> subtractedVariables = new ArrayList<>();
            for (HashMap.Entry<String, Fraction> termToSubtract : firstEq.entrySet()) {
                Fraction subtracted;
                if (secondEq.containsKey(termToSubtract.getKey())) {
                    subtracted = secondEq.get(termToSubtract.getKey());
                }
                else {
                    subtracted = Fraction.getFraction(0);
                }

                thirdEq.put(termToSubtract.getKey(), subtracted.subtract(termToSubtract.getValue()));
                subtractedVariables.add(termToSubtract.getKey());
            }
            // Include variables in the second equation but not the first
            ArrayList<String> variablesLeftOver = new ArrayList<>(secondEq.keySet());
            variablesLeftOver.removeAll(subtractedVariables);
            for (String leftOverVariable : variablesLeftOver) {
                thirdEq.put(leftOverVariable, secondEq.get(leftOverVariable));
            }
        } else {
            // thirdEq.put(targetVariable, secondEq.get(targetVariable).subtract(firstEq.get(targetVariable)));
            ArrayList<String> subtractedVariables = new ArrayList<>();
            for (HashMap.Entry<String, Fraction> termToSubtract : secondEq.entrySet()) {
                Fraction subtracted;
                // If the variable is not in the subtracted equation, the variable's coefficient is 0
                if (firstEq.containsKey(termToSubtract.getKey())) {
                    subtracted = firstEq.get(termToSubtract.getKey());
                }
                else {
                    subtracted = Fraction.getFraction(0);
                }
                thirdEq.put(termToSubtract.getKey(), subtracted.subtract(termToSubtract.getValue()));
                subtractedVariables.add(termToSubtract.getKey());
            }
            // Include variables in the first equation but not the second (the subtracted variables)
            ArrayList<String> variablesLeftOver = new ArrayList<>(firstEq.keySet());
            variablesLeftOver.removeAll(subtractedVariables);
            for (String leftOverVariable : variablesLeftOver) {
                thirdEq.put(leftOverVariable, firstEq.get(leftOverVariable));
            }
        }

        // Rearrange in terms of the target variable
        // If the coefficient of the target variable is negative, then you will need to divide it by a negative number
        // and everything else should be divided by a positive number
        // If the coefficient of the target variable is positive, then you will need to divide it by a positive number
        // and everything else should be divided by a negative number
        // Therefore, negate the divisor for everything, and the target will be one
        Fraction divisor = thirdEq.get(targetVariable).multiplyBy(Fraction.getFraction(-1));

        if (divisor.equals(Fraction.getFraction(0))) {
            throw new ArithmeticException("Equations are equivalent, cannot be solved");
        }

        for (HashMap.Entry<String, Fraction> term : thirdEq.entrySet()) {
            thirdEq.put(term.getKey(), term.getValue().divideBy(divisor));
        }

        thirdEq.remove(targetVariable);

        // Substitute out any of the known variables of the 3rd eq

        Fraction targetValue = Fraction.getFraction(0);
        for (HashMap.Entry<String, Fraction> term : thirdEq.entrySet()) {
            targetValue = targetValue.add(term.getValue().multiplyBy(variables.get(term.getKey())));
        }

        knownTerms.add(targetVariable);
        variables.put(targetVariable, targetValue);
        solveSimpleSubstitution(variables, knownTerms);
    }

    private boolean shouldSubtractFirstEq(String targetVariable, HashMap<String, Fraction> firstEq,
                                          HashMap<String, Fraction> secondEq) {
        // You should subtract if first coefficient is smaller than the second
        return firstEq.get(targetVariable).compareTo(secondEq.get(targetVariable)) <= -1;
    }

    private HashMap<String, Fraction> putTermsToOneSide(AlgebraicEquation eq, List<String> balancedVariables) {
        HashMap<String, Fraction> rearrangedTerms = new HashMap<>();

        // For the variables not moving
        for (HashMap.Entry<String, Integer> term : eq.getFirstSide().entrySet()) {
            rearrangedTerms.put(term.getKey(), Fraction.getFraction(term.getValue()));
        }

        // For the variables moving to the other side
        for (HashMap.Entry<String, Integer> term : eq.getSecondSide().entrySet()) {
            rearrangedTerms.put(term.getKey(), Fraction.getFraction(term.getValue() * -1));
        }

        return rearrangedTerms;
    }

    private static boolean areAllFractionsIntegers(HashMap<String, Fraction> coefficientFractionValues) {
        for (Fraction fraction : coefficientFractionValues.values()) {
            if (fraction.getDenominator() != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the {@link #terms} of the {@code AlgebraicEquation} object
     * @return the {@link #terms} of the {@code AlgebraicEquation} object
     */
    public ArrayList<String> getTerms() {
        return terms;
    }

    /**
     * Gets the {@link #firstSide} of the {@code AlgebraicEquation} object
     * @return the {@link #firstSide} of the {@code AlgebraicEquation} object
     */
    public HashMap<String, Integer> getFirstSide() {
        return firstSide;
    }

    /**
     * Gets the {@link #secondSide} of the {@code AlgebraicEquation} object
     * @return the {@link #secondSide} of the {@code AlgebraicEquation} object
     */
    public HashMap<String, Integer> getSecondSide() {
        return secondSide;
    }

    /**
     * Gets the {@link #equation} of the {@code AlgebraicEquation} object
     * @return the {@link #equation} of the {@code AlgebraicEquation} object
     */
    public String getEquation() {
        return equation;
    }

    /**
     * Gets the {@link #equation} of the {@code AlgebraicEquation} object
     * @return the {@link #equation} of the {@code AlgebraicEquation} object
     */
    @Override
    public String toString() {
        return equation;
    }

    /**
     * An {@link ArrayList} of the terms of the {@code AlgebraicEquation} object. For example: {@code [A, B, C]}
     */
    protected ArrayList<String> terms = new ArrayList<>();

    /**
     * A {@link String} representation of the {@code AlgebraicEquation} object. For example: {@code A + 2B = C}
     */
    protected String equation;

    /**
     * A {@link HashMap} of the first side of the {@code AlgebraicEquation} object. For example: {@code A + 2B}
     */
    protected HashMap<String, Integer> firstSide = new HashMap<>();

    /**
     * A {@link HashMap} of the second side of the {@code AlgebraicEquation} object. For example: {@code C}
     */
    protected HashMap<String, Integer> secondSide = new HashMap<>();

    /**
     * A flag for constructing {@code AlgebraicEquation} objects, to throw an exception if terms are repeated
     */
    public static final boolean WARN_ON_MULTIPLE_OCCURRENCES = true;

    /**
     * A flag for constructing {@code AlgebraicEquation} objects, not to throw an exceptions if terms are repeated
     */
    public static final boolean DO_NOT_WARN_ON_MULTIPLE_OCCURRENCES = false;
}
