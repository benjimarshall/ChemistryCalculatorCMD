package io.github.benjimarshall.chem;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Chemical Element. A {@code Element} object consists of a name, symbol, an atomic mass and an atomic number
 *
 * @author Benji Marshall
 * @since 2016-2-2
 */
public class Element {
    private Element() {}

    /**
     * Makes a deep copy of the {@code Element} object
     * @param element the element to copy
     */
    public Element(Element element) {
        this.symbol = element.getSymbol();
        this.name = element.getName();
        this.atomicNumber = element.getAtomicNumber();
        this.massNumber = element.getMassNumber();
    }



    /**
     * Find a {@code Element} object in the periodic table based on symbol or name
     * @param name the symbol or name of the element (eg. {@code Na} or {@code Sodium})
     * @param flag whether {@code name} is a symbol {@link #SYMBOL} or a {@link #NAME}
     * @throws FlagException if the flag is not one of the two flags
     * @throws NotationInterpretationException if no element was found to match the name parameter
     */
    public Element(String name, int flag) throws FlagException, NotationInterpretationException {
        // One way flag to see if the element is found in the periodic table or not
        boolean successful = false;

        if (flag == SYMBOL) {
            // Check if the name looks valid
            if (!name.matches("[A-Z][a-z]?")) {
                throw new NotationInterpretationException("Symbol did not meet notation standards of capital letters " +
                        "and lower case letters: " + name);
            }

            // Loop through the periodic table
            for (Element element : periodicTable) {
                if (element.getSymbol().equals(name)) { // If the name and symbol match
                    // Copy across the data from the element
                    this.symbol = element.getSymbol();
                    this.name = element.getName();
                    this.atomicNumber = element.getAtomicNumber();
                    this.massNumber = element.getMassNumber();

                    // Note that the element has been found, and don't bother with the rest of the table
                    successful = true;
                    break;
                }
            }
        }
        else if (flag == NAME) {
            // Loop through the periodic table
            name = name.toLowerCase();
            for (Element element : periodicTable) {
                if (element.getName().toLowerCase().equals(name)) { // If the name and element's name match
                    // Copy across the data from the element
                    this.symbol = element.getSymbol();
                    this.name = element.getName();
                    this.atomicNumber = element.getAtomicNumber();
                    this.massNumber = element.getMassNumber();

                    // Note that the element has been found, and don't bother with the rest of the table
                    successful = true;
                    break;
                }
            }
        }
        // If the flag doesn't equal SYMBOL or NAME
        else {
            throw new FlagException("Flag was neither 0 nor 1, neither SYMBOL nor NAME");
        }

        // If the flag was valid but no element was found to match the "name" variable
        if (!successful) {
            throw new NotationInterpretationException("Couldn't find element or symbol in the periodic table");
        }

    }

    /**
     * Makes a custom {@code Element} object from a name and numerical parameters
     * @param symbol the symbol of the element (eg. {@code Na})
     * @param name the name of the element (eg. {@code Sodium})
     * @param atomicNumber the atomic number of the element (eg. {@code 11})
     * @param massNumber the relative atomic mass of the element (eg. {@code 22.9898})
     * @see #symbol
     * @see #name
     * @see #atomicNumber
     * @see #massNumber
     */
    public Element(String symbol, String name, int atomicNumber, BigDecimal massNumber) {
        this.symbol = symbol;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.massNumber = massNumber;
    }

    /**
     * A custom equals method, to compare elements
     * @param o the object to compare to this {@code Element} object
     * @return whether the two {@code Element} objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        return element.getMassNumber().compareTo(getMassNumber()) == 0 &&
                getAtomicNumber() == element.getAtomicNumber() &&
                getSymbol().equals(element.getSymbol()) &&
                getName().equals(element.getName());
    }

    /**
     * Generates the hash code of this {@code Element} object
     * @return the hash code of this {@code Element} object
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getSymbol().hashCode();
        result = 31 * result + getName().hashCode();
        temp = Double.doubleToLongBits(getMassNumber().doubleValue());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getAtomicNumber();
        return result;
    }


    /**
     * Gets the {@code Element} object's {@link #symbol}
     * @return the {@code Element} object's {@link #symbol}
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the {@code Element} object's {@link #name}
     * @return the {@code Element} object's {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the {@code Element} object's {@link #massNumber}
     * @return the {@code Element} object's {@link #massNumber}
     */
    public BigDecimal getMassNumber() {
        return massNumber;
    }

    /**
     * Gets the {@code Element} object's {@link #atomicNumber}
     * @return the {@code Element} object's {@link #atomicNumber}
     */
    public int getAtomicNumber() {
        return atomicNumber;
    }

    /**
     * Gets the {@code Element} object's {@link #symbol}
     * @return the {@code Element} object's {@link #symbol}
     */
    @Override
    public String toString() {
        return this.getSymbol();
    }

    /**
     * The symbol of the {@code Element} object. For example: {@code Na}
     */
    protected String symbol;

    /**
     * The name of the {@code Element} object. For example: {@code Sodium}
     */
    protected String name;

    /**
     * The atomic number of the {@code Element} object. For example: {@code 11}
     */
    protected BigDecimal massNumber;

    /**
     * The relative atomic mass of the {@code Element} object. For example: {@code 23.0}
     */
    protected int atomicNumber;   // Eg. 11

    /**
     * A flag for the {@link #Element(String, int)} constructor, for when the element's symbol is passed
     */
    public static final int SYMBOL = 0;

    /**
     * A flag for the {@link #Element(String, int)} constructor, for when the element's name is passed
     */
    public static final int NAME = 1;

    /**
     * A periodic table generated from a csv resource file
     */
    protected static ArrayList<Element> periodicTable = new ArrayList<>();
    static {
        try {
            // Make the CSV reader
            InputStream is = (new Element()).getClass().getResourceAsStream("/periodic_table.csv");
            CSVReader r = new CSVReader(new InputStreamReader(is));

            // Dump the header row
            r.readNext();

            // For each line make the respective element
            for (String line[]: r) {
                periodicTable.add(new Element(
                        line[1], // Symbol
                        line[2], // Name
                        Integer.parseInt(line[0]), // Atomic Number
                        BigDecimal.valueOf(Double.parseDouble(line[3])).setScale(1, RoundingMode.HALF_UP) // Mass Number
                ));
            }
            r.close();
        }
        // Catch the checked IOException which could be throw by the CSVReader
        catch (IOException e) {
            System.out.println("There has been an IO error in reading the periodic table. Program closing");
            System.exit(1);
        }
    }
}
