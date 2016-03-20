package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.math.MathContext;

public class Concentration {
    /**
     * Makes a {@code Concentration} object from a {@code BigDecimal} concentration, with moldm<sup>-3</sup> as the assumed unit
     * @param concentration a {@code BigDecimal} concentration value
     */
    public Concentration(BigDecimal concentration) {
        this.concentration = concentration;
    }

    /**
     * Makes a {@code Concentration} object from a {@code double} concentration, with moldm<sup>-3</sup> as the assumed unit
     * @param concentration a {@code double} concentration value
     */
    public Concentration(double concentration) {
        this(BigDecimal.valueOf(concentration));
    }

    /**
     * Add this {@code Concentration} object to the {@code Concentration} parameter, and return a new {@code Concentration} object of the
     * resultant value
     * @param concentration the {@code Concentration} object to add to this object
     * @return a new {@code Concentration} object of the resultant value
     */
    public Concentration add(Concentration concentration) {
        return new Concentration(this.concentration.add(concentration.getConcentration()));
    }

    /**
     * Subtract the {@code Concentration} parameter from this {@code Concentration} object, and return a new {@code Concentration} object of
     * the resultant value
     * @param concentration the {@code Concentration} object to subtract from this object
     * @return a new {@code Concentration} object of the resultant value
     */
    public Concentration subtract(Concentration concentration) {
        return new Concentration(this.concentration.subtract(concentration.getConcentration()));
    }

    /**
     * Multiply this {@code Concentration} object by the {@code Concentration} parameter, and return a new {@code Concentration} object of the
     * resultant value
     * @param concentration the {@code Concentration} object to multiply by this object
     * @return a new {@code Concentration} object of the resultant value
     */
    public Concentration multiply(Concentration concentration) {
        return new Concentration(this.concentration.multiply(concentration.getConcentration()));
    }

    /**
     * Divide this {@code Concentration} object by the {@code Concentration} parameter, and return a new {@code Concentration} object of the
     * resultant value
     * @param concentration the {@code Concentration} object to divide this object by
     * @return a new {@code Concentration} object of the resultant value
     */
    public Concentration divide(Concentration concentration) {
        return new Concentration(this.concentration.divide(concentration.getConcentration(), MathContext.DECIMAL64));
    }

    /**
     * Gets the {@link #concentration} of the {@code Concentration} object in moldm<sup>-3</sup>
     * @return the {@link #concentration} of the {@code Concentration} object in moldm<sup>-3</sup>
     */
    public BigDecimal getConcentration() {
        return this.concentration;
    }

    /**
     * Generates a {@code String} representation of the {@code Concentration} object
     * @return a {@code String} representation of the {@code Concentration} object
     */
    @Override
    public String toString() {
        return concentration.toString() + " moldm-3";
    }

    /**
     * A {@code BigDecimal} object with the value of the concentration in moldm<sup>-3</sup>
     */
    protected BigDecimal concentration;
}
