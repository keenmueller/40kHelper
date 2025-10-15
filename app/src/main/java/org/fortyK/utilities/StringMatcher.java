package org.fortyK.utilities;

public class StringMatcher {

    public static boolean compareStringWithTolerance(String s1, String s2, int tolerance)
    {
        if (s1.equals(s2))
            return true;

        if (s1.length() != s2.length())
            return false;

        int differences = 0;
        for(int i = 0; i < s1.length(); i++)
        {
            if (s1.charAt(i) != s2.charAt(i))
                differences++;
        }
        return differences <= tolerance;
    }
}
