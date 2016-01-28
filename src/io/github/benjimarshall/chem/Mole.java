package io.github.benjimarshall.chem;

import java.math.BigDecimal;

public class Mole {
    private Mole() {

    }

    private Mole(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public static Mole valueOf(double quantity) {
        return new Mole(BigDecimal.valueOf(quantity));
    }

    public static Mole valueOf(BigDecimal quantity) {
        return new Mole(quantity);
    }

    public Mole add(Mole mole) {
        return new Mole(this.quantity.add(mole.getQuantity()));
    }

    public Mole subtract(Mole mole) {
        return new Mole(this.quantity.subtract(mole.getQuantity()));
    }

    public Mole multiply(Mole mole) {
        return new Mole(this.quantity.multiply(mole.getQuantity()));
    }

    public Mole divide(Mole mole) {
        return new Mole(this.quantity.divide(mole.getQuantity()));
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return this.quantity.toString() + "mol";
    }

    public BigDecimal getNumberofMolecules() {
        return AVOGADRO_CONST.multiply(this.quantity);
    }

    private BigDecimal quantity;
    public static final BigDecimal AVOGADRO_CONST  = BigDecimal.valueOf(6022, 20);
}
