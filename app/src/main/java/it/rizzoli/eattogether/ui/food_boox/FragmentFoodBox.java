package it.rizzoli.eattogether.ui.food_boox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.FoodItemAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;

import java.util.List;

public class FragmentFoodBox extends Fragment {
    private RecyclerView foodItemsRecyclerView;
    private DatabaseHelper databaseHelper;

    public FragmentFoodBox() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_box, container, false);
        foodItemsRecyclerView = rootView.findViewById(R.id.foodItemsRecyclerView);
        databaseHelper = new DatabaseHelper(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            int foodBoxId = bundle.getInt("foodBoxId");

            List<String> foodItems = databaseHelper.getFoodItemsForFoodBox(foodBoxId);
            if (foodItems != null && !foodItems.isEmpty()) {
                FoodItemAdapter adapter = new FoodItemAdapter(getContext(), foodItems);
                foodItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                foodItemsRecyclerView.setAdapter(adapter);
            }
        }

        return rootView;
    }
}

