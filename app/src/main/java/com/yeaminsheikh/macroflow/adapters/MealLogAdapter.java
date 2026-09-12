package com.yeaminsheikh.macroflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeaminsheikh.macroflow.R;
import com.yeaminsheikh.macroflow.models.MealLogEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying logged meals and snacks.
 */
public class MealLogAdapter extends RecyclerView.Adapter<MealLogAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(MealLogEntry entry);
    }

    private List<MealLogEntry> entries = new ArrayList<>();
    private final OnDeleteClickListener deleteListener;

    public MealLogAdapter(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setEntries(List<MealLogEntry> list) {
        this.entries = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_log, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealLogEntry entry = entries.get(position);

        holder.tvFoodName.setText(entry.getFoodName());
        holder.tvMealTag.setText(entry.getMealType());

        String servingStr = String.format(Locale.US, "%.1f serving (%s)", entry.getServings(), entry.getServingUnit() != null ? entry.getServingUnit() : "portion");
        holder.tvServingInfo.setText(servingStr);

        holder.tvCalories.setText(String.format(Locale.US, "%d kcal", entry.getTotalCalories()));
        holder.tvProtein.setText(String.format(Locale.US, "P: %.1fg", entry.getTotalProtein()));
        holder.tvCarbs.setText(String.format(Locale.US, "C: %.1fg", entry.getTotalCarbs()));
        holder.tvFat.setText(String.format(Locale.US, "F: %.1fg", entry.getTotalFat()));

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(entry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;
        TextView tvMealTag;
        TextView tvServingInfo;
        TextView tvCalories;
        TextView tvProtein;
        TextView tvCarbs;
        TextView tvFat;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvMealTag = itemView.findViewById(R.id.tv_meal_tag);
            tvServingInfo = itemView.findViewById(R.id.tv_serving_info);
            tvCalories = itemView.findViewById(R.id.tv_item_calories);
            tvProtein = itemView.findViewById(R.id.tv_pill_protein);
            tvCarbs = itemView.findViewById(R.id.tv_pill_carbs);
            tvFat = itemView.findViewById(R.id.tv_pill_fat);
            btnDelete = itemView.findViewById(R.id.btn_delete_log);
        }
    }
}
