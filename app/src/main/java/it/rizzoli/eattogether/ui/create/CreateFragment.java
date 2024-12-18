package it.rizzoli.eattogether.ui.create;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import it.rizzoli.eattogether.activity.LoginActivity;
import it.rizzoli.eattogether.activity.MainActivity;
import it.rizzoli.eattogether.activity.SignUpActivity;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.entity.Event;


public class CreateFragment extends Fragment {

    EditText title;
    EditText address;
    EditText city;
    EditText description;
    TextView date;
    TextView time;
    Button create;
    Uri imgUri;

    @SuppressLint("Range")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create, container, false);

        ImageView imageView = root.findViewById(R.id.imageView);
        Button selectImage = root.findViewById(R.id.uploadImage);

        date = root.findViewById(R.id.data);
        time = root.findViewById(R.id.ora);
        title = root.findViewById(R.id.title);
        address = root.findViewById(R.id.address);
        city = root.findViewById(R.id.city);
        description = root.findViewById(R.id.description);
        create = root.findViewById(R.id.create);

        Calendar calendarDate = Calendar.getInstance();
        Calendar calendarTime = Calendar.getInstance();
        int hour = calendarDate.get(Calendar.HOUR_OF_DAY) + 1;
        int minute = calendarDate.get(Calendar.MINUTE);
        int year = calendarDate.get(Calendar.YEAR);
        int month = calendarDate.get(Calendar.MONTH);
        int day = calendarDate.get(Calendar.DAY_OF_MONTH);

        DatabaseHelper dbHelper =  new DatabaseHelper(root.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        var pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    imgUri = result;
                    imageView.setImageURI(result);
                    imageView.setVisibility(View.VISIBLE);
                    selectImage.setVisibility(View.GONE);

                    byte[] imgBLOB = Utility.getBytesFromUri(result, root.getContext());

                    System.out.println(imgBLOB);

                }
            }
        });

        selectImage.setOnClickListener(v -> pickImage.launch("image/*"));


        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    root.getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String d = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        date.setText(d);
                    }, year, month, day);

            calendarDate.set(year, month, day);
            datePickerDialog.getDatePicker().setMinDate(calendarDate.getTimeInMillis());
            datePickerDialog.show();
        });

        time.setOnClickListener(v -> {

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    root.getContext(),
                    (view, hourOfDay, minute1) -> {
                        String t = String.format("%02d:%02d", hourOfDay, minute1);
                        time.setText(t);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        create.setOnClickListener(view -> {
            if (isFill()) {
                int id = 0;
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);

                String sql = "SELECT * FROM USERS WHERE username = ?";
                String[] selectionArgs = new String[] { sharedPreferences.getString("session", "notFound") };
                Cursor cursor = db.rawQuery(sql, selectionArgs);

                if (cursor.moveToFirst()) {
                    id = cursor.getInt(cursor.getColumnIndex("_id"));

                }
                cursor.close();
                ContentValues values = null;
                if(imgUri == null) {
                    Event event = new Event(id, title.getText().toString(), date.getText().toString(),
                            time.getText().toString(), address.getText().toString(), city.getText().toString(),
                            description.getText().toString());
                    values = event.toContentValuesNoImg();
                } else {
                    Event event = new Event(id, title.getText().toString(), date.getText().toString(),
                            time.getText().toString(), address.getText().toString(), city.getText().toString(),
                            description.getText().toString(), Utility.getBytesFromUri(imgUri, root.getContext()));
                    values = event.toContentValuesWithImg();
                }
                db.insert("Events", null, values);
                Toast toast = Toast.makeText(root.getContext(), "Event added correctly", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(root.getContext(), "All fields must be filled in", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return root;
    }

    private Boolean isFill() {
        return !(title.getText().toString().equals("")
                || address.getText().toString().equals("")
                || city.getText().toString().equals("")
                || description.getText().toString().equals("")
                || date.getText().toString().equals("")
                || time.getText().toString().equals("")
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}