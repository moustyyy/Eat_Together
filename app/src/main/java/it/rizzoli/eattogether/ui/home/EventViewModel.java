package it.rizzoli.eattogether.ui.home;// EventViewModel.java

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.rizzoli.eattogether.database.DatabaseHelper;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<List<String>> eventNames = new MutableLiveData<>();

    private final DatabaseHelper databaseHelper;

    public EventViewModel(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        loadEventNames();
    }

    private void loadEventNames() {
        List<String> events = databaseHelper.getEventNames();
        eventNames.setValue(events);
    }

    public LiveData<List<String>> getEventNames() {
        return eventNames;
    }
}
