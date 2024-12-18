package it.rizzoli.eattogether.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.rizzoli.eattogether.R;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("session", "notFound");

        TextView username = root.findViewById(R.id.profileUserName);
        username.setText("Benvenuto, " + session + "!");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}