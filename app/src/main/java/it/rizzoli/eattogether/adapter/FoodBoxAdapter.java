package it.rizzoli.eattogether.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.ui.food_boox.FragmentFoodBox;

public class FoodBoxAdapter extends BaseAdapter {
    private Context context;
    private List<String> foodBoxes;
    private LayoutInflater inflater;
    private FragmentTransactionListener fragmentTransactionListener;

    public FoodBoxAdapter(Context context, List<String> foodBoxes, FragmentTransactionListener listener) {
        this.context = context;
        this.foodBoxes = foodBoxes;
        this.inflater = LayoutInflater.from(context);
        this.fragmentTransactionListener = listener;
    }

    @Override
    public int getCount() {
        return foodBoxes.size();
    }

    @Override
    public Object getItem(int position) {
        return foodBoxes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_food_box_list, parent, false);
        }

        TextView foodBoxName = convertView.findViewById(R.id.foodBoxName);
        TextView foodBoxCreator = convertView.findViewById(R.id.foodBoxCreator);
        foodBoxName.setText(foodBoxes.get(position));

        convertView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("foodBoxId", position);

            FragmentFoodBox fragmentFoodBox = new FragmentFoodBox();
            fragmentFoodBox.setArguments(bundle);

            if (fragmentTransactionListener != null) {
                fragmentTransactionListener.onFoodBoxClick(fragmentFoodBox);
            }
        });

        return convertView;
    }

    public interface FragmentTransactionListener {
        void onFoodBoxClick(FragmentFoodBox fragmentFoodBox);
    }
}


