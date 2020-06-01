package com.example.removedragon;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Data> datalist;
    private Context context;
    private OnItemListener mOnItemListener;

    ArrayList<String> data2_packageName;
    ArrayList<String> data2_name;

    PackageManager packageManager;

    public DataAdapter(ArrayList<Data> datalist,OnItemListener onItemListener,ArrayList<String> data2_name,ArrayList<String> data2_packageName,PackageManager packageManager) {
        this.mOnItemListener=onItemListener;
        this.datalist=datalist;
        this.data2_name=data2_name;
        this.data2_packageName=data2_packageName;
        this.packageManager=packageManager;
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
//        holder.AppName.setText("Name : "+datalist.get(position).getName());
//        holder.PackageName.setText("package Name : "+datalist.get(position).getPackagName());

        holder.AppName.setText("Name : "+data2_name.get(position));
        holder.PackageName.setText("package Name : "+data2_packageName.get(position));



                    Drawable appIcon = null;
            try {
                appIcon = packageManager.getApplicationIcon(data2_packageName.get(position));

                holder.appIcon.setImageDrawable(appIcon);

            } catch (PackageManager.NameNotFoundException e) {
//                holder.appIcon.setImageDrawable(R.drawable.ic_launcher_foreground);
                e.printStackTrace();
            }



    }

    @Override
    //deals with the length of data.
    public int getItemCount() {
        return data2_packageName.size();
    }
//****************************************************************************************************
    protected  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView AppName,PackageName;
        OnItemListener onItemListener;
        ImageView appIcon;
//        PackageManager packageManager=context.getPackageManager();
        public ViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener=onItemListener;
            AppName=itemView.findViewById(R.id.txt_appName);
            PackageName=itemView.findViewById(R.id.txt_packageName);
            appIcon=itemView.findViewById(R.id.app_icon);


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
