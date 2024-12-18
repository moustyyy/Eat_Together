package it.rizzoli.eattogether.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.rizzoli.eattogether.R;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {
    private Context context;
    private List<String> foodItems;

    public FoodItemAdapter(Context context, List<String> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodItemViewHolder holder, int position) {
        String foodItem = foodItems.get(position);
        holder.foodName.setText(foodItem);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;

        public FoodItemViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
        }
    }
}

