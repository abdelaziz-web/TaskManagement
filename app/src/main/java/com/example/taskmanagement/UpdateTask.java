package com.example.taskmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateTask extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db;
    private Button chooseDateButton;
    private Button chooseTimeButton;
    private ProgressBar progressBar;
    private TextView displaytime;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;

    private  Button chooseImageButton;
    private Uri selectedImageUri;
    private TextView displayDateTextView;
    private Calendar selectedDate;
    private String taskdate,tasktime;
    private String imageurl;

    Button update;
    EditText uptitle,updesc;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_task);
        db = FirebaseFirestore.getInstance();
        String taskid = getIntent().getStringExtra("taskid");
        chooseDateButton = findViewById(R.id.chooseDateButton);
        displayDateTextView = findViewById(R.id.displayDateTextView);
        selectedDate = Calendar.getInstance();
        uptitle = (EditText) findViewById(R.id.updatetitle);
        updesc = (EditText) findViewById(R.id.updatedesc);
        update = (Button) findViewById(R.id.updatenow);
        storage  = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("images");
        cancel = findViewById(R.id.cancel_update);

        chooseImageButton = findViewById(R.id.UpdateImageButton);
        chooseTimeButton = findViewById(R.id.chooseTimeButton);
        displaytime = findViewById(R.id.displayTimeTextView);
        progressBar = findViewById(R.id.progressBar);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UpdateTask.this,TasksActivity.class);
                startActivity(in);
            }
        });

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newt = uptitle.getText().toString();
                String newd = updesc.getText().toString();

                Map<String,Object> updates = new HashMap<>();



                if (!newt.equals("")) {
                    updates.put("title",newt);
                }
                if (!newd.equals("")) {
                    updates.put("desc",newd);
                }
                if (imageurl != null) {
                    updates.put("img",imageurl);
                }

                try {
                    Timestamp timestamp;
                    String dateString = taskdate+" "+tasktime;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.ENGLISH);
                    Date parsedDate = dateFormat.parse(dateString);

                    // Convert the parsed Date to a Timestamp
                    timestamp = new Timestamp(parsedDate);

                    // Now 'timestamp' holds the value in Timestamp format

                    if (timestamp != null) {
                        Log.d("date change : ",timestamp.toString());
                        updates.put("date",timestamp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                db.collection("task").document(taskid).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateTask.this,"sucess",Toast.LENGTH_SHORT);
                        Intent in1 = new Intent(UpdateTask.this,TasksActivity.class);
                        UpdateTask.this.startActivity(in1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateTask.this,"failed",Toast.LENGTH_SHORT);
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadImageToFirebase();
        }
    }
    private void uploadImageToFirebase() {
        if (selectedImageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("images/" + selectedImageUri.getLastPathSegment());

            imagesRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    imageurl = imageUrl;
                                    // Log the image URL
                                    Log.e("Image URL", imageUrl);
                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(imageUrl);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private  void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                UpdateTask.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update the selected time
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDate.set(Calendar.MINUTE, minute);

                        // Update the TextView to display the selected date and time
                        updateDisplayDateTimeTextView();
                    }
                },
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE),
                true // Set true if you want 24-hour format
        );
        timePickerDialog.show();
    }
    private void updateDisplayDateTimeTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(selectedDate.getTime());
        Log.d("timesss",dateFormat.toString());
        tasktime = formattedDateTime;
        displaytime.setText("selected time : "+formattedDateTime);
    }
    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            // Update the selected date
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Update the TextView to display the selected date
            updateDisplayDateTextView();

        }

    };



    private void updateDisplayDateTextView() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        Log.d("taggg",sdf.toString());
        String formattedDate = sdf.format(selectedDate.getTime());
        taskdate = formattedDate;
        displayDateTextView.setText("Selected Date: " + formattedDate);
    }
}
