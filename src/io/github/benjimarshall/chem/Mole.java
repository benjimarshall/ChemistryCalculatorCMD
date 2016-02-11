package io.github.benjimarshall.chem;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Moles of particles. An immutable {@code Mole} object is a wrapper for the {@link BigDecimal} class with a method to 
 * convert between moles and number of particles
 */
public class Mole {
    private Mole() {

    }

    /**
     * Makes a {@code Mole} object from a {@code BigDecimal} quantity
     * @param quantity a {@code BigDecimal} quantity
     */
    public Mole(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * Makes a {@code Mole} object from a {@code double} quantity
     * @param quantity a {@code double} quantity
     */
    public Mole(double quantity) {
        this(BigDecimal.valueOf(quantity));
    }

    /**
     * Add this {@code mole} object to the {@code mole} parameter, and return a new {@code mole} object of the resultant
     * value
     * @param mole the {@code mole} object to add to this object
     * @return a new {@code mole} object of the resultant value
     */
    public Mole add(Mole mole) {
        return new Mole(this.quantity.add(mole.getQuantity()));
    }

    /**
     * Subtract the {@code Mole} parameter from this {@code Mole} object, and return a new {@code Mole} object of the
     * resultant value
     * @param mole the {@code Mole} object to subtract from this object
     * @return a new {@code Mole} object of the resultant value
     */
    public Mole subtract(Mole mole) {
        return new Mole(this.quantity.subtract(mole.getQuantity()));
    }

    /**
     * Multiply this {@code Mole} object by the {@code Mole} parameter, and return a new {@code Mole} object of the
     * resultant value
     * @param mole the {@code Mole} object to multiply by this object
     * @return a new {@code Mole} object of the resultant value
     */
    public Mole multiply(Mole mole) {
        return new Mole(this.quantity.multiply(mole.getQuantity()));
    }

    /**
     * Divide this {@code Mole} object by the {@code Mole} parameter, and return a new {@code Mole} object of the
     * resultant value
     * @param mole the {@code Mole} object to divide this object by
     * @return a new {@code Mole} object of the resultant value
     */
    public Mole divide(Mole mole) {
        return new Mole(this.quantity.divide(mole.getQuantity(), MathContext.DECIMAL32));
    }

    /**
     * Gets the {@link #quantity} of the {@code Mole} object
     * @return the {@link #quantity} of the {@code Mole} object
     */
    public BigDecimal getQuantity() {
        return quantity;
    }


    /**
     * Gets a {@code String} representation of this {@code Mole} object. It is the {@link #quantity} with the units
     * "mol" appended
     * @return the {@link #quantity} with the units "mol" appended
     */
    @Override
    public String toString() {
        return this.quantity.toString() + "mol";
    }

    /**
     * Gets the number of molecules in this {@code Mole} object. This is found by multiplying {@link #quantity} by the
     * {@link #AVOGADRO_CONST}
     * @return the number of molecules in this {@code Mole} object.
     */
    public BigDecimal getNumberofMolecules() {
        return AVOGADRO_CONST.multiply(this.quantity);
    }

    /**
     * The number of moles this {@code Mole} object contains
     */
    protected BigDecimal quantity;

    /**
     * The Avogadro constant
     */
    public static final BigDecimal AVOGADRO_CONST  = BigDecimal.valueOf(6022, 20);
}
