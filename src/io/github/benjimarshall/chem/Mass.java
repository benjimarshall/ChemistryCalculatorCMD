package io.github.benjimarshall.chem;


import java.math.BigDecimal;
import java.math.MathContext;

public class Mass {

    private Mass() {

    }

    public Mass(BigDecimal mass) {
        this.mass = mass;
    }

    public Mass(BigDecimal mass , Unit units) {
        this(mass.multiply(BigDecimal.TEN.pow(indexToConvertMass(units))));
    }

    public Mass(double mass, Unit units) {
        this(BigDecimal.valueOf(mass), units);
    }

    // If no units are provided, assume grams so do nothing
    public Mass(double mass) {
        this(BigDecimal.valueOf(mass));
    }

    private static int indexToConvertMass(Unit units) {
        int index = 0;
        switch (units) {
            case Yg:
                index = 24;
                break;
            case Zg:
                index = 21;
                break;
            case Eg:
                index = 18;
                break;
            case Pg:
                index = 15;
                break;
            case Tg:
                index = 12;
                break;
            case Gg:
                index = 9;
                break;
            case Mg:
                index = 6;
                break;
            case tonne:
                index = 6;
                break;
            case kg:
                index = 3;
                break;
            case g:
                index = 0;
                break;
            case mg:
                index = -3;
                break;
            case ug:
                index = -6;
                break;
            case ng:
                index = -9;
                break;
            case pg:
                index = -12;
                break;
            case fg:
                index = -15;
                break;
            case ag:
                index = -18;
                break;
            case zg:
                index = -21;
                break;
            case yg:
                index = -28;
                break;
            default:
                break;
        }

        return index;
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

    @Override
    public String toString() {
        return this.getMassInGrams() + "g";
    }

    public enum Unit {
        Yg, Zg, Eg, Pg, Tg, Gg, Mg, tonne, kg, g, mg, ug, ng, pg, fg, ag, zg, yg
    }

    // Mass in g
    private BigDecimal mass;
}
