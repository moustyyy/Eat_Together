package it.rizzoli.eattogether.ui.event;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.FoodBoxAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;

import android.widget.ListView;

import it.rizzoli.eattogether.ui.food_boox.FragmentFoodBox;


public class EventFragment extends Fragment implements FoodBoxAdapter.FragmentTransactionListener {
    private ImageView eventImage;
    private TextView eventName, eventDate, eventOrganizer, eventTime, eventDescription, eventAddress;
    private DatabaseHelper databaseHelper;
    private ListView foodBoxListView;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);

        eventImage = root.findViewById(R.id.eventImage);
        eventName = root.findViewById(R.id.eventName);
        eventDate = root.findViewById(R.id.eventDate);
        eventTime = root.findViewById(R.id.eventTime);
        eventOrganizer = root.findViewById(R.id.eventOrganizer);
        eventDescription = root.findViewById(R.id.eventDescription);
        eventAddress = root.findViewById(R.id.eventAddress);

        databaseHelper = new DatabaseHelper(getContext());
        foodBoxListView = root.findViewById(R.id.foodBoxList);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int eventId = bundle.getInt("selectedEvent", 1);

            List<String> existingFoodBoxes = databaseHelper.getFoodBoxesForEvent(eventId);
            if (existingFoodBoxes == null || existingFoodBoxes.isEmpty()) {
                databaseHelper.insertSampleFoodItemsForEvent(eventId);
            }

            List<String> foodBoxes = databaseHelper.getFoodBoxesForEvent(eventId);

            if (foodBoxes != null && !foodBoxes.isEmpty()) {
                FoodBoxAdapter foodBoxAdapter = new FoodBoxAdapter(getContext(), foodBoxes, this);
                foodBoxListView.setAdapter(foodBoxAdapter);
            } else {
                Log.d("EventFragment", "No food boxes available");
            }

            Event event = databaseHelper.getEventById(eventId);
            String username = "";
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            String sql = "SELECT * FROM Users WHERE _id = ?";
            String[] selectionArgs = new String[] { String.valueOf(event.getIdUserCreator()) };
            Cursor cursor = db.rawQuery(sql, selectionArgs);

            if (cursor.moveToFirst()) {
                username = cursor.getString(cursor.getColumnIndex("username"));
            }

            cursor.close();
            if (event != null) {
                eventName.setText(event.getNome());
                eventDate.setText(event.getData());
                eventOrganizer.setText(username);
                eventTime.setText(event.getOra());
                eventDescription.setText(event.getDescrizione());
                eventAddress.setText(event.getIndirizzo() + ", " + event.getCitta());
            }
        }

        return root;
    }

    @Override
    public void onFoodBoxClick(FragmentFoodBox fragmentFoodBox) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragmentFoodBox)
                    .addToBackStack(null)
                    .commit();
        }
    }
}



