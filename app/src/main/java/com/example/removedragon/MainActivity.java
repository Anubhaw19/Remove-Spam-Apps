package com.example.removedragon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    Button btnRefresh;
    ProgressBar progressBar;
    private ArrayList<String> data2_name = new ArrayList<>();
    private ArrayList<String> data2_packageName = new ArrayList<>();
    ArrayList<Drawable> appIcons = new ArrayList<>();
    TextView scanningText;
    private static final int REQUEST_CODE = 1;
    ImageButton popupMenuButton;
    int p = -1;
    boolean scanComplete = false;
    ImageView noAppsAnimation;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d("MyTag", "Inside Main ");
        initViews();

        popupMenuButton = findViewById(R.id.menu_botton);
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, popupMenuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                Toast.makeText(getApplicationContext(), "1 click", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.two:
                                Toast.makeText(getApplicationContext(), "2 click", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.three:
                                Toast.makeText(getApplicationContext(), "3 click", Toast.LENGTH_LONG).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();
// comments

            }
        });
    }

    private void initViews() {

        btnRefresh = findViewById(R.id.scan_again);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_LONG).show();


                bottomSheetDialog = new BottomSheetDialog
                        (MainActivity.this, R.style.BottomSheetDialogTheme);

                bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.demo_layout
                                , findViewById(R.id.id_for_demo_layout)
                        );
                progressBar = bottomSheetView.findViewById(R.id.progressBar);
                scanningText = bottomSheetView.findViewById(R.id.scanning);


                recyclerView = bottomSheetView.findViewById(R.id.recyclerBin);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                adapter = new DataAdapter(MainActivity.this, data2_name, data2_packageName, getPackageManager(), appIcons);
                recyclerView.setAdapter(adapter);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "Dialog Dismissed", Toast.LENGTH_SHORT).show();
                    }
                });


                if (scanComplete == false) {
                    loadJSON();
                } else {
                    installedApps();
                }


            }
        });
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
                DataList dataList = response.body();
                Log.d("MyTag", "Received Response ");

                data = new ArrayList<>(Arrays.asList(dataList.getSheet1()));

                installedApps();

            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void installedApps() {

        int count = 0;
        if (scanComplete == false) {
            List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packageList.size(); i++) {
                PackageInfo packageInfo = packageList.get(i);

                String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String packageName = packageInfo.packageName;

                int result = binarySearch(packageName, data);

                if (result != -1) {
                    data2_name.add(appName);
                    data2_packageName.add(packageName);
//                Log.d("MyTag", "FOUND ");


                    try {
                        appIcons.add(getPackageManager().getApplicationIcon(packageName));


                    } catch (PackageManager.NameNotFoundException e) {
                        appIcons.add(null);
                        e.printStackTrace();
                    }


                    adapter.notifyItemInserted(count);
                    count = count + 1;
                }
            }
            scanComplete = true;

        }

        if (data2_packageName.size()==0){
            noAppsAnimation=findViewById(R.id.no_apps);
            noAppsAnimation.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);
        scanningText.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
    }

    int binarySearch(String x, ArrayList<Data> arr) {
        int l = 0, r = arr.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            int res = x.compareTo(arr.get(m).getPackagName());

            // Check if x is present at mid
            if (res == 0)
                return m;

            // If x greater, ignore left half
            if (res > 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }

        return -1;
    }

    @Override
    public void OnListClick(int position) {
        Toast.makeText(this, "  UNINSTALL  ", Toast.LENGTH_LONG).show();
        p = position;
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + data2_packageName.get(position)));
        startActivityForResult(intent, REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Toast.makeText(this, "App Uninstalled", Toast.LENGTH_SHORT).show();
            if (p != -1) {
                adapter.notifyItemRemoved(p);
            }

        } else {
            Toast.makeText(this, "App Not Removed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume invoked");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause invoked");
        data2_name.clear();
        data2_packageName.clear();
        adapter.notifyDataSetChanged();
        scanComplete=false;
        bottomSheetDialog.hide();

    }


}
