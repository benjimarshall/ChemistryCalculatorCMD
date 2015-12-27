package io.github.benjimarshall.chem;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Element {
    public Element() {

    }

    public Element(Element element) {
        this.symbol = element.getSymbol();
        this.name = element.getName();
        this.atomicNumber = element.getAtomicNumber();
        this.massNumber = element.getMassNumber();
    }

    public Element(String name, int flag) {
        boolean successful = false;
        if (flag == SYMBOL) {
            for (Element element : periodicTable) {
                if (element.getSymbol() == name) {
                    this.symbol = element.getSymbol();
                    this.name = element.getName();
                    this.atomicNumber = element.getAtomicNumber();
                    this.massNumber = element.getMassNumber();
                    successful = true;
                    break;
                }
            }
        }
        else if (flag == NAME) {
            for (Element element : periodicTable) {
                if (element.getName() == name) {
                    this.symbol = element.getSymbol();
                    this.name = element.getName();
                    this.atomicNumber = element.getAtomicNumber();
                    this.massNumber = element.getMassNumber();
                    successful = true;
                    break;
                }
            }
        }

    }

    public Element(String symbol, String name, int atomicNumber, double massNumber) {
        this.symbol = symbol;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.massNumber = massNumber;
    }

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

    private String symbol;
    private String name;
    private double massNumber;
    private int atomicNumber;

    public final static int SYMBOL = 0;
    public final static int NAME = 1;

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
        catch (IOException e) {
            System.out.println("There has been an IO error in reading the periodic table. Program closing");
            System.exit(1);
        }
    }
}
