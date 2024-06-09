package com.example.taskmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class SharedTasks extends AppCompatActivity {

    FirebaseFirestore db;
    //LinkedList<Tache> taches;
    RecyclerView myRecycler;

    private FirebaseAuth mAuth;
    SharedAdapter myadapter;
    ArrayList<Shared> sharedtasks;

    String userEmail ;

    Button back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_tasks);
        back = findViewById(R.id.back_shared);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SharedTasks.this,TasksActivity.class);
                startActivity(in);
            }
        });

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userEmail = mAuth.getCurrentUser().getEmail();


        myRecycler=findViewById(R.id.recycler_shared);
        myRecycler.setHasFixedSize(true);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        sharedtasks = new ArrayList<Shared>();

        myadapter = new SharedAdapter(SharedTasks.this,sharedtasks);
        myRecycler.setAdapter(myadapter);
        mAuth = FirebaseAuth.getInstance();
        EventChangeListener();




    }

    public void EventChangeListener() {



        db.collection("shared_tasks").whereEqualTo("user", userEmail).orderBy("taskid", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {


                    Log.e("firebase message", Objects.requireNonNull(error.getMessage()));

                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        String documentId = dc.getDocument().getId(); // Get the document ID
                        Shared task = new Shared(documentId,dc.getDocument().getString("taskid"),dc.getDocument().getString("user"),dc.getDocument().getString("type"),dc.getDocument().getString("password"));

                        task.setId(documentId); // Set the document ID in your Task model
                        Log.d("shared tasks :",task.toString());

                        sharedtasks.add(task);
                        Log.d("all shared tasks : ",sharedtasks.toString());

                    }

                    myadapter.notifyDataSetChanged();

                }


            }
        });
        Log.d("test 5","done");


    }
}
