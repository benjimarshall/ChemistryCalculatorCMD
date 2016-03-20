package io.github.benjimarshall.chem;

import java.math.MathContext;
import java.math.RoundingMode;

public class Solution extends Substance {
    public Solution(Substance substance, Volume volume) {
        super(substance);
        this.volume = volume;
        this.concentration = calculateConcentration(getMoles(), volume);
    }

    public Solution(Substance substance, Concentration concentration) {
        super(substance);
        this.concentration = concentration;
        this.volume = calculateVolume(substance.getMoles(), concentration);
    }

    protected Concentration calculateConcentration(Mole moles, Volume volume) {
        return new Concentration(moles.getQuantity().divide(volume.getVolumeInLitres(),
                new MathContext(5, RoundingMode.HALF_UP)));
    }

    protected Volume calculateVolume(Mole moles, Concentration concentration) {
        return new Volume(moles.getQuantity().divide(concentration.getConcentration(),
                new MathContext(5, RoundingMode.HALF_UP)));
    }

    protected Mole calculateMoles(Volume volume, Concentration concentration) {
        return new Mole(volume.getVolumeInLitres().multiply(concentration.getConcentration(),
                new MathContext(5, RoundingMode.HALF_UP)));
    }


    public Solution(String formula, Mole moles, Volume volume) throws NotationInterpretationException {
        super(formula, moles);
        this.volume = volume;
        this.concentration = calculateConcentration(moles, volume);
    }

    public Solution(String formula, Mass mass, Volume volume) throws NotationInterpretationException {
        super(formula, mass);
        this.volume = volume;
        this.concentration = calculateConcentration(getMoles(), volume);
    }

    public Solution(Molecule molecule, Mole moles, Volume volume) {
        super(molecule, moles);
        this.volume = volume;
        this.concentration = calculateConcentration(moles, volume);
    }

    public Solution(Molecule molecule, Mass mass, Volume volume) {
        super(molecule, mass);
        this.volume = volume;
        this.concentration = calculateConcentration(getMoles(), volume);
    }

    public Solution(String formula, Mole moles, Concentration concentration) throws NotationInterpretationException {
        super(formula, moles);
        this.concentration = concentration;
        this.volume = calculateVolume(moles, concentration);
    }

    public Solution(String formula, Mass mass, Concentration concentration) throws NotationInterpretationException {
        super(formula, mass);
        this.concentration = concentration;
        this.volume = calculateVolume(getMoles(), concentration);
    }

    public Solution(Molecule molecule, Mole moles, Concentration concentration) {
        super(molecule, moles);
        this.concentration = concentration;
        this.volume = calculateVolume(moles, concentration);
    }

    public Solution(Molecule molecule, Mass mass, Concentration concentration) {
        super(molecule, mass);
        this.concentration = concentration;
        this.volume = calculateVolume(getMoles(), concentration);
    }

    public Volume getVolume() {
        return volume;
    }

    public Concentration getConcentration() {
        return concentration;
    }

    protected Volume volume;
    // In dm3
    protected Concentration concentration;
}
