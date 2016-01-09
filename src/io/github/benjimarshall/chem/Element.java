package io.github.benjimarshall.chem;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Element {
    private Element() {

    };

    // Make deep copy
    public Element(Element element) {
        this.symbol = element.getSymbol();
        this.name = element.getName();
        this.atomicNumber = element.getAtomicNumber();
        this.massNumber = element.getMassNumber();
    }

    // Find the element in the periodic table based on symbol or name
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

    // Specify each field for the element object
    public Element(String symbol, String name, int atomicNumber, double massNumber) {
        this.symbol = symbol;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.massNumber = massNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        return Double.compare(element.getMassNumber(), getMassNumber()) == 0 &&
                getAtomicNumber() == element.getAtomicNumber() &&
                getSymbol().equals(element.getSymbol()) &&
                getName().equals(element.getName());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getSymbol().hashCode();
        result = 31 * result + getName().hashCode();
        temp = Double.doubleToLongBits(getMassNumber());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getAtomicNumber();
        return result;
    }

    // Getters for the Element
    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getMassNumber() {
        return massNumber;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    // Fields of the class
    private String symbol;      // Eg. Na
    private String name;        // Eg. Sodium
    private double massNumber;  // Eg. 22.9898...
    private int atomicNumber;   // Eg. 11

    // Flags for constructors
    public static final int SYMBOL = 0;
    public static final int NAME = 1;

    // Make the periodic table
    private static ArrayList<Element> periodicTable = new ArrayList<>();
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
                        Double.parseDouble(line[3]) // Mass Number
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
