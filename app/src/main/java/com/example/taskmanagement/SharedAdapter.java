package com.example.taskmanagement;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SharedAdapter extends RecyclerView.Adapter<SharedAdapter.MyViewHolder> {

    Context context;
    ArrayList<Shared> shared;

    public SharedAdapter(Context context, ArrayList<Shared> shared) {
        this.context = context;
        this.shared = shared;



    }

    @NonNull
    @Override
    public SharedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shareditem,parent,false);
        Log.d("shared adapter 1","done");
        //Log.d("test 1","done");
        //Log.e("test task",tasks.toString());
        //Log.e("test full",tasksFull.toString());
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedAdapter.MyViewHolder holder, int position) {
        Log.d("shared adapter 1","done");
        Shared sharedone = shared.get(position);
        holder.sharedid.setText(sharedone.getId());
        holder.sharedpassword.setText(sharedone.getPassword());
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Shared ID", sharedone.getId());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in your dataset
        return shared.size();
    }

    // Define your ViewHolder class here
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sharedid,sharedpassword;
        ImageButton copy;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            Log.d("shared adapter 1","done");
            sharedid = itemView.findViewById(R.id.sharedid);
            sharedpassword = itemView.findViewById(R.id.sharedpassword);
            copy = itemView.findViewById(R.id.copybutton);
            //Log.d("test 3","done");

        }
    }
}
