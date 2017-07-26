package org.lucidfox.tengwarcalendar;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Month;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by maia on 25.07.17.
 */

public class Assets {
    private static Assets INSTANCE;

    public static Assets get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Assets(context);
        }

        return INSTANCE;
    }

    private final Typeface regularFont;
    private final Typeface boldFont;
    private final Map<Month, String> monthNames = new EnumMap<>(Month.class);
    private final Map<DayOfWeek, String> dayNames = new EnumMap<>(DayOfWeek.class);

    private Assets(Context context) {
        AssetManager assets = context.getAssets();
        regularFont = Typeface.createFromAsset(assets, "fonts/tngan.ttf");
        boldFont = Typeface.createFromAsset(assets, "fonts/tnganb.ttf");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assets.open("months.properties"), "UTF-8"));
            Properties monthProps = new Properties();
            monthProps.load(br);

            for (Map.Entry<Object, Object> kvp :  monthProps.entrySet()) {
                monthNames.put(Month.valueOf(kvp.getKey().toString()),
                        ViewerUtils.parseGlyphNames(kvp.getValue().toString()));
            }

            br = new BufferedReader(new InputStreamReader(assets.open("days.properties"), "UTF-8"));
            Properties dayProps = new Properties();
            dayProps.load(br);

            for (Map.Entry<Object, Object> kvp :  dayProps.entrySet()) {
                dayNames.put(DayOfWeek.valueOf(kvp.getKey().toString()),
                        ViewerUtils.parseGlyphNames(kvp.getValue().toString()));
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Typeface getRegularFont() {
        return regularFont;
    }

    public Typeface getBoldFont() {
        return boldFont;
    }

    public String getMonthName(Month month) {
        return monthNames.get(month);
    }

    public String getDayOfWeekName(DayOfWeek day) {
        return dayNames.get(day);
    }
}
