/*
 * Decompiled with CFR 0_122.
 */
package org.lucidfox.tengwarcalendar;

import org.threeten.bp.Month;

public final class MonthWrapper implements Comparable<MonthWrapper> {
    private final Month month;
    private final String displayName;

    MonthWrapper(Month month, String displayName) {
        this.month = month;
        this.displayName = displayName;
    }

    public Month getMonth() {
        return this.month;
    }

    @Override
    public int compareTo(MonthWrapper o) {
        return this.month.compareTo(o.month);
    }

    public String toString() {
        return this.displayName;
    }
}

