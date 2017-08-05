package org.lucidfox.tengwarcalendar;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.lucidfox.tengwar.Numerals;
import org.lucidfox.tengwarcalendar.databinding.ActivityMainBinding;
import org.lucidfox.tengwarcalendar.databinding.FragmentMainBinding;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.YearMonth;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maia on 05.08.17.
 */
public class MainFragment extends Fragment {
    private static final String KEY_YEAR_MONTH = "yearMonth";

    private final LocalDate today;
    private final Map<DayOfWeek, TextView> dayHeaders = new EnumMap<>(DayOfWeek.class);
    private final List<Map<DayOfWeek, TextView>> dayCells = new ArrayList<>();

    private YearMonth yearMonth;
    private FragmentMainBinding ui;
    private int contentBackground;

    public static MainFragment newInstance(YearMonth yearMonth) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_YEAR_MONTH, yearMonth);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        Calendar cld = Calendar.getInstance();
        today = LocalDate.of(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH) + 1, cld.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            yearMonth = (YearMonth) getArguments().getSerializable(KEY_YEAR_MONTH);
        }

        if (yearMonth == null) {
            yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Assets assets = Assets.get(getActivity());
        Typeface regularFont = assets.getRegularFont();
        Typeface boldFont = assets.getBoldFont();

        contentBackground = 0xfffafafa;

        ui = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        ui.monthView.setTypeface(assets.getRegularFont());
        ui.yearView.setTypeface(assets.getRegularFont());

        ui.monthView.setOnClickListener(view -> {
            MonthDialogFragment dialog = MonthDialogFragment.newInstance(yearMonth.getMonth());
            dialog.show(getActivity().getSupportFragmentManager(), "month");
        });

        ui.yearView.setOnClickListener(view -> {
            YearDialogFragment dialog = YearDialogFragment.newInstance(yearMonth.getYear());
            dialog.show(getActivity().getSupportFragmentManager(), "year");
        });

        for (DayOfWeek day : DayOfWeek.values()) {
            TextView dayHeader = new TextView(getActivity());
            dayHeader.setTypeface(boldFont);
            dayHeader.setGravity(Gravity.CENTER);
            dayHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            dayHeader.setText(assets.getDayOfWeekName(day));
            dayHeader.setSingleLine(true);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setBackgroundColor(contentBackground);

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
                TextView dayCell = new TextView(getActivity());
                dayCell.setTypeface(regularFont);
                dayCell.setGravity(Gravity.CENTER);
                dayCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                dayCell.setSingleLine(true);
                dayCell.setPadding(8, 8, 8, 8);
                dayCell.setBackgroundColor(contentBackground);

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
        return ui.getRoot();
    }

    public void onYearSelected(int year) {
        yearMonth = yearMonth.withYear(year);
        update();
    }

    public void onMonthSelected(Month month) {
        yearMonth = yearMonth.withMonth(month.getValue());
        update();
    }

    private void update() {
        int year = yearMonth.getYear();
        Month month = yearMonth.getMonth();

        /*
        int numWeeks = 1;

        for (LocalDate date = LocalDate.of(year, month, 2); date.getMonth() == month; date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                ++numWeeks;
            }
        }
        */

        ui.monthView.setText(Assets.get(getActivity()).getMonthName(yearMonth.getMonth()));
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

            TextView dayCell = dayCells.get(weekRow).get(dayOfWeek);
            dayCell.setText(Numerals.toTengwarString(date.getDayOfMonth()));

            if (date.equals(today)) {
                dayCell.setBackgroundColor(getResources().getColor(R.color.colorSelection));
            } else {
                dayCell.setBackgroundColor(contentBackground);
            }
        }
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }
}
