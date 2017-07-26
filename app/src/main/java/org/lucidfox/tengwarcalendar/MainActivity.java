package org.lucidfox.tengwarcalendar;

import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.lucidfox.tengwar.Numerals;
import org.lucidfox.tengwarcalendar.databinding.ActivityMainBinding;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.YearMonth;
import org.threeten.bp.ZoneOffset;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements YearDialogFragment.Callbacks, MonthDialogFragment.Callbacks {
    private static final String KEY_YEAR_MONTH = "yearMonth";

    private YearMonth yearMonth = YearMonth.now(ZoneOffset.UTC);
    private ActivityMainBinding ui;
    private Map<DayOfWeek, TextView> dayHeaders = new EnumMap<>(DayOfWeek.class);
    private List<Map<DayOfWeek, TextView>> dayCells = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_YEAR_MONTH, yearMonth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            yearMonth = (YearMonth) savedInstanceState.getSerializable(KEY_YEAR_MONTH);
        }

        Assets assets = Assets.get(this);
        Typeface regularFont = assets.getRegularFont();
        Typeface boldFont = assets.getBoldFont();

        ui = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ui.monthView.setTypeface(assets.getRegularFont());
        ui.yearView.setTypeface(assets.getRegularFont());

        ui.monthView.setOnClickListener(view -> {
            MonthDialogFragment dialog = MonthDialogFragment.newInstance(yearMonth.getMonth());
            dialog.show(getSupportFragmentManager(), "month");
        });

        ui.yearView.setOnClickListener(view -> {
            YearDialogFragment dialog = YearDialogFragment.newInstance(yearMonth.getYear());
            dialog.show(getSupportFragmentManager(), "year");
        });

        for (DayOfWeek day : DayOfWeek.values()) {
            TextView dayHeader = new TextView(this);
            dayHeader.setTypeface(boldFont);
            dayHeader.setGravity(Gravity.CENTER);
            dayHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            dayHeader.setText(assets.getDayOfWeekName(day));
            dayHeader.setSingleLine(true);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setBackground(getWindow().getDecorView().getBackground());

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 0.2f);
            params.setMargins(2, 2, 2, 2);
            ui.calendarGrid.addView(dayHeader, params);

            dayHeaders.put(day, dayHeader);
        }

        for (int i = 0; i < 6; i++) {
            Map<DayOfWeek, TextView> rowMap = new EnumMap<>(DayOfWeek.class);

            for (DayOfWeek day : DayOfWeek.values()) {
                TextView dayCell = new TextView(this);
                dayCell.setTypeface(regularFont);
                dayCell.setGravity(Gravity.CENTER);
                dayCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                dayCell.setSingleLine(true);
                dayCell.setPadding(8, 8, 8, 8);
                dayCell.setBackground(getWindow().getDecorView().getBackground());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(2, 2, 2, 2);
                ui.calendarGrid.addView(dayCell, params);

                rowMap.put(day, dayCell);
            }

            dayCells.add(rowMap);
        }

        update();
    }

    @Override
    public void onYearSelected(int year) {
        yearMonth = yearMonth.withYear(year);
        update();
    }

    @Override
    public void onMonthSelected(Month month) {
        yearMonth = yearMonth.withMonth(month.getValue());
        update();
    }

    private void update() {
        int year = yearMonth.getYear();
        Month month = yearMonth.getMonth();
        int numWeeks = 1;

        for (LocalDate date = LocalDate.of(year, month, 2); date.getMonth() == month; date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                ++numWeeks;
            }
        }

        ui.monthView.setText(Assets.get(this).getMonthName(yearMonth.getMonth()));
        ui.yearView.setText(Numerals.toTengwarString(yearMonth.getYear()));

        // First, clear all text fields
        for (int i = 0; i < 6; i++) {
            Map<DayOfWeek, TextView> cellMap = dayCells.get(i);

            for (TextView view : cellMap.values()) {
                view.setText("");

                GridLayout.LayoutParams params = (GridLayout.LayoutParams) view.getLayoutParams();

                /*
                if (i < numWeeks) {
                    params.setMargins(2, 2, 2, 2);
                } else {
                    params.setMargins(0, 0, 0, 0);
                }
                */
            }
        }

        // Now fill days for selected year/month
        int weekRow = 0;

        for (LocalDate date = LocalDate.of(year, month, 1); date.getMonth() == month; date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.MONDAY && date.getDayOfMonth() > 1) {
                weekRow++;
            }

            dayCells.get(weekRow).get(dayOfWeek).setText(Numerals.toTengwarString(date.getDayOfMonth()));
        }
    }
}
