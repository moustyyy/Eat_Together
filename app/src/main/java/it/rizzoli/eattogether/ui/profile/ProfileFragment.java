package it.rizzoli.eattogether.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.activity.LoginActivity;
import it.rizzoli.eattogether.activity.MainActivity;
import it.rizzoli.eattogether.activity.SignUpActivity;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("session", "notFound");

        TextView username = root.findViewById(R.id.profileUserName);
        username.setText(session);

        LinearLayout logout = root.findViewById(R.id.logout);

        logout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("session");
            editor.apply();

            Intent intent = new Intent(requireContext(), SignUpActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}