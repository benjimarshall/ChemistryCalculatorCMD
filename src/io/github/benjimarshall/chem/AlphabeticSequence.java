package io.github.benjimarshall.chem;

import java.util.Iterator;

public class AlphabeticSequence implements Iterator<String> {
    public  static char[] alphabet = new char[26];
    // Generate an alphabet
    static{
        final String stringAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < 26; i++) {
            alphabet[i] = stringAlphabet.charAt(i);
        }
    }

    protected int position = -1;
    protected String prefix = "";

    public boolean hasNext() {
        return true;
    }

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

    public String next() {
        position++;
        return makeString(position, new StringBuilder()).toString();
    }
}
