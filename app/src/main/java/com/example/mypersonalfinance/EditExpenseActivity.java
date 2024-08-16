package com.example.mypersonalfinance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class EditExpenseActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DatabaseHelper(this);

        EditText amountEditText = findViewById(R.id.amount);
        EditText descriptionEditText = findViewById(R.id.description);
        EditText dateTrigger = findViewById(R.id.date_trigger);
        DatePicker datePicker = findViewById(R.id.date_picker);
        Button addExpenseButton = findViewById(R.id.add_expense_button);
        Button deleteExpenseButton = findViewById(R.id.delete_expense_button);

        // Get data from intent
        expenseId = getIntent().getIntExtra("EXPENSE_ID", -1);
        String amount = getIntent().getStringExtra("AMOUNT");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String date = getIntent().getStringExtra("DATE");

        // Set data to views
        amountEditText.setText(amount);
        descriptionEditText.setText(description);
        dateTrigger.setText(date);

        dateTrigger.setOnClickListener(v -> {
            if (datePicker.getVisibility() == View.GONE) {
                datePicker.setVisibility(View.VISIBLE);
            } else {
                datePicker.setVisibility(View.GONE);
            }
        });

        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            String newDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            dateTrigger.setText(newDate);
            datePicker.setVisibility(View.GONE);
        });

        addExpenseButton.setText("Update Expense");
        addExpenseButton.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString();
            String newDescription = descriptionEditText.getText().toString();
            String newDate = dateTrigger.getText().toString();

            if (amountStr.isEmpty() || newDescription.isEmpty() || newDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double newAmount = Double.parseDouble(amountStr);
            boolean isUpdated = dbHelper.updateTransaction(expenseId, newAmount, newDescription, newDate, "expense");

            if (isUpdated) {
                Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Failed to update expense", Toast.LENGTH_SHORT).show();
            }
        });

        deleteExpenseButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    boolean isDeleted = dbHelper.deleteTransaction(expenseId);
                    if (isDeleted) {
                        Toast.makeText(this, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
        });
    }
}