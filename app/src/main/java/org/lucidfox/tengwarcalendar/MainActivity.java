package org.lucidfox.tengwarcalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.YearMonth;

import java.util.Calendar;

public class MainActivity extends FragmentActivity implements YearDialogFragment.Callbacks, MonthDialogFragment.Callbacks {
    private static final String KEY_YEAR_MONTH = "yearMonth";

    private ViewPager viewPager;
    private MainFragment fragment;
    private YearMonth yearMonth;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_YEAR_MONTH, yearMonth);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.fragment_container);

        if (savedInstanceState == null) {
            Calendar cld = Calendar.getInstance();
            yearMonth = YearMonth.of(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH) + 1);
        } else {
            yearMonth = (YearMonth) savedInstanceState.getSerializable(KEY_YEAR_MONTH);
        }

        setYearMonth(yearMonth);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                // Do nothing
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int position = viewPager.getCurrentItem();

                    if (position != 1) {
                        setYearMonth(yearMonth.plusMonths(position - 1));
                    }
                }
            }
        });
    }

    @Override
    public void onYearSelected(int year) {
        setYearMonth(yearMonth.withYear(year));
    }

    @Override
    public void onMonthSelected(Month month) {
        setYearMonth(yearMonth.withMonth(month.getValue()));
    }

    private void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MainFragment.newInstance(yearMonth.plusMonths(position - 1));
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        viewPager.setCurrentItem(1, false);
    }
}
