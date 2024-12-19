package it.rizzoli.eattogether.ui.event;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.List;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.FoodBoxAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;

import android.widget.ListView;
import android.widget.Toast;

import it.rizzoli.eattogether.database.entity.FoodBox;
import it.rizzoli.eattogether.ui.food_box.FoodBoxFragment;


public class EventFragment extends Fragment implements FoodBoxAdapter.FragmentTransactionListener {
    private ImageView eventImage;
    private TextView eventName, eventDate, eventOrganizer, eventTime, eventDescription, eventAddress;
    private DatabaseHelper databaseHelper;
    private ListView foodBoxListView;

    private Button btnAddFoodBox;

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
        eventImage = root.findViewById(R.id.eventImage);
        btnAddFoodBox = root.findViewById(R.id.btnAddFoodBox);

        databaseHelper = new DatabaseHelper(getContext());
        foodBoxListView = root.findViewById(R.id.foodBoxList);
        int eventId;
        Event event;
        Bundle bundle = getArguments();
        List<FoodBox> foodBoxes;
        FoodBoxAdapter foodBoxAdapter;
        if (bundle != null) {
            eventId = bundle.getInt("selectedEvent", 1);

            foodBoxes = databaseHelper.getFoodBoxesForEvent(eventId);

            foodBoxAdapter = new FoodBoxAdapter(getContext(), foodBoxes, this);
            foodBoxListView.setAdapter(foodBoxAdapter);

            event = databaseHelper.getEventById(eventId);
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
                eventTime.setText(event.getOra().substring(0,5));
                eventDescription.setText(event.getDescrizione());
                eventAddress.setText(event.getIndirizzo() + ", " + event.getCitta());
                if(event.hasImage()) {
                    eventImage.setImageBitmap(BitmapFactory.decodeByteArray(event.getImage(), 0, event.getImage().length));
                }
            }
        } else {
            foodBoxAdapter = null;
            foodBoxes = Collections.emptyList();
            event = null;
            eventId = -1;
        }

        btnAddFoodBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodSelectionDialog();
            }

            private void showFoodSelectionDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a Food Box");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);

                final EditText name = new EditText(getContext());
                name.setHint("Name");
                layout.addView(name);

                final EditText description = new EditText(getContext());
                description.setHint("Description");
                layout.addView(description);

                builder.setView(layout);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    String nameValue = name.getText().toString().trim();
                    String descriptionValue = description.getText().toString().trim();

                    if (!nameValue.isEmpty() && !descriptionValue.isEmpty()) {
                            SQLiteDatabase db = databaseHelper.getReadableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("idUserAdder", event.getIdUserCreator());
                            values.put("idEvent", eventId);
                            values.put("nome", nameValue);
                            values.put("descrizione", descriptionValue);

                            long lastInsertId = db.insert("Food_Boxes", null, values);

                            System.out.println(lastInsertId);
                            Toast toast = Toast.makeText(getContext(), "Food Box added successfully", Toast.LENGTH_LONG);
                            toast.show();

                            foodBoxes.add(new FoodBox((int) lastInsertId, event.getIdUserCreator(), eventId, nameValue, descriptionValue));
                            assert foodBoxAdapter != null;
                            foodBoxAdapter.notifyDataSetChanged();
                    } else {
                        Toast toast = Toast.makeText(getContext(), "All fields must be filled in", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return root;
    }

    @Override
    public void onFoodBoxClick(FoodBoxFragment fragmentFoodBox) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragmentFoodBox)
                    .addToBackStack(null)
                    .commit();
        }
    }
}