package io.github.benjimarshall;

import io.github.benjimarshall.chem.*;
import org.apache.commons.lang3.math.Fraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static boolean balancingMode = false;


    public static void main(String[] args) {
        if (balancingMode) {
            launchBalancer();
        }
        else {
            try {
                BigDecimal bd = new BigDecimal(BigInteger.valueOf(3926), 21);
                System.out.println(bd);
                System.out.println(bd.toEngineeringString());
                System.out.println(bd.toEngineeringString().split("[Ee]")[1].substring(1));
                Mass m = new Mass(5632.2103, Mass.MetricUnit.ng);
                System.out.println("\n" + m.getMassInGrams());
                System.out.println(m.getMass(Mass.MetricUnit.g).toEngineeringString());
                System.out.println(m.getMass());


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
                System.out.println("Empirical formula: " + molecule.getEmpiricalFormula());

                HashMap<Element, Integer> elementMap = new HashMap<>();
                elementMap.put(new Element("C", Element.SYMBOL), 1);
                elementMap.put(new Element("O", Element.SYMBOL), 2);
                System.out.println(elementMap);
                Molecule molecule1 = new Molecule(elementMap);
                System.out.println(molecule1 + ", RFM: " + molecule1.getRelativeFormulaMass());
                System.out.println("Empirical formula: " + molecule1.getEmpiricalFormula());

                Molecule molecule2 = new Molecule("C4H8");
                System.out.println("Molecule from: \"C4H8\"");
                for (HashMap.Entry<Element, Integer> entry : molecule2.getElementMap().entrySet()) {
                    System.out.println(entry.getKey().getName() + ": " + entry.getValue());
                }
                System.out.println("RFM: " + molecule2.getRelativeFormulaMass());
                System.out.println("Empirical formula: " + molecule2.getEmpiricalFormula());


                Equation equation = new Equation("C2H6 + O2 -> CO2 + H2O");
                System.out.println(equation);
                System.out.println("Atom Economy of CO2: " + equation.atomEconomy(new Molecule("CO2")));
                Equation equation1 = new Equation("N2 + H2 + H2 + H2 -> NH3 + NH3");
                System.out.println(equation1);
                System.out.println("Atom Economy of NH3: " + equation1.atomEconomy(new Molecule("NH3")));
                Equation equation2 = new Equation("N2 + H2 -> NH3");
                System.out.println(equation2);
                System.out.println("Atom Economy of NH3: " + equation2.atomEconomy(new Molecule("NH3")));
                Equation equation3 = new Equation("C + O2 -> CO2");
                System.out.println(equation3);
                System.out.println("Atom Economy of CO2: " + equation3.atomEconomy(new Molecule("CO2")));
                Equation equation4 = new Equation("S + HNO3 -> H2SO4 + NO2 + H2O");
                System.out.println(equation4);
                System.out.println("Atom Economy of NO2: " + equation4.atomEconomy(new Molecule("NO2")));
                Equation equation5 = new Equation("C2H6 + O2 -> CO2 + H2O + C");
                System.out.println(equation5);
                System.out.println("Atom Economy of C: " + equation5.atomEconomy(new Molecule("C")) + "\n\n");

                Mole mole = new Mole(5.0);
                Mole mole1 = new Mole(3);

                System.out.println(mole.toString());
                System.out.println("5 + 3 = " + mole.add(mole1));
                System.out.println("5 - 3 = " + mole.subtract(mole1));
                System.out.println("5 * 3 = " + mole.multiply(mole1));
                System.out.println("5 / 3 = " + mole.divide(mole1));


                Mass mass = new Mass(BigDecimal.valueOf(3), Mass.MetricUnit.tonne);
                Mass mass1 = new Mass(3, Mass.MetricUnit.g);

                System.out.println("\n" + mass);
                System.out.println("5 + 3 = " + mass.add(mass1));
                System.out.println("5 - 3 = " + mass.subtract(mass1));
                System.out.println("5 * 3 = " + mass.multiply(mass1));
                System.out.println("5 / 3 = " + mass.divide(mass1));

                Substance sub = new Substance("H2O", new Mass(new BigDecimal(BigInteger.valueOf(6L))));
                System.out.println("\n" + sub.toString());
                System.out.println(sub.getMass());
                System.out.println(sub.getMoles());

                Substance sub2 = new Substance("H2O", new Mole(new BigDecimal((0.5))));
                System.out.println("\n" + sub2.toString());
                System.out.println(sub2.getMass());
                System.out.println(sub2.getMoles());

                Substance substance = new Substance("H2O", new Mass(new BigDecimal(BigInteger.valueOf(18L))));
                Substance substance1 = new Substance("CO2", new Mass(new BigDecimal(BigInteger.valueOf(22L))));

                SubstanceEquation seq = new SubstanceEquation("MgCl2 + H2O + CO2 -> MgCO3 + HCl");
                seq.putReactantSubstance(substance);
                seq.putReactantSubstance(substance1);

                System.out.println("Before filling in: " + seq);
                seq.fillInSubstances();
                System.out.println("After filling in: " + seq);
                System.out.println("Limiting reagent: " + seq.getLimitingReagent());


                SubstanceEquation seq1 = new SubstanceEquation("S + HNO3 -> NO2 + H2O + H2SO4");
                seq1.fillInFromSubstance(new Substance("HNO3", new Mole(2)));

                System.out.println("\n\nBefore filling in: " + seq);
                seq.fillInSubstances();
                System.out.println("After filling in: " + seq);
                System.out.println("Limiting reagent: " + seq.getLimitingReagent());


            } catch (ArithmeticException | FlagException | NotationInterpretationException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }

            sc.close();
        }
    }

    public static void launchBalancer() {
        while (true) {
            try {
                System.out.print("Enter an equation (blank to end): ");
                String input = sc.nextLine();

                if (input.length() == 0) {
                    break;
                }

                Equation eq = new Equation(input);
                System.out.println(eq.toString());
            }
            catch (NotationInterpretationException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
    }
}
