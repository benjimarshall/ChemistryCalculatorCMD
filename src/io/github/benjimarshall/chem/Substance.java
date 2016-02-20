package io.github.benjimarshall.chem;


import java.math.MathContext;

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

    public Mass getMass() {
        return mass;
    }

    public Mole getMoles() {
        return moles;
    }

    @Override
    public String toString(){
        return moles.toString() + " of " + super.toString();
    }

    protected Mass mass;
    protected Mole moles;
}
