package io.github.benjimarshall.chem;

import java.util.Iterator;

/**
 * An Alphabetic Sequence generator. For example: A, B, C, D, ..., Y, Z, AA, AB, AC, ..., AY, AZ, BA, BC, ... ZX, ZY,
 * ZZ, AAA, AAB, AAC, ...
 */
public class AlphabeticSequence implements Iterator<String> {
    /**
     * An array of characters of the alphabet, in order
     */
    public static char[] alphabet = new char[26];
    // Generate an alphabet
    static{
        final String stringAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < 26; i++) {
            alphabet[i] = stringAlphabet.charAt(i);
        }
    }

    private int position = -1;

    /**
     * Implements {@link Iterator}'s method to see if there is another word in the sequence, which there always will be
     * @return if there is another word in the sequence, {@code true}
     */
    public boolean hasNext() {
        return true;
    }

    /**
     * Finds the next element for this position in the sequence
     * @param prefix the position of the element in the sequence
     * @param suffix the characters after this in the element
     * @return the element in the sequence
     */
    private StringBuilder makeString(int prefix, StringBuilder suffix) {
        // If the number is more than 26, another letter is needed
        if (prefix >= 26)  {
            // So recur after dividing by 26
            suffix = makeString((prefix / 26) - 1, suffix);
        }

        // Find the last character this recursion is responsible for
        prefix %= 26;
        // Add that character to the string builder
        suffix.append(alphabet[prefix]);

        return suffix;
    }

    /**
     * Gets the next element in the sequence
     * @return the next element in the sequence
     */
    public String next() {
        position++;
        return makeString(position, new StringBuilder()).toString();
    }
}
