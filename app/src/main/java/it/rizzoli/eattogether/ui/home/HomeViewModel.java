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

    public HomeViewModel(DatabaseHelper databaseHelper, int userId) {
        this.databaseHelper = databaseHelper;
        loadEvents(userId);
    }

    private void loadEvents(int userId) {
        List<Event> events = databaseHelper.getEvents(userId);
        eventsList.setValue(events);
    }

    public LiveData<List<Event>> getEvents() {
        return eventsList;
    }
}
