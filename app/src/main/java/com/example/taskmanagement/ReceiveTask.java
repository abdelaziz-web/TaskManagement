package com.example.taskmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReceiveTask extends AppCompatActivity {


    EditText receiveid;
    Button get;
    private FirebaseFirestore db;
    LinearLayout taskdet;
    String taskid;
    TextView dettitle,detdesc,detdate,detdone,detuser;
    DrawerLayout drawerlayout;
    ImageButton imagebut;
    Button back;
    EditText passwd;
    Button receive;
    private FirebaseAuth mAuth;
    Task taskdd;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_task);
        mAuth = FirebaseAuth.getInstance();

        receiveid = findViewById(R.id.receive_task_id);
        get = findViewById(R.id.get_task);
        db = FirebaseFirestore.getInstance();
        taskdet = findViewById(R.id.taskdet);
        receive = findViewById(R.id.receive_task);

        dettitle = findViewById(R.id.dettitle);
        detdesc = findViewById(R.id.detdesc);
        detdate = findViewById(R.id.detdate);
        detdone = findViewById(R.id.detdone);
        detuser = findViewById(R.id.detuser);
        passwd = findViewById(R.id.receive_password);

        back = findViewById(R.id.back_butt_xd);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ReceiveTask.this,TasksActivity.class);
                startActivity(in);
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(taskdd);
            }
        });




        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("shared_tasks").document(receiveid.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            taskid = documentSnapshot.getString("taskid");
                            String type = documentSnapshot.getString("type");
                            String taskpass = documentSnapshot.getString("password");

                            if (type.equals("private")) {
                                passwd.setVisibility(View.VISIBLE);

                                String password = passwd.getText().toString();

                                if (password.equals(taskpass)) {
                                    getdata(taskid);
                                }
                                else  {
                                    Toast.makeText(ReceiveTask.this,"Wrong password",Toast.LENGTH_SHORT).show();
                                }


                            }

                            if (type.equals("public")) {
                                getdata(taskid);
                            }

                        } else {
                            // Document doesn't exist
                            Log.d("Firestore", "No such document");
                        }
                    }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while retrieving the document
                            Log.w("Firestore", "Error getting document", e);
                        }
                    });



                /*


                 */
            }
        });



    }
    private void getdata(String id) {
        db.collection("task").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String title = document.getString("title");
                        String desc = document.getString("desc");
                        String imageUrl = document.getString("img");
                        String id = document.getString("id");
                        String owner = document.getString("user");

                        boolean done = document.getBoolean("done");
                        Timestamp deadline = document.getTimestamp("date");
                        taskdd = new Task(title,owner,desc,done,imageUrl,deadline,id);




                        taskdet.setVisibility(View.VISIBLE);
                        dettitle.setText(title);
                        detdesc.setText(desc);
                        detdate.setText(taskdd.getFormattedDate());
                        detdone.setText(String.valueOf(done));
                        detuser.setText(owner);



                    } else {
                        // Document does not exist
                        Toast.makeText(ReceiveTask.this, "no such document", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(ReceiveTask.this, "Error getting document", Toast.LENGTH_LONG).show();

                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
    };

    public void addTask(Task task) {

        String userEmail = mAuth.getCurrentUser().getEmail();

        Map<String,Object> data = new HashMap<>();
        data.put("title",task.getTitle());
        data.put("desc",task.getDesc());
        data.put("date",task.getDate());
        data.put("img",task.getImg());
        data.put("done",task.getDone());
        data.put("user",userEmail);

        Log.d("email1: ",task.toString());
        Log.d("email2: ",userEmail);

        if (userEmail.equals(task.getEmail())) {
            Toast.makeText(ReceiveTask.this,"THIS TASK ALREADY EXIST",Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("task")
                    .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent in = new Intent(ReceiveTask.this,TasksActivity.class);
                            ReceiveTask.this.startActivity(in);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReceiveTask.this,"failed",Toast.LENGTH_SHORT);
                        }
                    });
        }



    }


}
