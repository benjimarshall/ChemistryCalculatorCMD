package io.github.benjimarshall;

import io.github.benjimarshall.chem.*;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try {
            // A = 3, B = 12, C = 7, D = 0
            AlgebraicEquation ag = new AlgebraicEquation("3C=1B+3A+2D");
            AlgebraicEquation ag2 = new AlgebraicEquation("2B=3C+1A+2D");
            ArrayList<String> test = new ArrayList<>();
            test.add("A");
            test.add("D");
            HashMap<String, Fraction> knownValues = new HashMap<>();
            knownValues.put("A", Fraction.getFraction(3));
            knownValues.put("D", Fraction.getFraction(0));
            ag.solveSimultaneousEquations(ag2, test, knownValues);

            ArrayList<Integer> testArray = new ArrayList<>();
            testArray.add(20);
            testArray.add(5);
            System.out.println(AlgebraicEquation.lcm(testArray));
            System.out.println(AlgebraicEquation.gcd(testArray));

            HashMap<String, Fraction> testSimplifier = new HashMap<>();
            testSimplifier.put("A", Fraction.getFraction(1, 3));
            testSimplifier.put("B", Fraction.getFraction(1, 4));
            System.out.println(AlgebraicEquation.simplifyCoefficients(testSimplifier));

            System.out.println("Equation: A=3X+C");
            System.out.println("A = 11");
            System.out.println("C = 5");
            AlgebraicEquation ae = new AlgebraicEquation("A=3X+C");
            ArrayList<String> knownTerms = new ArrayList<>();
            knownTerms.add("A");
            knownTerms.add("C");

            HashMap<String, Fraction> variables = new HashMap<>();
            variables.put("A", Fraction.getFraction(11));
            variables.put("C", Fraction.getFraction(5));
            System.out.println("Is solvable by simple substitution: " + ae.isSolvableBySimpleSubstitution(knownTerms));
            ae.solveSimpleSubstitution(variables, knownTerms);
            System.out.println("SSEq Solution: " + variables);

            Element e = new Element("Sodium", Element.NAME);
            Element e2 = new Element("Sodium", Element.NAME);
            System.out.println("Two elements constructed from \"Sodium\" through .equals(): " + e.equals(e2));
            System.out.println("Element generated from \"Sodium\": " + e.getSymbol() + " " + e.getName() + " " +
                    e.getAtomicNumber() + " " + e.getMassNumber());

            Molecule molecule = new Molecule("Al2(CO3)3");
            System.out.println("Molecule from: \"Al2(CO3)3\"");
            for (HashMap.Entry<Element, Integer> entry : molecule.getElementMap().entrySet()) {
                System.out.println(entry.getKey().getName() + ": " + entry.getValue());
            }
            System.out.println("RFM: " + molecule.getRelativeFormulaMass());

            Equation equation1 = new Equation("C2H6 + O2 -> CO2 + H2O");
            System.out.println(equation1);
            Equation equation2 = new Equation("N2 + H2 -> NH3");
            System.out.println(equation2);
            Equation equation3 = new Equation("C + O2 -> CO2");
            System.out.println(equation3);
            Equation equation4 = new Equation("S + HNO3 -> H2SO4 + NO2 + H2O");
            System.out.println(equation4);
            Equation equation = new Equation("C2H6 + O2 -> CO2 + H2O + C");
            System.out.println(equation);

        }
        catch (ArithmeticException | FlagException | NotationInterpretationException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
