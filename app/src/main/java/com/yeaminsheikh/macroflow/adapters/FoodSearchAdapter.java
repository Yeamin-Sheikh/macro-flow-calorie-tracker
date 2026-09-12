package com.yeaminsheikh.macroflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeaminsheikh.macroflow.R;
import com.yeaminsheikh.macroflow.models.FoodItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for browsing and selecting food items from the verified database.
 */
public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.ViewHolder> {

    public interface OnFoodSelectedListener {
        void onFoodSelected(FoodItem food);
    }

    private List<FoodItem> items = new ArrayList<>();
    private final OnFoodSelectedListener listener;

    public FoodSearchAdapter(OnFoodSelectedListener listener) {
        this.listener = listener;
    }

    public void setItems(List<FoodItem> list) {
        this.items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_search, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = items.get(position);
        holder.tvName.setText(item.getName());
        String meta = String.format(Locale.US, "Per %s • P: %.1fg | C: %.1fg | F: %.1fg",
                item.getServingUnit(), item.getProteinGrams(), item.getCarbsGrams(), item.getFatGrams());
        holder.tvMeta.setText(meta);
        holder.tvCalories.setText(String.format(Locale.US, "%d kcal", item.getCaloriesPerServing()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvMeta;
        TextView tvCalories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_search_food_name);
            tvMeta = itemView.findViewById(R.id.tv_search_food_meta);
            tvCalories = itemView.findViewById(R.id.tv_search_food_calories);
        }
    }
}
