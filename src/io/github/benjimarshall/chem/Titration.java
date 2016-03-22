package io.github.benjimarshall.chem;

import org.apache.commons.lang3.math.Fraction;

public final class Titration {
    private Titration() {

    }

    public static Solution doTitration(Solution solution1, Molecule molecule2, Volume volume2, Fraction sol1PerSol2) {
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(molecule2, mol2, volume2);
    }

    public static Solution doTitration(Solution solution1, String formula2, Volume volume2, Fraction sol1PerSol2)
            throws NotationInterpretationException {
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(formula2, mol2, volume2);
    }

    public static Solution doTitration(Solution solution1, Molecule molecule2, Concentration concentration,
                                       Fraction sol1PerSol2) {
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(molecule2, mol2, concentration);
    }

    public static Solution doTitration(Solution solution1, String formula2, Concentration concentration,
                                       Fraction sol1PerSol2) throws NotationInterpretationException{
        Mole mol2 = solution1.getMoles().multiply(new Mole(sol1PerSol2.invert().doubleValue()));
        return new Solution(formula2, mol2, concentration);
    }
}
