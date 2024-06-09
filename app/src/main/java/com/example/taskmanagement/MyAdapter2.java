package com.example.taskmanagement;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<Task> tasks;
    ArrayList<Task> tasksFull;

    public MyAdapter2(Context context, ArrayList<Task> tasks, ArrayList<Task> alltasks) {
        this.context = context;
        this.tasks = tasks;

        this.tasksFull = alltasks;

    }

    @NonNull
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        Log.d("test 1","done");
        //Log.e("test task",tasks.toString());
        //Log.e("test full",tasksFull.toString());
        return new MyViewHolder(v);

    }
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks=tasks;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.MyViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.id.setText(task.getId());
        holder.title.setText(task.getTitle());
        //holder.desc.setText(task.getDesc());
        holder.deadline.setText(task.getFormattedDate());
        holder.showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(v.getContext(), TaskActivity.class);
                in1.putExtra("taskid",task.getId());
                v.getContext().startActivity(in1);

            }
        });
        //Log.d("test 2","done");

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,deadline,id;
        LinearLayout showmore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvtitle);
            //desc = itemView.findViewById(R.id.tvdesc);
            deadline = itemView.findViewById(R.id.tvdeadline);
            showmore = itemView.findViewById(R.id.item);
            id = itemView.findViewById(R.id.tvid);
            Log.d("test 3","done");

        }

    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }
    private Filter taskFilter = new Filter() {
        @Override

        protected FilterResults performFiltering(CharSequence constraint) {
            //Log.e("full tasks",tasksFull.toString());
            List<Task> filteredList = new ArrayList<>();
            Log.e("init",tasksFull.toString());

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tasksFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Task task : tasksFull) {
                    if (task.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(task);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            //Log.e("result",filteredList.toString());
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (constraint == null || constraint.length() == 0) {
                tasks.clear();
                tasks.addAll(tasksFull); // Restore original unfiltered data
                Log.e("taskk 0",tasks.toString());
                Log.e("full task 0",tasksFull.toString());
            } else {
                tasks.clear();
                tasks.addAll((List<Task>) results.values); // Update with filtered results
                Log.e("taskk",tasks.toString());
                Log.e("full task",tasksFull.toString());
            }
            notifyDataSetChanged();
        }

    };
}
