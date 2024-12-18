package it.rizzoli.eattogether.ui.create;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.activity.MainActivity;


public class CreateFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create, container, false);

        ImageView imageView = root.findViewById(R.id.imageView);
        Button selectImageButton = root.findViewById(R.id.uploadImage);

        var pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    imageView.setImageURI(result);
                    imageView.setVisibility(View.VISIBLE);
                    selectImageButton.setVisibility(View.GONE);

                }
            }
        });

        selectImageButton.setOnClickListener(v -> pickImage.launch("image/*"));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}