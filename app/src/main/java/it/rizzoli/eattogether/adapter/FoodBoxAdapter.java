package it.rizzoli.eattogether.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.FoodBox;
import it.rizzoli.eattogether.ui.food_box.FoodBoxFragment;

public class FoodBoxAdapter extends BaseAdapter {
    private Context context;
    private List<FoodBox> foodBoxes;
    private LayoutInflater inflater;
    private FragmentTransactionListener fragmentTransactionListener;

    public FoodBoxAdapter(Context context, List<FoodBox> foodBoxes, FragmentTransactionListener listener) {
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

    @SuppressLint("Range")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_food_box_list, parent, false);
        }

        TextView foodBoxName = convertView.findViewById(R.id.foodBoxName);
        TextView foodBoxCreator = convertView.findViewById(R.id.foodBoxCreator);
        DatabaseHelper dbHelper =  new DatabaseHelper(parent.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM Users WHERE _id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(foodBoxes.get(position).getIdUserAdder()) });

        if (cursor.moveToFirst()) {
            foodBoxCreator.setText(cursor.getString(cursor.getColumnIndex("username")));
        }
        cursor.close();
        foodBoxName.setText(foodBoxes.get(position).getNome());

        convertView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("foodBoxId", foodBoxes.get(position).getId());

            FoodBoxFragment fragmentFoodBox = new FoodBoxFragment();
            fragmentFoodBox.setArguments(bundle);

            if (fragmentTransactionListener != null) {
                fragmentTransactionListener.onFoodBoxClick(fragmentFoodBox);
            }
        });

        return convertView;
    }

    public interface FragmentTransactionListener {
        void onFoodBoxClick(FoodBoxFragment fragmentFoodBox);
    }
}