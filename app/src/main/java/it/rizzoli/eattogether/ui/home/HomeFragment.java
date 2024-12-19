package it.rizzoli.eattogether.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.atomic.AtomicReference;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.EventAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;
import it.rizzoli.eattogether.ui.event.EventFragment;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private DatabaseHelper databaseHelper;

    @SuppressLint("Range")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_event_view);
        databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int id;
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("session", "notFound");
        String sql = "SELECT * FROM users WHERE username = ?";
        String[] selectionArgs = new String[] { session };
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor.moveToFirst()) {
            id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
        } else {
            id = -1;
        }
        cursor.close();
        AtomicReference<HomeViewModel> homeViewModel = new AtomicReference<>(new HomeViewModel(databaseHelper, id));

        homeViewModel.get().getEvents().observe(getViewLifecycleOwner(), events -> {
            eventAdapter = new EventAdapter(events, this::navigateToEventDetails);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(eventAdapter);
        });

        Button btnAddEvent = root.findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodSelectionDialog();
            }

            private void showFoodSelectionDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter an invitation code");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);

                final EditText inputCode = new EditText(getContext());
                inputCode.setHint("Invitation Code");
                layout.addView(inputCode);

                builder.setView(layout);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    String code = inputCode.getText().toString().trim();
                    if (!code.isEmpty()) {
                        //Toast.makeText(getContext(), "Code entered: " + code, Toast.LENGTH_SHORT).show();
                        Event e = databaseHelper.getEventById(Integer.parseInt(code));
                        if(e == null) {
                            Toast toast = Toast.makeText(getContext(), "Invitation code invalid", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            boolean check = databaseHelper.checkInvitation(e, id);
                            if(check) {
                                Toast toast = Toast.makeText(getContext(), "The event is already present", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                ContentValues values = new ContentValues();
                                values.put("idEvent", String.valueOf(e.getId()));
                                values.put("idUser", String.valueOf(id));
                                db.insert("Event_User", null, values);
                                Toast toast = Toast.makeText(getContext(), "Event added successfully", Toast.LENGTH_LONG);
                                toast.show();
                                homeViewModel.set(new HomeViewModel(databaseHelper, id));
                                homeViewModel.get().getEvents().observe(getViewLifecycleOwner(), events -> {
                                    eventAdapter.updateData(events);
                                });

                            }
                        }
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

    private void navigateToEventDetails(Event event) {
        EventFragment eventDetailsFragment = new EventFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("selectedEvent", event.getId());

        eventDetailsFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
