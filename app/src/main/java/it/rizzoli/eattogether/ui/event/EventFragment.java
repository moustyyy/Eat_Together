package it.rizzoli.eattogether.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;
import it.rizzoli.eattogether.ui.home.HomeViewModel;

public class EventFragment extends Fragment {
    private ImageView eventImage;
    private TextView eventName, eventDate, eventUserCreator;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);

        eventImage = root.findViewById(R.id.eventImage);
        eventName = root.findViewById(R.id.eventName);
        eventDate = root.findViewById(R.id.eventDate);
        eventUserCreator = root.findViewById(R.id.eventUserCreator);

        databaseHelper = new DatabaseHelper(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            int eventId = bundle.getInt("selectedEvent", 1);

            if (eventId != -1) {
                Event event = databaseHelper.getEventById(eventId);
                if (event != null) {
                    eventName.setText(event.getNome());
                    eventDate.setText(event.getData());
                    eventUserCreator.setText(String.valueOf(event.getIdUserCreator()));
                }
            }
        }
        return root;
    }
}
