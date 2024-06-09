package com.example.taskmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.bumptech.glide.Glide;
//import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText title, den, desc, image , document ;
    Button addtask,back;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    String userEmail;


    private String taskdate,tasktime;

    // date
    private Button chooseDateButton;
    private TextView displayDateTextView;
    private Calendar selectedDate;
    private Button chooseTimeButton;
    private  TextView displayTimeTextView;
    private String imageurl;

    private Button selectImageButton;
    private Uri selectedImageUri;
    private ProgressBar progressBar;




    Task task = new Task();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task2);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage  = FirebaseStorage.getInstance();

        userEmail = mAuth.getCurrentUser().getEmail();


        selectImageButton = findViewById(R.id.selectImageButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("images");




        title = (EditText) findViewById(R.id.title);
        //den = (EditText) findViewById(R.id.den);
        desc = (EditText)   findViewById(R.id.desc);


        addtask = (Button) findViewById(R.id.addtask);
        back = (Button) findViewById(R.id.back);
        addtask.setOnClickListener(this);
        back.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);




        //date
        chooseDateButton = findViewById(R.id.chooseDateButton);
        displayDateTextView = findViewById(R.id.displayDateTextView);
        selectedDate = Calendar.getInstance();
        //Time
        chooseTimeButton = findViewById(R.id.chooseTimeButton);
        displayTimeTextView = findViewById(R.id.displayTimeTextView);
        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

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
                            Toast.makeText(AddTaskActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }









    public void onClick(View view) {
        //mAuth.signOut();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (view.getId()==R.id.back) {
            Intent in1 = new Intent(this, TasksActivity.class);
            startActivity(in1);
        }
        if (view.getId()==R.id.addtask) {

            String title = this.title.getText().toString();
            boolean done = false;
            String desc = this.desc.getText().toString();
            //Timestamp document = Timestamp.now();
            Timestamp timestamp=Timestamp.now();
            try {
                String dateString = taskdate+" "+tasktime;
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.ENGLISH);
                Date parsedDate = dateFormat.parse(dateString);

                // Convert the parsed Date to a Timestamp
                timestamp = new Timestamp(parsedDate);

                // Now 'timestamp' holds the value in Timestamp format
                Log.d("final date : ",timestamp.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            task.setDone(done);
            task.setDesc(desc);
            task.setDate(timestamp);
            task.setTitle(title);
            task.setImg(imageurl);

            /*
            Map<String, Object> task = new HashMap<>();

            task.put("title", title);
            task.put("desc", desc);
            task.put("den", den);
            task.put("img", image);
            task.put("doc", document);

             */
            //Log.d("display image",task.getImg().toString());
            if (task.getImg()==null) {
                Toast.makeText(AddTaskActivity.this,"image required",Toast.LENGTH_SHORT).show();
                //Intent in3 = new Intent(this, AddTaskActivity.class);
                //startActivity(in3);
                finish();
            }

            Map<String,Object> data = new HashMap<>();
            data.put("title",task.getTitle());
            data.put("desc",task.getDesc());
            data.put("date",task.getDate());
            data.put("img",task.getImg());
            data.put("done",task.getDone());
            data.put("user",userEmail);
            db.collection("task")
                    .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent in = new Intent(AddTaskActivity.this,TasksActivity.class);
                            AddTaskActivity.this.startActivity(in);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddTaskActivity.this,"failed",Toast.LENGTH_SHORT);
                        }
                    });

        }


    }
    // date
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
    private  void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddTaskActivity.this,
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
    private void updateDisplayDateTimeTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(selectedDate.getTime());
        Log.d("timesss",dateFormat.toString());
        tasktime = formattedDateTime;
        displayTimeTextView.setText("selected time : "+formattedDateTime);
    }

    private void updateDisplayDateTextView() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        Log.d("taggg",sdf.toString());
        String formattedDate = sdf.format(selectedDate.getTime());
        taskdate = formattedDate;
        displayDateTextView.setText("Selected Date: " + formattedDate);
    }







}