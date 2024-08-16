package com.example.mypersonalfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddExpenseActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

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

        dateTrigger.setOnClickListener(v -> {
            if (datePicker.getVisibility() == View.GONE) {
                datePicker.setVisibility(View.VISIBLE);
            } else {
                datePicker.setVisibility(View.GONE);
            }
        });

        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            dateTrigger.setText(date);
            datePicker.setVisibility(View.GONE);
        });

        addExpenseButton.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String date = dateTrigger.getText().toString();

            if (amountStr.isEmpty() || description.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            boolean isInserted = dbHelper.addTransaction(amount, description, date, "expense");

            if (isInserted) {
                Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        });
    }
}