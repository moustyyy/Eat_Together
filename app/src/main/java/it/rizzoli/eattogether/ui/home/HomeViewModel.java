package it.rizzoli.eattogether.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> eventsList = new MutableLiveData<>();

    private final DatabaseHelper databaseHelper;

    public HomeViewModel(DatabaseHelper databaseHelper) {
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
