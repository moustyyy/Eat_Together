package it.rizzoli.eattogether.ui.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.EventsAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;
import it.rizzoli.eattogether.ui.event.EventFragment;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsAdapter eventAdapter;
    private DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        databaseHelper = new DatabaseHelper(getContext());

        HomeViewModel homeViewModel = new HomeViewModel(databaseHelper);

        homeViewModel.getEvents().observe(getViewLifecycleOwner(), eventNames -> {
            eventAdapter = new EventsAdapter(eventNames, this::navigateToEventDetails);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(eventAdapter);
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
