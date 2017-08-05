package org.lucidfox.tengwarcalendar;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.threeten.bp.Month;

public class MainActivity extends FragmentActivity implements YearDialogFragment.Callbacks, MonthDialogFragment.Callbacks {
    private MainFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (MainFragment) fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = MainFragment.newInstance(null);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onYearSelected(int year) {
        fragment.onYearSelected(year);
    }

    @Override
    public void onMonthSelected(Month month) {
        fragment.onMonthSelected(month);
    }
}
