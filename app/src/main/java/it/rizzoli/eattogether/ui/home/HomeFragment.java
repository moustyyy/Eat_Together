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

import it.rizzoli.eattogether.adapter.EventAdapter;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EventViewModel eventViewModel;

    private DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerView;
        databaseHelper = new DatabaseHelper(getContext());

        eventViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) new EventViewModel(databaseHelper);
            }
        }).get(EventViewModel.class);

        eventViewModel.getEventNames().observe(getViewLifecycleOwner(), eventNames -> {
            eventAdapter = new EventAdapter(eventNames);
            recyclerView.setAdapter(eventAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
