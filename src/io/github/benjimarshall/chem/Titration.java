package io.github.benjimarshall.chem;

import org.apache.commons.lang3.math.Fraction;

/**
 * Titration calculator. A static class of static methods for calculating a titration using static functions.
 *
 * @see Solution
 */
public final class Titration {
    private Titration() {

    }

    /**
     * Find the second solution to neutralise the first solution.
     * @param solution1 the solution to be neutralised
     * @param molecule2 the molecule type of the second solution
     * @param volume2 the volume of the second solution
     * @param sol1PerSol2 the number of moles of the second solution to neutralise one mole of the second solution
     * @return a {@code Solution} object of the second solution
     */
    public static Solution doTitration(Solution solution1, Molecule molecule2, Volume volume2, Fraction sol1PerSol2) {
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(molecule2, mol2, volume2);
    }

    /**
     * Find the second solution to neutralise the first solution.
     * @param solution1 the solution to be neutralised
     * @param formula2 a {@code String} representation of the molecule type of the second solution
     * @param volume2 the volume of the second solution
     * @param sol1PerSol2 the number of moles of the second solution to neutralise one mole of the second solution
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     * @return a {@code Solution} object of the second solution
     */
    public static Solution doTitration(Solution solution1, String formula2, Volume volume2, Fraction sol1PerSol2)
            throws NotationInterpretationException {
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(formula2, mol2, volume2);
    }

    /**
     * Find the second solution to neutralise the first solution.
     * @param solution1 the solution to be neutralised
     * @param molecule2 the molecule type of the second solution
     * @param concentration2 the concentration of the second solution
     * @param sol1PerSol2 the number of moles of the second solution to neutralise one mole of the second solution
     * @return a {@code Solution} object of the second solution
     */
    public static Solution doTitration(Solution solution1, Molecule molecule2, Concentration concentration2,
                                       Fraction sol1PerSol2) {
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(molecule2, mol2, concentration2);
    }

    /**
     * Find the second solution to neutralise the first solution.
     * @param solution1 the solution to be neutralised
     * @param formula2 a {@code String} representation of the molecule type of the second solution
     * @param concentration2 the concentration of the second solution
     * @param sol1PerSol2 the number of moles of the second solution to neutralise one mole of the second solution
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     * @return a {@code Solution} object of the second solution
     */
    public static Solution doTitration(Solution solution1, String formula2, Concentration concentration2,
                                       Fraction sol1PerSol2) throws NotationInterpretationException{
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(formula2, mol2, concentration2);
    }
}
