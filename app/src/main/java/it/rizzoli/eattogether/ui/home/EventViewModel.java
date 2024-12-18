package it.rizzoli.eattogether.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<List<String>> eventNames = new MutableLiveData<>();

    public EventViewModel() {
        eventNames.setValue(new ArrayList<>(Arrays.asList(
                "Festa di Natale", "Capodanno", "Compleanno di Marco"
        )));
    }

    public LiveData<List<String>> getEventNames() {
        return eventNames;
    }
}
