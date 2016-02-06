package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;

public class Mass {

    private Mass() {

    }

    public Mass(BigDecimal mass) {
        this.mass = mass;
    }

    public Mass(BigDecimal mass , Unit units) {
        this(mass.multiply(power(BigDecimal.TEN, getSIIndexFromMassUnit(units))));
    }

    public Mass(double mass, Unit units) {
        this(BigDecimal.valueOf(mass), units);
    }

    // If no units are provided, assume grams so do nothing
    public Mass(double mass) {
        this(BigDecimal.valueOf(mass));
    }

    public Mass add(Mass mass) {
        return new Mass(this.mass.add(mass.getMassInGrams()));
    }

    public Mass subtract(Mass mass) {
        return new Mass(this.mass.subtract(mass.getMassInGrams()));
    }

    public Mass multiply(Mass mass) {
        return new Mass(this.mass.multiply(mass.getMassInGrams()));
    }

    public Mass divide(Mass mass) {
        return new Mass(this.mass.divide(mass.getMassInGrams(), MathContext.DECIMAL32));
    }

    public BigDecimal getMassInGrams() {
        return this.mass;
    }

    public String getMass() {
        // Broken
        /*
        String engineeringString = mass.toEngineeringString();
        String[] components = engineeringString.split("[Ee]");


        if (components[1].charAt(0) == '+') {
            components[1] = components[1].substring(1);
        }

        if (Integer.parseInt(components[1]) > 24) {
            return (new BigDecimal(BigInteger.valueOf((long) Double.parseDouble(components[0])),
                    Integer.parseInt(components[1]) - 24)).multiply(BigDecimal.TEN.pow(-24));
        }
        else if (Integer.parseInt(components[1]) < -24) {
            return null;
        }
        else {
            return (new BigDecimal(components[0])).multiply(BigDecimal.valueOf(Double.parseDouble(components[1])));
        }*/


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

        return newMass.toEngineeringString() + " " +  SI_VALUE_UNITS.get(exponent).name();
    }

    public BigDecimal getMass(Unit targetUnit) {
        return mass.divide(power(BigDecimal.TEN, SI_UNIT_VALUES.get(targetUnit)), MathContext.DECIMAL64);
    }


    public static BigDecimal power(BigDecimal base, int n) {
        if (n < 0) {
            return BigDecimal.ONE.divide(base.pow(n * -1), MathContext.DECIMAL128);
        }
        else {
            return base.pow(n);
        }
    }

    @Override
    public String toString() {
        return this.getMass();
    }

    public enum Unit {
        Yg, Zg, Eg, Pg, Tg, Gg, Mg, tonne, mt, kg, g, mg, µg, ng, pg, fg, ag, zg, yg
    }

    public static int getSIIndexFromMassUnit(Unit unit) {
        return SI_UNIT_VALUES.get(unit);
    }

    private static HashMap<Unit, Integer> SI_UNIT_VALUES = new HashMap<>();
    private static HashMap<Integer, Unit> SI_VALUE_UNITS = new HashMap<>();
    static {
        SI_UNIT_VALUES.put(Unit.Yg, 24);
        SI_UNIT_VALUES.put(Unit.Zg, 21);
        SI_UNIT_VALUES.put(Unit.Eg, 18);
        SI_UNIT_VALUES.put(Unit.Pg, 15);
        SI_UNIT_VALUES.put(Unit.Tg, 12);
        SI_UNIT_VALUES.put(Unit.Gg, 9);
        SI_UNIT_VALUES.put(Unit.tonne, 6);
        SI_UNIT_VALUES.put(Unit.mt, 6);
        SI_UNIT_VALUES.put(Unit.Mg, 6);
        SI_UNIT_VALUES.put(Unit.kg, 3);
        SI_UNIT_VALUES.put(Unit.g, 0);
        SI_UNIT_VALUES.put(Unit.mg, -3);
        SI_UNIT_VALUES.put(Unit.µg, -6);
        SI_UNIT_VALUES.put(Unit.ng, -9);
        SI_UNIT_VALUES.put(Unit.pg, -12);
        SI_UNIT_VALUES.put(Unit.fg, -15);
        SI_UNIT_VALUES.put(Unit.ag, -18);
        SI_UNIT_VALUES.put(Unit.zg, -21);
        SI_UNIT_VALUES.put(Unit.yg, -24);

        for (HashMap.Entry<Unit, Integer> entry : SI_UNIT_VALUES.entrySet()) {
            SI_VALUE_UNITS.put(entry.getValue(), entry.getKey());
        }
        SI_UNIT_VALUES.put(Unit.mt, 6);
    }

    // Mass in g
    private BigDecimal mass;
}
