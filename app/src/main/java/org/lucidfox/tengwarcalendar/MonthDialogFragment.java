package org.lucidfox.tengwarcalendar;


import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lucidfox.tengwar.Numerals;
import org.lucidfox.tengwarcalendar.databinding.FragmentMonthDialogBinding;
import org.lucidfox.tengwarcalendar.databinding.FragmentYearDialogBinding;
import org.threeten.bp.Month;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthDialogFragment extends DialogFragment {
    public static final String EXTRA_MONTH = MonthDialogFragment.class.getPackage().getName() + ".month";

    public static MonthDialogFragment newInstance(Month month) {
        MonthDialogFragment fragment = new MonthDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MONTH, month);
        fragment.setArguments(args);

        return fragment;
    }

    private Callbacks callbacks;
    private FragmentMonthDialogBinding ui;
    private Month month;

    public MonthDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            month = (Month) getArguments().getSerializable(EXTRA_MONTH);
        } else {
            month = (Month) savedInstanceState.getSerializable(EXTRA_MONTH);
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
        ui = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_month_dialog, null, false);
        MonthAdapter adapter = new MonthAdapter(getActivity());
        ui.list.setAdapter(adapter);

        AlertDialog dlg = new AlertDialog.Builder(getActivity())
                .setView(ui.getRoot())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();

        ui.list.setOnItemClickListener((adapterView, view, i, l) -> {
            callbacks.onMonthSelected(adapter.getItem(i));
            dlg.dismiss();
        });

        return dlg;
    }

    private void update() {

    }

    public interface Callbacks {
        void onMonthSelected(Month month);
    }

    private static class MonthAdapter extends BaseAdapter {
        private final Activity activity;
        private final List<Month> months = Arrays.asList(Month.values());

        public MonthAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return months.size();
        }

        @Override
        public Month getItem(int i) {
            return months.get(i);
        }

        @Override
        public long getItemId(int i) {
            return months.get(i).getValue();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(activity).inflate(R.layout.list_item_month, viewGroup, false);
            }

            Assets assets = Assets.get(activity);
            TextView monthName = view.findViewById(R.id.month_name);
            monthName.setTypeface(assets.getRegularFont());
            monthName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            monthName.setText(assets.getMonthName(months.get(i)));

            return view;
        }
    }
}
