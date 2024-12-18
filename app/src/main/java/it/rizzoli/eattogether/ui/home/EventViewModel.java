package it.rizzoli.eattogether.ui.home;// EventViewModel.java

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> eventsList = new MutableLiveData<>();

    private final DatabaseHelper databaseHelper;

    public EventViewModel(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        loadEventNames();
    }

    private void loadEventNames() {
        List<Event> events = databaseHelper.getEvents();
        eventsList.setValue(events);
    }

    public LiveData<List<Event>> getEvents() {
        return eventsList;
    }
}
