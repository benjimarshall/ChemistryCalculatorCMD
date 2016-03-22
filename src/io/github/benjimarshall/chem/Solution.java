package io.github.benjimarshall.chem;

import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A Chemical Solution, a {@link Substance} object with a {@link Volume} and {@link Concentration}. The
 * {@code Solution} class extends the {@code Substance} class. It has all of the extended fields and methods of the
 * {@code Substance} class, as well as a {@code Volume} and {@code Concentration} object.
 */
public class Solution extends Substance {
    /**
     * Constructs a {@code Solution} object with a {@code Substance} and a {@code Volume} to be dissolved in
     * @param substance the {@code Substance} to be dissolved
     * @param volume the {@code Volume} of the solvent
     */
    public Solution(Substance substance, Volume volume) {
        super(substance);
        this.volume = volume;
        this.concentration = calculateConcentration(getMoles(), volume);
    }

    /**
     * Constructs a {@code Solution} object with a {@code Substance} and the {@code Concentration} of the solution
     * @param substance the {@code Substance} to be dissolved
     * @param concentration the {@code Concentration} of the solution
     */
    public Solution(Substance substance, Concentration concentration) {
        super(substance);
        this.concentration = concentration;
        this.volume = calculateVolume(substance.getMoles(), concentration);
    }

    /**
     * Constructs a {@code Solution} object with a formula, the a number of moles and the volume of the solvent
     * @param formula a string representation of the formula of the solute
     * @param moles the number of {@code Mole}s of the solute
     * @param volume the {@code Volume} of the solvent
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     */
    public Solution(String formula, Mole moles, Volume volume) throws NotationInterpretationException {
        super(formula, moles);
        this.volume = volume;
        this.concentration = calculateConcentration(moles, volume);
    }

    /**
     * Constructs a {@code Solution} object with a formula, the mass and the volume of the solvent
     * @param formula a string representation of the formula of the solute
     * @param mass the {@code Mass} of the solute
     * @param volume the {@code Volume} of the solvent
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     */
    public Solution(String formula, Mass mass, Volume volume) throws NotationInterpretationException {
        super(formula, mass);
        this.volume = volume;
        this.concentration = calculateConcentration(getMoles(), volume);
    }

    /**
     * Constructs a {@code Solution} object with a molecule, the a number of moles and the volume of the solvent
     * @param molecule the solute molecule
     * @param moles the number of {@code Mole}s of the solute
     * @param volume the {@code Volume} of the solvent
     */
    public Solution(Molecule molecule, Mole moles, Volume volume) {
        super(molecule, moles);
        this.volume = volume;
        this.concentration = calculateConcentration(moles, volume);
    }

    /**
     * Constructs a {@code Solution} object with a molecule, the mass and the volume of the solvent
     * @param molecule the solute molecule
     * @param mass the {@code Mass} of the solute
     * @param volume the {@code Volume} of the solvent
     */
    public Solution(Molecule molecule, Mass mass, Volume volume) {
        super(molecule, mass);
        this.volume = volume;
        this.concentration = calculateConcentration(getMoles(), volume);
    }

    /**
     * Constructs a {@code Solution} object with a formula, the a number of moles of the solute, and the concentration
     * of the solution
     * @param formula a string representation of the formula of the solute
     * @param moles the number of {@code Mole}s of the solute
     * @param concentration the {@code Concentration} of the solution
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     */
    public Solution(String formula, Mole moles, Concentration concentration) throws NotationInterpretationException {
        super(formula, moles);
        this.concentration = concentration;
        this.volume = calculateVolume(moles, concentration);
    }

    /**
     * Constructs a {@code Solution} object with a formula, the a number of moles of the solute, and the concentration
     * of the solution
     * @param formula a string representation of the formula of the solute
     * @param mass the {@code Mass} of the solute
     * @param concentration the {@code Concentration} of the solution
     * @throws NotationInterpretationException when the string cannot be interpreted as a molecule
     */
    public Solution(String formula, Mass mass, Concentration concentration) throws NotationInterpretationException {
        super(formula, mass);
        this.concentration = concentration;
        this.volume = calculateVolume(getMoles(), concentration);
    }

    /**
     * Constructs a {@code Solution} object with a molecule, the a number of moles of the solute, and the concentration
     * of the solution
     * @param molecule the solute molecule
     * @param moles the number of {@code Mole}s of the solute
     * @param concentration the {@code Concentration} of the solvent
     */
    public Solution(Molecule molecule, Mole moles, Concentration concentration) {
        super(molecule, moles);
        this.concentration = concentration;
        this.volume = calculateVolume(moles, concentration);
    }

    /**
     * Constructs a {@code Solution} object with a molecule, the mass of the solute, and the concentration of the
     * solution
     * @param molecule the solute molecule
     * @param mass the {@code Mass} of the solute
     * @param concentration the {@code Concentration} of the solvent
     */
    public Solution(Molecule molecule, Mass mass, Concentration concentration) {
        super(molecule, mass);
        this.concentration = concentration;
        this.volume = calculateVolume(getMoles(), concentration);
    }

    /**
     * Calculates the {@code Concentration} of a solution, from the number of {@code Mole}s of the solute, and the
     * {@code Volume} of the solution
     * @param moles the number of {@code Mole}s of the solute
     * @param volume the {@code Volume} of the solution
     * @return the {@code Concentration} of the solution
     */
    public static Concentration calculateConcentration(Mole moles, Volume volume) {
        return new Concentration(moles.getQuantity().divide(volume.getVolumeInLitres(),
                new MathContext(5, RoundingMode.HALF_UP)));
    }

    /**
     * Calculates the {@code Volume} of a solution, from the number of {@code Mole}s of the solute, and the
     * {@code Concentration} of the solution
     * @param moles the number of {@code Mole}s of the solute
     * @param concentration the {@code Concentration} of the solution
     * @return the {@code Volume} of the solution
     */
    public static Volume calculateVolume(Mole moles, Concentration concentration) {
        return new Volume(moles.getQuantity().divide(concentration.getConcentration(),
                new MathContext(5, RoundingMode.HALF_UP)));
    }

    /**
     * Calculates the number of {@code Mole}s of the solute, from the {@code Volume} and the {@code Concentration} of
     * the solution
     * @param volume the {@code Volume} of the solution
     * @param concentration the {@code Concentration} of the solution
     * @return the number of {@code Mole}s of the solute
     */
    public static Mole calculateMoles(Volume volume, Concentration concentration) {
        return new Mole(volume.getVolumeInLitres().multiply(concentration.getConcentration(),
                new MathContext(5, RoundingMode.HALF_UP)));
    }

    /**
     * Generates a {@code String} representation of this {@code Solution} object
     * @return a {@code String} representation of this {@code Solution} object
     */
    @Override
    public String toString() {
        return super.toString() + " in " + volume.toString() + " at " + concentration.toString();
    }

    /**
     * Gets the {@link #volume} of the {@code Solution} object.
     * @return the {@link #volume} of the {@code Solution} object.
     */
    public Volume getVolume() {
        return volume;
    }

    /**
     * Gets the {@link #concentration} of the {@code Solution} object.
     * @return the {@link #concentration} of the {@code Solution} object.
     */
    public Concentration getConcentration() {
        return concentration;
    }

    /**
     * The volume of the {@code Solution} object, as a {@link Volume} object.
     */
    protected Volume volume;

    /**
     * The concentration of the {@code Solution} object, as a {@link Concentration} object.
     */
    protected Concentration concentration;
}
