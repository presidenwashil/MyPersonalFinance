package com.example.mypersonalfinance;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FinanceActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText amountEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private ListView transactionsListView;
    private TransactionAdapter adapter;
    private ArrayList<Transaction> transactionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        dbHelper = new DatabaseHelper(this);
        amountEditText = findViewById(R.id.amount);
        descriptionEditText = findViewById(R.id.description);
        dateEditText = findViewById(R.id.date);
        transactionsListView = findViewById(R.id.transactions_list);
        transactionsList = new ArrayList<>();
        adapter = new TransactionAdapter(this, transactionsList);
        transactionsListView.setAdapter(adapter);

        Button addIncomeButton = findViewById(R.id.add_income_button);
        Button addExpenseButton = findViewById(R.id.add_expense_button);

        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction("income");
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction("expense");
            }
        });

        loadTransactions();
    }

    private void addTransaction(String type) {
        String amountStr = amountEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();

        if (amountStr.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        boolean result = dbHelper.addTransaction(amount, description, date, type);

        if (result) {
            Toast.makeText(this, "Transaction added", Toast.LENGTH_SHORT).show();
            loadTransactions();
        } else {
            Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTransactions() {
        transactionsList.clear();
        Cursor cursor = dbHelper.getAllTransactions();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                transactionsList.add(new Transaction(id, amount, description, date, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}