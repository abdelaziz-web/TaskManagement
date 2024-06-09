package com.example.taskmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendTask extends AppCompatActivity implements View.OnClickListener {


    EditText taskid;
    RadioButton pub,pri;
    EditText passwd;
    Button send;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String userEmail;
    Button back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_task);

        taskid = findViewById(R.id.send_task_id);
        pub = findViewById(R.id.radio_pub);
        pri = findViewById(R.id.radio_pri);
        passwd = findViewById(R.id.password_send);
        send = findViewById(R.id.but_send);
        back = findViewById(R.id.but_send_back);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userEmail = mAuth.getCurrentUser().getEmail();


        ArrayList<String> datax = new ArrayList<>();
        datax.add("1");
        datax.add("public");
        datax.add("1");

        RadioGroup radioGroup = findViewById(R.id.radio_send);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    // You can get the text or any other properties of the selected RadioButton
                    String selectedText = radioButton.getText().toString();

                    if (selectedText.equals("Private task")) {
                        passwd.setVisibility(View.VISIBLE);
                        datax.set(1,"private");
                    }
                    else {
                        passwd.setVisibility(View.GONE);
                        datax.set(1,"public");
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SendTask.this,TasksActivity.class);
                startActivity(in);
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SendTask.this,"clickd",Toast.LENGTH_SHORT);
                String id = taskid.getText().toString();
                String pass = passwd.getText().toString();

                if (id != null && !id.isEmpty()) {
                    datax.set(0,id);
                    datax.set(2,pass);
                    Log.d("datax = ",datax.toString());
                    senTask(datax);
                } else {

                    Toast.makeText(SendTask.this, "Task ID is required", Toast.LENGTH_SHORT).show();

                }




            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    private void senTask(ArrayList<String> datax) {

        Map<String,Object> data = new HashMap<>();
        data.put("taskid",datax.get(0));
        data.put("type",datax.get(1));
        data.put("password",datax.get(2));
        data.put("user",userEmail);

        db.collection("shared_tasks").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent in = new Intent(SendTask.this,TasksActivity.class);
                SendTask.this.startActivity(in);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SendTask.this,"failed",Toast.LENGTH_SHORT);
            }
        });
    }
}
