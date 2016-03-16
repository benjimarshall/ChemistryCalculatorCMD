package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;

/**
 * Metric Volume. An immutable {@code Volume} object is a wrapper for the {@link BigDecimal} class with methods to
 * convert between metric units of volume
 * @see MetricVolumeUnit
 */
public class Volume {

    private Volume() {

    }

    /**
     * Makes a {@code Volume} object from a {@code BigDecimal} volume, with litres as the assumed {@code MetricVolumeUnit}
     * @param volume a {@code BigDecimal} volume value
     */
    public Volume(BigDecimal volume) {
        // If no units are provided, assume litres so do nothing
        this.volume = volume;
    }

    /**
     * Makes a {@code Volume} object from a {@code double} volume, with litres as the assumed {@code MetricVolumeUnit}
     * @param volume a {@code double} volume value
     */
    public Volume(double volume) {
        // If no units are provided, assume litre so do nothing
        this(BigDecimal.valueOf(volume));
    }

    /**
     * Makes a {@code Volume} object from a {@code BigDecimal} volume, with provided {@code MetricVolumeUnit}
     * @param volume a {@code BigDecimal} volume value
     * @param units a {@code MetricVolumeUnit} of the passed value
     */
    public Volume(BigDecimal volume , MetricVolumeUnit units) {
        this(volume.multiply(power(BigDecimal.TEN, getSIExponentFromVolumeUnit(units))));
    }

    /**
     * Makes a {@code Volume} object from a {@code double} volume, with provided {@code MetricVolumeUnit}
     * @param volume a {@code double} volume value
     * @param units a {@code MetricVolumeUnit} of the passed value
     */
    public Volume(double volume, MetricVolumeUnit units) {
        this(BigDecimal.valueOf(volume), units);
    }

    /**
     * Add this {@code Volume} object to the {@code Volume} parameter, and return a new {@code Volume} object of the
     * resultant value
     * @param volume the {@code Volume} object to add to this object
     * @return a new {@code Volume} object of the resultant value
     */
    public Volume add(Volume volume) {
        return new Volume(this.volume.add(volume.getVolumeInLitres()));
    }

    /**
     * Subtract the {@code Volume} parameter from this {@code Volume} object, and return a new {@code Volume} object of
     * the resultant value
     * @param volume the {@code Volume} object to subtract from this object
     * @return a new {@code Volume} object of the resultant value
     */
    public Volume subtract(Volume volume) {
        return new Volume(this.volume.subtract(volume.getVolumeInLitres()));
    }

    /**
     * Multiply this {@code Volume} object by the {@code Volume} parameter, and return a new {@code Volume} object of the
     * resultant value
     * @param volume the {@code Volume} object to multiply by this object
     * @return a new {@code Volume} object of the resultant value
     */
    public Volume multiply(Volume volume) {
        return new Volume(this.volume.multiply(volume.getVolumeInLitres()));
    }

    /**
     * Divide this {@code Volume} object by the {@code Volume} parameter, and return a new {@code Volume} object of the
     * resultant value
     * @param volume the {@code Volume} object to divide this object by
     * @return a new {@code Volume} object of the resultant value
     */
    public Volume divide(Volume volume) {
        return new Volume(this.volume.divide(volume.getVolumeInLitres(), MathContext.DECIMAL64));
    }

    /**
     * Gets the {@link #volume} of the {@code Volume} object in litres
     * @return the {@link #volume} of the {@code Volume} object in litres
     */
    public BigDecimal getVolumeInLitres() {
        return this.volume;
    }

    /**
     * Generates a {@code String} representation of the {@code Volume} object in dm<sup>3</sup>
     * @return a {@code String} representation of the {@code Volume} object dm<sup>3</sup>
     */
    public String getVolume() {
        return volume.toString() + " dm3";
    }

    /**
     * Generates the {@code BigDecimal} object with the value of the {@link #volume} with the given units
     * @param targetUnit the desired units for the value
     * @return the {@code BigDecimal} object with the value of the volume with the given units
     */
    public BigDecimal getVolume(MetricVolumeUnit targetUnit) {
        return volume.divide(power(BigDecimal.TEN, SI_UNIT_EXPONENTS.get(targetUnit)), MathContext.DECIMAL64);
    }

    /**
     * Calculates the value of the base to the power of n, a power method for {@code BigDecimal} to accept negative
     * exponents
     * @param base base of the expression to be evaluated
     * @param n exponent of the expression to be evaluated
     * @return a {@code BigDecimal} object with the value of the base to the power of n
     */
    public static BigDecimal power(BigDecimal base, int n) {
        if (n < 0) {
            return BigDecimal.ONE.divide(base.pow(n * -1), MathContext.DECIMAL128);
        }
        else {
            return base.pow(n);
        }
    }

    /**
     * Generates a {@code String} representation of the {@code Volume} object with appropriate units. A wrapper for the
     * {@link #getVolume()} method
     * @return a {@code String} representation of the {@code Volume} object with appropriate units
     */
    @Override
    public String toString() {
        return this.getVolume();
    }

    /**
     * Metric Units of Volume
     */
    public enum MetricVolumeUnit {
        /**
         * Metres cubed, 10<sup>3</sup>dm<sup>3</sup>
         */
        m3,

        /**
         * Decimetres cubed, 10<sup>0</sup>dm<sup>3</sup>
         */
        dm3,

        /**
         * Litres, 10<sup>0</sup>dm<sup>3</sup>
         */
        l,

        /**
         * Centimetres cubed, 10<sup>-3</sup>dm<sup>3</sup>
         */
        cm3,
    }

    /**
     * Gets the exponent for the base 10, relative to litres, from a {@code MetricVolumeUnit}
     * @param unit {@code MetricVolumeUnit} to find the exponent of
     * @return the exponent for the base 10, relative to litres
     */
    public static int getSIExponentFromVolumeUnit(MetricVolumeUnit unit) {
        return SI_UNIT_EXPONENTS.get(unit);
    }

    /**
     * A map of exponents with {@code MetricVolumeUnit} items as keys
     */
    protected static HashMap<MetricVolumeUnit, Integer> SI_UNIT_EXPONENTS = new HashMap<>();

    /**
     * A map of {@code MetricVolumeUnit} items of with exponents as keys
     */
    protected static HashMap<Integer, MetricVolumeUnit> SI_EXPONENT_UNITS = new HashMap<>();
    static {
        SI_UNIT_EXPONENTS.put(MetricVolumeUnit.dm3, 0);
        SI_UNIT_EXPONENTS.put(MetricVolumeUnit.cm3, -3);
        SI_UNIT_EXPONENTS.put(MetricVolumeUnit.m3, 3);

        for (HashMap.Entry<MetricVolumeUnit, Integer> entry : SI_UNIT_EXPONENTS.entrySet()) {
            SI_EXPONENT_UNITS.put(entry.getValue(), entry.getKey());
        }
        SI_UNIT_EXPONENTS.put(MetricVolumeUnit.l, 0);
    }

    // Volume in dm3
    /**
     * A {@code BigDecimal} object with the value of the volume in dm<sup>3</sup>
     */
    protected BigDecimal volume;
}
