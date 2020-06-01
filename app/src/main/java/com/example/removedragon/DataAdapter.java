package com.example.removedragon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Data> datalist;
    private Context context;
    private OnItemListener mOnItemListener;

    public DataAdapter(ArrayList<Data> datalist,OnItemListener onItemListener) {
        this.mOnItemListener=onItemListener;
        this.datalist=datalist;
    }


    @NonNull
    @Override
    //creates View
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        View view= LayoutInflater.from(getContext()).inflate(R.id.listview,parent,false);
       View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bin,parent,false);
        return new ViewHolder(view,mOnItemListener);
    }

    @Override
    //for setting of data in the View.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.AppName.setText("Name : "+datalist.get(position).getName());
        holder.PackageName.setText("package Name : "+datalist.get(position).getPackagName());



    }

    @Override
    //deals with the length of data.
    public int getItemCount() {
        return datalist.size();
    }
//****************************************************************************************************
    protected  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView AppName,PackageName;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener=onItemListener;
            AppName=itemView.findViewById(R.id.location_txt);
            PackageName=itemView.findViewById(R.id.dry_txt);


            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            onItemListener.OnListClick(getAdapterPosition());
        }
    }

    public  interface  OnItemListener{
        void OnListClick(int position);
    }
}
