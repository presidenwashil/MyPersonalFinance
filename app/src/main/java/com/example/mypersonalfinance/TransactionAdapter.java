package com.example.mypersonalfinance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Transaction> transactions;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return transactions.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        }

        Transaction transaction = transactions.get(position);

        TextView amountTextView = convertView.findViewById(R.id.amount);
        TextView descriptionTextView = convertView.findViewById(R.id.description);
        TextView typeTextView = convertView.findViewById(R.id.type);

        amountTextView.setText(String.valueOf(transaction.getAmount()));
        descriptionTextView.setText(transaction.getDescription());
        typeTextView.setText(transaction.getType());

        return convertView;
    }
}