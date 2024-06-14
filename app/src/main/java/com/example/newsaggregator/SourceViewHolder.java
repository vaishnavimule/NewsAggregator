package com.example.newsaggregator;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SourceViewHolder {
    TextView name;

    public SourceViewHolder(@NonNull View itemView) {
        name = itemView.findViewById(R.id.sourceItemTextView);
    }
}
