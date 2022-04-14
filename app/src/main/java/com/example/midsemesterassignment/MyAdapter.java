package com.example.midsemesterassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyviewHolder> {

    Context context;
    ArrayList<User> list;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
      User user=list.get(position);
      holder.filename.setText(user.getfilename());
      holder.reader.setText(user.getreader());
      holder.result.setText(user.getresult());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView filename,reader,result;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            filename=itemView.findViewById(R.id.tvfilename);
            reader=itemView.findViewById(R.id.tvreader);
            result=itemView.findViewById(R.id.tvresult);
        }
    }

}
