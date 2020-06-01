package com.example.removedragon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnItemListener {
    private RecyclerView recyclerView;
    private ArrayList<Data> data;
    private DataAdapter adapter;
    FloatingActionButton btnRefresh;
    ProgressBar progressBar;
    private ArrayList<String> data2_name = new ArrayList<>();
    private ArrayList<String> data2_packageName = new ArrayList<>();
//    PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        packageManager=getPackageManager();
        initViews();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerBin);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnRefresh = (FloatingActionButton) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                loadJSON();


//                Intent intent = new Intent(Intent.ACTION_DELETE);
//                intent.setData(Uri.parse("package:com.example.recyclerview"));
//                startActivity(intent);


                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_LONG).show();
            }
        });
        loadJSON();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bit.ly/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<DataList> call = apiInterface.getInfo();
        call.enqueue(new Callback<DataList>() {
            @Override
            public void onResponse(Call<DataList> call, Response<DataList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                DataList dataList = response.body();

                data = new ArrayList<>(Arrays.asList(dataList.getSheet1()));

                installedApps();

                adapter = new DataAdapter(data, MainActivity.this, data2_name, data2_packageName, getPackageManager());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


//        progressBar.setVisibility(View.INVISIBLE);
//        installedApps();
//        adapter = new DataAdapter(data, MainActivity.this, data2_name, data2_packageName, getPackageManager());
//        recyclerView.setAdapter(adapter);


    }

    @Override
    public void OnListClick(int position) {
        Toast.makeText(this, "  UNINSTALL  ", Toast.LENGTH_LONG).show();

//        Uri packageUri = Uri.parse("package:"+data2_packageName.get(position));
//        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageUri);
//        startActivity(uninstallIntent);

        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + data2_packageName.get(position)));
        startActivity(intent);

    }

    private void installedApps() {

        Log.d("MyTag", "Inside Installed Apps ");


        List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageList.size(); i++) {
            PackageInfo packageInfo = packageList.get(i);


            String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            for (int j = 0; j < data.size(); j++) {

                Log.d("MyTag",   packageName.equals(data.get(j).getName())+"    "+packageName+"     "+data.get(j).getPackagName());


                if (packageName.equals(data.get(j).getPackagName())) {
                    Log.d("MyTag", " --TRUE-- "+packageName);
                    data2_name.add(appName);
                    data2_packageName.add(packageName);
                }
            }


//            Drawable appIcon = null;
//            try {
//                appIcon = getPackageManager().getApplicationIcon("com.whatsapp");
//                imageView.setImageDrawable(appIcon);
//
//            } catch (PackageManager.NameNotFoundException e) {
//                imageView.setImageDrawable(R.drawable.ic_launcher_foreground);
//                e.printStackTrace();
//            }
        }
    }
}
