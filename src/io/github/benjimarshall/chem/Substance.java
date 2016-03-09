package io.github.benjimarshall.chem;


import java.math.MathContext;

/**
 * Chemical Substance, a {@code Molecule} object with mass. The {@code Substance} class extends the {@link Molecule}
 * class. A {@code Molecule} object consists of its constituent {@code Element} objects, their ratios, each
 * {@code Molecule}'s relative formula mass, and a mass part consisting of a {@link Mass} object and a {@link Mole}
 * object.
 *
 *
 * @author Benji Marshall
 * @since 2016-3-9
 */
public class Substance extends Molecule {
    public Substance(String formula, Mole moles) throws NotationInterpretationException {
        super(formula);
        this.moles = moles;

        mass = new Mass(moles.getQuantity().multiply(this.relativeFormulaMass));
    }

    public Substance(String formula, Mass mass) throws NotationInterpretationException {
        super(formula);
        this.mass = mass;

        moles = new Mole(mass.getMassInGrams().divide(this.relativeFormulaMass, MathContext.DECIMAL64));
    }

    /**
     * Gets the {@link #mass} of the {@code Substance} object.
     * @return the {@link #mass} of the {@code Substance} object.
     */
    public Mass getMass() {
        return mass;
    }

    /**
     * Gets the number of {@link #moles} of the {@code Substance} object.
     * @return the number of {@link #moles} of the {@code Substance} object.
     */
    public Mole getMoles() {
        return moles;
    }

    /**
     * Gets a {@link String} representation of the {@code Substance} object, as the number of {@link #moles} as a string
     * with the {@code Molecule} part as a string. (Eg. {@code 3mol of NH3})
     * @return a {@link String} representation of the {@code Substance} object, as the number of {@link #moles} as a
     * string with the {@code Molecule} part as a string. (Eg. {@code 3mol of NH3})
     */
    @Override
    public String toString(){
        return moles.toString() + " of " + super.toString();
    }

    /**
     * Compares the parameter to this object, returning true if they are equal.
     * @param o the object to compare to this {@code Molecule} object
     * @return whether the two {@code Molecule} objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Substance)) return false;
        if (!super.equals(o)) return false;

        Substance substance = (Substance) o;

        return getMass().equals(substance.getMass()) && getMoles().equals(substance.getMoles());

    }

    /**
     * Generates the hash code of this {@code Substance} object
     * @return the hash code of this {@code Substance} object
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getMass().hashCode();
        result = 31 * result + getMoles().hashCode();
        return result;
    }

    /**
     * The mass of the {@code Substance} object, as a {@link Mass} object.
     */
    protected Mass mass;

    /**
     * The number of moles of the {@code Substance} object, as a {@link Mole} object.
     */
    protected Mole moles;
}
