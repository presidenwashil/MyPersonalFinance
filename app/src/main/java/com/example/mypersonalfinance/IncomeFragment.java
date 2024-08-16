package com.example.mypersonalfinance;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IncomeFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private IncomeAdapter adapter;
    private ActivityResultLauncher<Intent> addIncomeLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        dbHelper = new DatabaseHelper(getContext());
        ListView listView = view.findViewById(R.id.income_list);
        Cursor cursor = dbHelper.getIncome();
        adapter = new IncomeAdapter(getContext(), cursor);
        listView.setAdapter(adapter);

        addIncomeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    refreshListView();
                }
            }
        );

        FloatingActionButton fab = view.findViewById(R.id.fab_add_income);
        fab.setOnClickListener(v -> addIncomeLauncher.launch(new Intent(getActivity(), AddIncomeActivity.class)));

        return view;
    }

    private void refreshListView() {
        Cursor cursor = dbHelper.getIncome();
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }
}