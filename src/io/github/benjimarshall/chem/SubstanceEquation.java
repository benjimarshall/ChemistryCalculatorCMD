package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.util.HashMap;

public class SubstanceEquation extends Equation {
    public SubstanceEquation(String equation) throws NotationInterpretationException {
        super(equation);
    }

    public void fillInFromSubstance(Substance substance) throws IllegalArgumentException {
        putReactantSubstance(substance);
        fillInSubstances();
    }

    public void putReactantSubstance(Substance substance) throws IllegalArgumentException {
        if (!super.reactants.containsKey(substance.getMolecule())) {
            throw new IllegalArgumentException("Molecule not found");
        }
        substanceReactants.put(substance.getMolecule(), substance);
    }

    public void fillInSubstances() {
        // Find the smallest multiplier
        for (Substance substance : substanceReactants.values()) {
            BigDecimal newValue = substance.getMoles().divide(new Mole(
                    super.getReactants().get(substance.getMolecule()))).getQuantity();
            // If the reactant multiplier is null or is bigger than the found value, replace it
            if (reactantMultiplier == null || reactantMultiplier.compareTo(newValue) == 1) {
                reactantMultiplier = newValue;
                limitingReagent = substance;
            }
        }

        /*// Generate the new reactants
        for (HashMap.Entry<Molecule, Integer> reactant : super.getReactants().entrySet()) {
            substanceReactants.put(reactant.getKey(), new Substance(reactant.getKey(), new Mole(
                    reactantMultiplier.multiply(new BigDecimal(reactant.getValue())))));
        }*/

        // Generate the products
        for (HashMap.Entry<Molecule, Integer> product : super.getProducts().entrySet()) {
            substanceProducts.put(product.getKey(), new Substance(product.getKey(), new Mole(
                    reactantMultiplier.multiply(new BigDecimal(product.getValue())))));
        }
    }

    public HashMap<Molecule, Substance> getSubstanceReactants() {
        return substanceReactants;
    }

    public HashMap<Molecule, Substance> getSubstanceProducts() {
        return substanceProducts;
    }

    public BigDecimal getReactantMultiplier() {
        return reactantMultiplier;
    }

    public Substance getLimitingReagent() {
        return limitingReagent;
    }

    protected HashMap<Molecule, Substance> substanceReactants = new HashMap<>();
    protected HashMap<Molecule, Substance> substanceProducts = new HashMap<>();
    protected BigDecimal reactantMultiplier;
    protected Substance limitingReagent;
}
