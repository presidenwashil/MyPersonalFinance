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

public class ExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ExpenseAdapter adapter;
    private ActivityResultLauncher<Intent> addExpenseLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());
        ListView listView = view.findViewById(R.id.expense_list);
        Cursor cursor = dbHelper.getExpense();
        adapter = new ExpenseAdapter(getContext(), cursor);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            Cursor itemCursor = (Cursor) parent.getItemAtPosition(position);
            int expenseId = itemCursor.getInt(itemCursor.getColumnIndexOrThrow("_id"));
            String amount = itemCursor.getString(itemCursor.getColumnIndexOrThrow("amount"));
            String description = itemCursor.getString(itemCursor.getColumnIndexOrThrow("description"));
            String date = itemCursor.getString(itemCursor.getColumnIndexOrThrow("date"));

            Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
            intent.putExtra("EXPENSE_ID", expenseId);
            intent.putExtra("AMOUNT", amount);
            intent.putExtra("DESCRIPTION", description);
            intent.putExtra("DATE", date);
            addExpenseLauncher.launch(intent);
        });


        addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    refreshListView();
                }
            }
        );

        FloatingActionButton fab = view.findViewById(R.id.fab_add_expense);
        fab.setOnClickListener(v -> addExpenseLauncher.launch(new Intent(getActivity(), AddExpenseActivity.class)));

        return view;
    }

    private void refreshListView() {
        Cursor cursor = dbHelper.getExpense();
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }
}