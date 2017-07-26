/*
 * Decompiled with CFR 0_122.
 */
package org.lucidfox.tengwarcalendar;

import org.lucidfox.tengwar.Glyph;

final class ViewerUtils {
    ViewerUtils() {
    }

    static String parseGlyphNames(String glyphNames) {
        StringBuilder result = new StringBuilder();

        for (String glyphStr : glyphNames.split("\\s")) {
            Glyph glyph = Glyph.safeValueOf(glyphStr);

            if (glyph != null) {
                result.append(glyph.getKey());
            }
        }

        return result.toString();
    }
}

