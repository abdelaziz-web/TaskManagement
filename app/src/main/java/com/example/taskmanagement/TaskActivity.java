package com.example.taskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;

public class TaskActivity extends AppCompatActivity {

    Task taskd;
    FirebaseFirestore db;
    FirebaseStorage dbs;
    TextView done, date, desc, title;

    Button delete, update;
    ImageView img;
    Button back;
    Button copyid;
    Button donne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        dbs = FirebaseStorage.getInstance();

        // Initialize views
        title = findViewById(R.id.dettitle);
        desc = findViewById(R.id.detdesc);
        date = findViewById(R.id.detdate);
        done = findViewById(R.id.detdone);
        img = findViewById(R.id.imagedet);
        copyid = findViewById(R.id.copyid);
        delete = findViewById(R.id.deletetask);
        update = findViewById(R.id.updatetask);
        back = findViewById(R.id.back_butt);
        donne = findViewById(R.id.donekk); // Initialize this here

        // Get task ID from intent
        String taskid = getIntent().getStringExtra("taskid");

        // Set up button listeners
        donne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markTaskAsDone(taskid);
            }
        });

        copyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Shared ID", taskid);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(TaskActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TaskActivity.this, TasksActivity.class);
                startActivity(in);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("task").document(taskid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(TaskActivity.this, "task with id = " + taskid + " is deleted", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(TaskActivity.this, TasksActivity.class);
                        TaskActivity.this.startActivity(in);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TaskActivity.this, "failed deleting", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TaskActivity.this, UpdateTask.class);
                in.putExtra("taskid", taskid);
                TaskActivity.this.startActivity(in);
            }
        });

        // Fetch task details
        getbyid(taskid, new TaskLoadCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                taskd = task;
                title.setText(taskd.getTitle());
                desc.setText(taskd.getDesc());
                date.setText(taskd.getFormattedDate());
                done.setText(taskd.getDone() ? "YES" : "NO");
                Glide.with(TaskActivity.this)
                        .load(taskd.getImg())
                        .apply(new RequestOptions().placeholder(R.drawable.logo))
                        .into(img);
            }
        });
    }

    public interface TaskLoadCallback {
        void onTaskLoaded(Task task);
    }

    public void getbyid(String id, final TaskLoadCallback callback) {
        db.collection("task").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String title = document.getString("title");
                        String desc = document.getString("desc");
                        String imageUrl = document.getString("img");
                        boolean done = document.getBoolean("done");
                        Timestamp deadline = document.getTimestamp("date");
                        Task taskdd = new Task(title, "", desc, done, imageUrl, deadline, id);
                        callback.onTaskLoaded(taskdd);
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
    }

    private void markTaskAsDone(String taskId) {
        db.collection("task").document(taskId)
                .update("done", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TaskActivity.this, "Task marked as done", Toast.LENGTH_SHORT).show();
                        done.setText("YES");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TaskActivity.this, "Failed to update task", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
