package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;

/**
 * Metric Mass. An immutable {@code Mass} object is a wrapper for the {@link BigDecimal} class with methods to convert
 * between metric units of mass
 * @see MetricMassUnit
 */
public class Mass {

    private Mass() {

    }

    /**
     * Makes a {@code Mass} object from a {@code BigDecimal} mass, with grams as the assumed {@code MetricMassUnit}
     * @param mass a {@code BigDecimal} mass value
     */
    public Mass(BigDecimal mass) {
        // If no units are provided, assume grams so do nothing
        this.mass = mass;
    }

    /**
     * Makes a {@code Mass} object from a {@code BigDecimal} mass, with provided {@code MetricMassUnit}
     * @param mass a {@code BigDecimal} mass value
     * @param units a {@code MetricMassUnit} of the passed value
     */
    public Mass(BigDecimal mass , MetricMassUnit units) {
        this(mass.multiply(power(BigDecimal.TEN, getSIExponentFromMassUnit(units))));
    }

    /**
     * Makes a {@code Mass} object from a {@code double} mass, with provided {@code MetricMassUnit}
     * @param mass a {@code double} mass value
     * @param units a {@code MetricMassUnit} of the passed value
     */
    public Mass(double mass, MetricMassUnit units) {
        this(BigDecimal.valueOf(mass), units);
    }

    /**
     * Makes a {@code Mass} object from a {@code double} mass, with grams as the assumed {@code MetricMassUnit}
     * @param mass a {@code double} mass value
     */
    public Mass(double mass) {
        // If no units are provided, assume grams so do nothing
        this(BigDecimal.valueOf(mass));
    }

    /**
     * Add this {@code Mass} object to the {@code Mass} parameter, and return a new {@code Mass} object of the resultant
     * value
     * @param mass the {@code Mass} object to add to this object
     * @return a new {@code Mass} object of the resultant value
     */
    public Mass add(Mass mass) {
        return new Mass(this.mass.add(mass.getMassInGrams()));
    }

    /**
     * Subtract the {@code Mass} parameter from this {@code Mass} object, and return a new {@code Mass} object of the
     * resultant value
     * @param mass the {@code Mass} object to subtract from this object
     * @return a new {@code Mass} object of the resultant value
     */
    public Mass subtract(Mass mass) {
        return new Mass(this.mass.subtract(mass.getMassInGrams()));
    }

    /**
     * Multiply this {@code Mass} object by the {@code Mass} parameter, and return a new {@code Mass} object of the
     * resultant value
     * @param mass the {@code Mass} object to multiply by this object
     * @return a new {@code Mass} object of the resultant value
     */
    public Mass multiply(Mass mass) {
        return new Mass(this.mass.multiply(mass.getMassInGrams()));
    }

    /**
     * Divide this {@code Mass} object by the {@code Mass} parameter, and return a new {@code Mass} object of the
     * resultant value
     * @param mass the {@code Mass} object to divide this object by
     * @return a new {@code Mass} object of the resultant value
     */
    public Mass divide(Mass mass) {
        return new Mass(this.mass.divide(mass.getMassInGrams(), MathContext.DECIMAL64));
    }

    /**
     * Gets the {@link #mass} of the {@code Mass} object in grams
     * @return the {@link #mass} of the {@code Mass} object in grams
     */
    public BigDecimal getMassInGrams() {
        return this.mass;
    }

    /**
     * Generates a {@code String} representation of the {@code Mass} object with appropriate units
     * @return a {@code String} representation of the {@code Mass} object with appropriate units
     */
    public String getMass() {
        // Find engineering exponent
        int exponent = -24;
        BigDecimal newMass;
        BigDecimal THOUSAND = new BigDecimal(BigInteger.valueOf(1000));
        BigDecimal ONE = new BigDecimal(BigInteger.valueOf(1));

        newMass = mass.divide(power(BigDecimal.TEN, exponent), MathContext.DECIMAL64);
        // Start with mass e-24 (which should yield a huge number), eg. 1 == 10000000000E-10
        // So if the number is less than 1, it must be less than yg, so do nothing,
        // Otherwise, make the exponent bigger to find a more sensible newMass
        if ((newMass.abs().compareTo(ONE) == 1)) {
            // If the exponent is bigger than 24, then it is in Yg and beyond this class' capabilities
            while (exponent < 24) {
                // If the newMass is less than a thousand, or (-1000 which is absolute value'd out), then the number
                // is now in Engineering form.
                // if (newMass.abs() < 1000)
                if (newMass.abs().compareTo(THOUSAND) == -1) {
                    break;
                }

                // Find the next mass
                exponent += 3;
                newMass = mass.divide(power(BigDecimal.TEN, exponent), MathContext.DECIMAL64);
            }
        }

        return newMass.round(new MathContext(3)).toEngineeringString() +  SI_EXPONENT_UNITS.get(exponent).name();
    }

    /**
     * Generates the {@code BigDecimal} object with the value of the {@link #mass} with the given units
     * @param targetUnit the desired units for the value
     * @return the {@code BigDecimal} object with the value of the mass with the given units
     */
    public BigDecimal getMass(MetricMassUnit targetUnit) {
        return mass.divide(power(BigDecimal.TEN, SI_UNIT_EXPONENTS.get(targetUnit)), MathContext.DECIMAL64);
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
     * Generates a {@code String} representation of the {@code Mass} object with appropriate units. A wrapper for the
     * {@link #getMass()} method
     * @return a {@code String} representation of the {@code Mass} object with appropriate units
     */
    @Override
    public String toString() {
        return this.getMass();
    }

    /**
     * Metric Units of Mass
     */
    public enum MetricMassUnit {
        /**
         * Yottagram 10<sup>24</sup>g
         */
        Yg,

        /**
         * Zettagram 10<sup>21</sup>g
         */
        Zg,

        /**
         * Exagram 10<sup>18</sup>g
         */
        Eg,

        /**
         * Petagram 10<sup>15</sup>g
         */
        Pg,

        /**
         * Teragram 10<sup>12</sup>g
         */
        Tg,

        /**
         * Gigagram 10<sup>9</sup>g
         */
        Gg,

        /**
         * Megagram 10<sup>6</sup>g
         */
        Mg,

        /**
         * Tonne 10<sup>6</sup>g
         */
        tonne,


        /**
         * Metric tonne 10<sup>6</sup>g
         */
        mt,

        /**
         * Kilogram 10<sup>3</sup>g
         */
        kg,

        /**
         * Gram 10<sup>0</sup>g
         */
        g,

        /**
         * Milligram 10<sup>-3</sup>g
         */
        mg,

        /**
         * Microgram 10<sup>-6</sup>g
         */
        µg,

        /**
         * Nanogram 10<sup>-9</sup>g
         */
        ng,

        /**
         * Picogram 10<sup>-12</sup>g
         */
        pg,

        /**
         * Femtogram 10<sup>-15</sup>g
         */
        fg,

        /**
         * Attogram 10<sup>-18</sup>g
         */
        ag,

        /**
         * Zeptogram 10<sup>-21</sup>g
         */
        zg,

        /**
         * Yoctogram 10<sup>-24</sup>g
         */
        yg
    }

    /**
     * Gets the exponent for the base 10, relative to grams, from a {@code MetricMassUnit}
     * @param unit {@code MetricMassUnit} to find the exponent of
     * @return the exponent for the base 10, relative to grams
     */
    public static int getSIExponentFromMassUnit(MetricMassUnit unit) {
        return SI_UNIT_EXPONENTS.get(unit);
    }

    /**
     * A map of exponents with {@code MetricMassUnit} items as keys
     */
    protected static HashMap<MetricMassUnit, Integer> SI_UNIT_EXPONENTS = new HashMap<>();

    /**
     * A map of {@code MetricMassUnit} items of with exponents as keys
     */
    protected static HashMap<Integer, MetricMassUnit> SI_EXPONENT_UNITS = new HashMap<>();
    static {
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Yg, 24);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Zg, 21);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Eg, 18);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Pg, 15);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Tg, 12);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Gg, 9);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.tonne, 6);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.mt, 6);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.Mg, 6);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.kg, 3);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.g, 0);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.mg, -3);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.µg, -6);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.ng, -9);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.pg, -12);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.fg, -15);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.ag, -18);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.zg, -21);
        SI_UNIT_EXPONENTS.put(MetricMassUnit.yg, -24);

        for (HashMap.Entry<MetricMassUnit, Integer> entry : SI_UNIT_EXPONENTS.entrySet()) {
            SI_EXPONENT_UNITS.put(entry.getValue(), entry.getKey());
        }
        SI_UNIT_EXPONENTS.put(MetricMassUnit.mt, 6);
    }

    // Mass in g
    /**
     * A {@code BigDecimal} object with the value of the mass in grams
     */
    protected BigDecimal mass;
}
