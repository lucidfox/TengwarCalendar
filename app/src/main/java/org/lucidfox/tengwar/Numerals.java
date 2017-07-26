package org.lucidfox.tengwar;

import java.util.Map;

public final class Numerals {
    private static final Map<Integer, Glyph> DIGIT_GLYPHS = new java.util.HashMap();

    static {
        DIGIT_GLYPHS.put(Integer.valueOf(0), Glyph.DIGIT_0);
        DIGIT_GLYPHS.put(Integer.valueOf(1), Glyph.DIGIT_1);
        DIGIT_GLYPHS.put(Integer.valueOf(2), Glyph.DIGIT_2);
        DIGIT_GLYPHS.put(Integer.valueOf(3), Glyph.DIGIT_3);
        DIGIT_GLYPHS.put(Integer.valueOf(4), Glyph.DIGIT_4);
        DIGIT_GLYPHS.put(Integer.valueOf(5), Glyph.DIGIT_5);
        DIGIT_GLYPHS.put(Integer.valueOf(6), Glyph.DIGIT_6);
        DIGIT_GLYPHS.put(Integer.valueOf(7), Glyph.DIGIT_7);
        DIGIT_GLYPHS.put(Integer.valueOf(8), Glyph.DIGIT_8);
        DIGIT_GLYPHS.put(Integer.valueOf(9), Glyph.DIGIT_9);
    }

    public Numerals() {
    }

    public static String toTengwarString(int number) {
        if (number == 10)
            return Character.toString(Glyph.DIGIT_10.getKey());
        if (number == 11) {
            return Character.toString(Glyph.DIGIT_11.getKey());
        }

        int temp = number;
        StringBuilder sb = new StringBuilder();

        do {
            int digit = temp % 10;
            Glyph glyph = (Glyph) DIGIT_GLYPHS.get(Integer.valueOf(digit));
            sb.append(glyph.getKey());
            temp /= 10;
        } while (temp != 0);

        return sb.toString();
    }
}
