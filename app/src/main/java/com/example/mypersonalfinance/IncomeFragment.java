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
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);

        dbHelper = new DatabaseHelper(getContext());
        ListView listView = rootView.findViewById(R.id.income_list);
        Cursor cursor = dbHelper.getIncome();
        adapter = new IncomeAdapter(getContext(), cursor);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            Cursor itemCursor = (Cursor) parent.getItemAtPosition(position);
            int incomeId = itemCursor.getInt(itemCursor.getColumnIndexOrThrow("_id"));
            String amount = itemCursor.getString(itemCursor.getColumnIndexOrThrow("amount"));
            String description = itemCursor.getString(itemCursor.getColumnIndexOrThrow("description"));
            String date = itemCursor.getString(itemCursor.getColumnIndexOrThrow("date"));

            Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
            intent.putExtra("INCOME_ID", incomeId);
            intent.putExtra("AMOUNT", amount);
            intent.putExtra("DESCRIPTION", description);
            intent.putExtra("DATE", date);
            addIncomeLauncher.launch(intent);
        });

        addIncomeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    refreshListView();
                }
            }
        );

        FloatingActionButton fab = rootView.findViewById(R.id.fab_add_income);
        fab.setOnClickListener(v -> addIncomeLauncher.launch(new Intent(getActivity(), AddIncomeActivity.class)));

        return rootView;
    }

    private void refreshListView() {
        Cursor cursor = dbHelper.getIncome();
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }
}