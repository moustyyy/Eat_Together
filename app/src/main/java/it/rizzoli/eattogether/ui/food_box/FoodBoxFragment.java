package it.rizzoli.eattogether.ui.food_box;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.FoodItemAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;
import java.util.List;

public class FoodBoxFragment extends Fragment {
    private RecyclerView foodItemsRecyclerView;
    private DatabaseHelper databaseHelper;
    private Button btnAddFood;
    public FoodBoxFragment() {
    }
    @Override
    @SuppressLint("Range")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_box, container, false);
        foodItemsRecyclerView = rootView.findViewById(R.id.foodItemsRecyclerView);
        databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Bundle bundle = getArguments();
        if (bundle != null) {
            int foodBoxId = getArguments().getInt("foodBoxId");
            int userId = databaseHelper.getIdUserAdderByFoodBoxId(foodBoxId);
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs",
                    Context.MODE_PRIVATE);
            String session = sharedPreferences.getString("session", "notFound");
            String sql = "SELECT * FROM Users WHERE username = ?";
            String[] selectionArgs = new String[] { session, };
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            int sessionId = -1;
            if (cursor.moveToFirst()) {
                sessionId = cursor.getInt(cursor.getColumnIndex("_id"));
            }
            btnAddFood = rootView.findViewById(R.id.btnAddFood);
            if(sessionId != userId) {
                btnAddFood.setVisibility(View.GONE);
            }
            List<String> foodItems = databaseHelper.getFoodItemsForFoodBox(foodBoxId);
            FoodItemAdapter adapter = new FoodItemAdapter(getContext(), foodItems);
            if (!(foodItems != null && !foodItems.isEmpty())) {
                Toast toast = Toast.makeText(rootView.getContext(), "Food Box List is empty", Toast.LENGTH_LONG);
                toast.show();
            }
            foodItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            foodItemsRecyclerView.setAdapter(adapter);
            btnAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFoodSelectionDialog();
                }
                private void showFoodSelectionDialog() {
                    List<String> foodNames = databaseHelper.getFoodNamesByFoodBox(foodBoxId);
                    ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foodNames);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Select a food");
                    builder.setAdapter(foodAdapter, (dialog, which) -> {
                        String selectedFood = foodNames.get(which);
                        showAddFoodToBoxDialog(selectedFood);
                    });
                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    ListView listView = dialog.getListView();
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = 800;
                    listView.setLayoutParams(params);
                }
                private void showAddFoodToBoxDialog(final String selectedFood) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Add food")
                            .setMessage("Do you want to add " + selectedFood + " to your food box?")
                            .setPositiveButton("add", (dialog, which) -> addFoodToFoodBox(selectedFood))
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                private void addFoodToFoodBox(String foodName) {

                    int foodId = databaseHelper.getFoodIdByFoodName(foodName);
                    boolean isAdded = databaseHelper.addFoodToFoodBox(foodBoxId, String.valueOf(foodId));
                    if (isAdded) {
                        Toast.makeText(getContext(), "Food added to food box with success!", Toast.LENGTH_SHORT).show();
                        foodItems.add(foodName);
                        adapter.notifyItemInserted(foodItems.size() - 1);
                    } else {
                        Toast.makeText(getContext(), "Error while adding food to food box.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return rootView;
    }
}
