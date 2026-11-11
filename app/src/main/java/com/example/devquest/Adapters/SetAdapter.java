package com.example.devquest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devquest.Activities.QuestionActivity;
import com.example.devquest.databinding.ItemSetsBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devquest.Models.SetModel;
import com.example.devquest.R;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    Context context;
    ArrayList<SetModel> list;

    public SetAdapter(Context context, ArrayList<SetModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sets, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SetModel model = list.get(position);

        holder.binding.setName.setText(model.getSetName());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, QuestionActivity.class);
            intent.putExtra("set",model.getSetName());
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

         ItemSetsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemSetsBinding.bind(itemView);
        }
    }
}
