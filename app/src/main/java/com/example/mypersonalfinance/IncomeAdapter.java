package com.example.mypersonalfinance;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class IncomeAdapter extends CursorAdapter {
    public IncomeAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView amountTextView = view.findViewById(R.id.amount);
        TextView descriptionTextView = view.findViewById(R.id.description);
        TextView dateTextView = view.findViewById(R.id.date);
        TextView typeTextView = view.findViewById(R.id.type);

        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

        amountTextView.setText(String.valueOf(amount));
        descriptionTextView.setText(description);
        dateTextView.setText(date);
        typeTextView.setText(type);
    }
}