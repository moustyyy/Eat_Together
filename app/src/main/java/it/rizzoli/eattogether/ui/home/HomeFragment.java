package it.rizzoli.eattogether.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.adapter.EventsAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsAdapter eventAdapter;
    private DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        databaseHelper = new DatabaseHelper(getContext());

        EventViewModel eventViewModel = new EventViewModel(databaseHelper);

        eventViewModel.getEvents().observe(getViewLifecycleOwner(), eventNames -> {
            eventAdapter = new EventsAdapter(eventNames);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(eventAdapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
