package it.rizzoli.eattogether.ui.create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.Utility;
import it.rizzoli.eattogether.activity.MainActivity;


public class CreateFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create, container, false);

        ImageView imageView = root.findViewById(R.id.imageView);
        Button selectImage = root.findViewById(R.id.uploadImage);

        TextView editTextDate = root.findViewById(R.id.data);
        TextView editTextTime = root.findViewById(R.id.ora);

        Calendar calendarDate = Calendar.getInstance();
        int hour = calendarDate.get(Calendar.HOUR_OF_DAY);
        int minute = calendarDate.get(Calendar.MINUTE);

        Calendar calendarTime = Calendar.getInstance();
        int year = calendarTime.get(Calendar.YEAR);
        int month = calendarTime.get(Calendar.MONTH);
        int day = calendarTime.get(Calendar.DAY_OF_MONTH);

        var pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    imageView.setImageURI(result);
                    imageView.setVisibility(View.VISIBLE);
                    selectImage.setVisibility(View.GONE);

                    byte[] imgBLOB = Utility.getBytesFromUri(result, root.getContext());

                    System.out.println(imgBLOB);

                }
            }
        });

        selectImage.setOnClickListener(v -> pickImage.launch("image/*"));


        editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    root.getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        editTextDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        editTextTime.setOnClickListener(v -> {

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    root.getContext(),
                    (view, hourOfDay, minute1) -> {
                        String time = String.format("%02d:%02d", hourOfDay, minute1);
                        editTextTime.setText(time);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}