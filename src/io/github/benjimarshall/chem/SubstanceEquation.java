package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Chemical equation with amounts. A {@code SubstanceEquation} object consists of two maps, of its reactants and its
 * products, with a string representation of it. It also contains two maps of {@code Substance} objects to calculate
 * quantities from. Also, a reference to the limiting reagent is kept.
 *
 * @see AlgebraicEquation
 * @see Substance
 */
public class SubstanceEquation extends Equation {
    /**
     * Constructs a {@code SubstanceEquation} object from a {@code String} representation of the equation, without
     * amounts of reactants or products.
     * @param equation {@code String} representation of the equation
     * @throws NotationInterpretationException when the equation cannot be interpreted, or a constituent element cannot
     * be parsed
     */
    public SubstanceEquation(String equation) throws NotationInterpretationException {
        super(equation);
    }

    /**
     * Deduces all of the quantities of the substances from one substance, to exactly react with no left over reacants.
     * @param substance the substance with the known quantities to deduce all other quantities from
     * @throws IllegalArgumentException when the substance is of a molecule not involved in the reaction
     */
    public void fillInFromSubstance(Substance substance) throws IllegalArgumentException {
        deleteQuantities();
        putReactantSubstance(substance);
        fillInSubstances();
    }

    /**
     * Adds a reactant with a known quantity to the {@code SubstanceEquation} object
     * @param substance a reactant with a known quantity
     * @throws IllegalArgumentException when the substance is the child of a molecule not involved in the reaction
     */
    public void putReactantSubstance(Substance substance) throws IllegalArgumentException {
        if (!super.reactants.containsKey(substance.getMolecule())) {
            throw new IllegalArgumentException("Molecule not found");
        }
        substanceReactants.put(substance.getMolecule(), substance);
    }

    /**
     * Deduce the minimum quantities of remaining reactants, maximum quantities of products, and the limiting reagent
     * based upon the reactants already added to the {@code SubstanceEquation} object
     * @throws NullPointerException when no substances have previously been specified
     */
    public void fillInSubstances() throws NullPointerException {
        if (substanceReactants.isEmpty()) {
            throw new NullPointerException("No quantities are known");
        }

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

    /**
     * Removes all data pertaining to quantities of reactants reverting to when the {@code SubstanceEquation} object was
     * constructed.
     */
    public void deleteQuantities() {
        substanceReactants = new HashMap<>();
        substanceProducts = new HashMap<>();
        reactantMultiplier = null;
        limitingReagent = null;
    }

    /**
     * Gets the {@code String} representation of the {@code SubstanceEquation} object
     * @return the {@code String} representation of the {@code SubstanceEquation} object
     */
    @Override
    public String toString() {
        StringBuilder equationBuilder = new StringBuilder();
        equationBuilder.append(super.toString()).append("\n");

        for (Substance reactant : this.substanceReactants.values()) {
            equationBuilder.append(reactant);
            equationBuilder.append(" + ");
        }

        equationBuilder.replace(equationBuilder.length() - 3, equationBuilder.length(), " -> ");

        for (Substance product : this.substanceProducts.values()) {
            equationBuilder.append(product);
            equationBuilder.append(" + ");
        }

        equationBuilder.replace(equationBuilder.length() - 3, equationBuilder.length(), "");
        return equationBuilder.toString();
    }

    /**
    * Gets the {@link #substanceReactants} of {@code SubstanceEquation} object
    * @return the {@link #substanceReactants} of {@code SubstanceEquation} object
    */
    public HashMap<Molecule, Substance> getSubstanceReactants() {
        return substanceReactants;
    }

    /**
     * Gets the {@link #substanceProducts} of {@code SubstanceEquation} object
     * @return the {@link #substanceProducts} of {@code SubstanceEquation} object
     */
    public HashMap<Molecule, Substance> getSubstanceProducts() {
        return substanceProducts;
    }

    /**
     * Gets the {@link #reactantMultiplier} of {@code SubstanceEquation} object
     * @return the {@link #reactantMultiplier} of {@code SubstanceEquation} object
     */
    public BigDecimal getReactantMultiplier() {
        return reactantMultiplier;
    }

    /**
     * Gets the {@link #limitingReagent} of {@code SubstanceEquation} object
     * @return the {@link #limitingReagent} of {@code SubstanceEquation} object
     */
    public Substance getLimitingReagent() {
        return limitingReagent;
    }

    /**
     * A {@link HashMap} of the reactants in the equation, the key being the reactant molecules involved and the value
     * their quantities stored in a {@code Substance} object
     */
    protected HashMap<Molecule, Substance> substanceReactants = new HashMap<>();

    /**
     * A {@link HashMap} of the products in the equation, the key being the product molecules involved and the value
     * their quantities stored in a {@code Substance} object
     */
    protected HashMap<Molecule, Substance> substanceProducts = new HashMap<>();

    /**
     * The reactant multiplier, which when multiplied by the integer ratio number of a reactant, gives the number of
     * moles of the reactant
     */
    protected BigDecimal reactantMultiplier;

    /**
     * The limiting reagent of the equation, the substance which is fully used when all others are not.
     */
    protected Substance limitingReagent;
}
