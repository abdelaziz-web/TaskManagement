package com.example.taskmanagement;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import model.Tache;



public class TasksActivity extends AppCompatActivity  {

    FirebaseFirestore db;
    //LinkedList<Tache> taches;
    RecyclerView myRecycler;
    Button add;
    private FirebaseAuth mAuth;
    MyAdapter2 myadapter2;
    ArrayList<Task> tasks,alltasks;

    String userEmail ;
    TextView showmore ;
    EditText search;
    DrawerLayout drawerlayout;
    ImageButton imagebut;
    NavigationView navview;
    private ProgressDialog progressDialog;

    FloatingActionButton addflot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set message
        progressDialog.setCancelable(false);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userEmail = mAuth.getCurrentUser().getEmail();
        addflot = findViewById(R.id.fab);



        search = (EditText) findViewById(R.id.hint);



        myRecycler=findViewById(R.id.recycler_tasks);
        myRecycler.setHasFixedSize(true);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        tasks = new ArrayList<Task>();
        alltasks = new ArrayList<Task>();
        myadapter2 = new MyAdapter2(TasksActivity.this,tasks,alltasks);
        myRecycler.setAdapter(myadapter2);
        mAuth = FirebaseAuth.getInstance();


        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        imagebut = (ImageButton)  findViewById(R.id.navbut);

        navview = (NavigationView) findViewById(R.id.navview);

        imagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test display","done done done");
                drawerlayout.open();
            }
        });

        addflot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TasksActivity.this,AddTaskActivity.class);
                startActivity(in);
            }
        });



        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Log.d("test display","done done done");

                int itemid = item.getItemId();

                if (itemid == R.id.nav_tasks) {
                    Toast.makeText(TasksActivity.this,"tasks",Toast.LENGTH_SHORT).show();
                    drawerlayout.close();
                }
                if (itemid == R.id.nav_add) {
                    Toast.makeText(TasksActivity.this,"add",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TasksActivity.this,AddTaskActivity.class);
                    startActivity(in);
                }
                if (itemid == R.id.nav_collections) {
                    Toast.makeText(TasksActivity.this,"collections",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TasksActivity.this,Collections.class);
                    startActivity(in);
                }

                if (itemid == R.id.shared_tasks) {
                    Toast.makeText(TasksActivity.this,"shared tasks",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TasksActivity.this,SharedTasks.class);
                    startActivity(in);
                }
                if (itemid == R.id.nav_logout) {
                    Toast.makeText(TasksActivity.this,"logout",Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(TasksActivity.this, AuthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();


                }
                if (itemid == R.id.nav_receive) {
                    Toast.makeText(TasksActivity.this,"receive",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TasksActivity.this,ReceiveTask.class);
                    startActivity(in);
                }
                if (itemid == R.id.nav_send) {
                    Toast.makeText(TasksActivity.this,"send",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TasksActivity.this,SendTask.class);
                    startActivity(in);
                }
                if (itemid == R.id.nav_settings) {
                    Toast.makeText(TasksActivity.this,"settings",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TasksActivity.this,Settings.class);
                    startActivity(in);
                }
                drawerlayout.close();
                return true;
            }
        });

        EventChangeListener();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TextChange", s.toString());
                searchTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}


            /*
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myadapter2.getFilter().filter(s.toString());
                myadapter2.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

             */
        });

    }

    public void searchTasks(String content) {

        if (content.length() > 0) {
            ArrayList<Task> t = new ArrayList<>();
            for (Task task : tasks) {
                String b = task.getTitle() + task.getDesc();
                if (b.contains(content))
                    t.add(task);
            }
            this.myadapter2.setTasks(t);
        } else {
            this.myadapter2.setTasks(tasks);
        }
        myadapter2.notifyDataSetChanged();
    }

    public void EventChangeListener() {
        progressDialog.show();


        db.collection("task").whereEqualTo("user", userEmail).orderBy("title", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {

                    /*
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                     */
                    Log.e("firebase message", Objects.requireNonNull(error.getMessage()));

                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        String documentId = dc.getDocument().getId(); // Get the document ID
                        Task task = dc.getDocument().toObject(Task.class);
                        task.setId(documentId); // Set the document ID in your Task model


                        tasks.add(task);

                        /*
                        tasks.add(dc.getDocument().toObject(Task.class));

                         */

                    }
                    alltasks = new ArrayList<>(tasks);
                    myadapter2.notifyDataSetChanged();
                    /*
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                     */
                }

                
            }
        });
        Log.d("test 5","done");
        progressDialog.dismiss();

    }







        /*
        db.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tache tache= new Tache(document.getString("title"),document.getString("description"),document.getString("deadline"),document.getString("img"));
                                taches.add(tache);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            myRecycler.setHasFixedSize(true);
                            // use a linear layout manager
                            LinearLayoutManager layoutManager = new LinearLayoutManager(TasksActivity.this);
                            myRecycler.setLayoutManager(layoutManager);
                            // specify an adapter (see also next example)
                            MyAdapter myAdapter = new MyAdapter(taches,TasksActivity.this);
                            myRecycler.setAdapter(myAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

       */

    }





/*
DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
        docRef.collection("tasks").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tache tache= new Tache(document.getString("title"),document.getString("description"),document.getString("deadline"),document.getString("img"));
                                taches.add(tache);
                            }
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });
 */