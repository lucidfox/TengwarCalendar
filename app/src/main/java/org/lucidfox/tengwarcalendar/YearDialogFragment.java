package org.lucidfox.tengwarcalendar;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lucidfox.tengwar.Numerals;
import org.lucidfox.tengwarcalendar.databinding.FragmentYearDialogBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class YearDialogFragment extends DialogFragment {
    public static final String EXTRA_YEAR = YearDialogFragment.class.getPackage().getName() + ".year";

    public static YearDialogFragment newInstance(int year) {
        YearDialogFragment fragment = new YearDialogFragment();

        Bundle args = new Bundle();
        args.putInt(EXTRA_YEAR, year);
        fragment.setArguments(args);

        return fragment;
    }

    private Callbacks callbacks;
    private FragmentYearDialogBinding ui;
    private int year;

    public YearDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            year = getArguments().getInt(EXTRA_YEAR);
        } else {
            year = savedInstanceState.getInt(EXTRA_YEAR);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ui = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_year_dialog, null, false);
        ui.yearView.setTypeface(Assets.get(getActivity()).getRegularFont());
        update();

        ui.plusButton.setOnClickListener(view -> {
            year++;
            update();
        });

        ui.minusButton.setOnClickListener(view -> {
            year--;
            update();
        });

        return new AlertDialog.Builder(getActivity())
                .setView(ui.getRoot())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    callbacks.onYearSelected(year);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();
    }

    private void update() {
        ui.yearView.setText(Numerals.toTengwarString(year));
    }

    public interface Callbacks {
        void onYearSelected(int year);
    }
}
