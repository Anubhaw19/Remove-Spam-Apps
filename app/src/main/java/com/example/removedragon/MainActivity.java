package com.example.removedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnItemListener , FeedbackDialog.sendButtonClicked {
    private static final int REQUEST_CODE = 1;
    public TextView hint;
    Button scanButton;
    ProgressBar progressBar;
    ArrayList<String> localDataBase = new ArrayList<>();
    ArrayList<Drawable> appIcons = new ArrayList<>();
    TextView scanningText;
    ImageButton popupMenuButton;
    int p = -1;
    LottieAnimationView noAppsAnimation;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    TextView textLink;
    String TAG = "MyTag";
    String uninstallAppName = "";
    int appRemoveIndex = -1;
    private RecyclerView recyclerView;
    private ArrayList<Data> data;
    private DataAdapter adapter;
    private ArrayList<String> final_name = new ArrayList<>();
    private ArrayList<String> final_packageName = new ArrayList<>();
    LinearLayout openSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        popupMenuButton = findViewById(R.id.menu_botton);
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupOption(v); //method for popup menu.
            }
        });
        textLink = findViewById(R.id.textLink);
        textLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/Team-Voyager-100284605056678"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        openSource=findViewById(R.id.openSource);
        openSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/anandshivam44/Remove_Spam_Apps"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showPopupOption(View v) {
        PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.zero://feedback
                        openDialog();
                        break;
                    case R.id.one:

                        try {
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.whatsapp");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I am using this app. It scans and removes spam apps from your phone. Download it here https://play.google.com/store/apps/details?id=com.uninstall.removespam");
                            startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, "Hey, I am using this app. It scans and removes spam apps from your phone. Download it here https://play.google.com/store/apps/details?id=com.uninstall.removespam");
                            startActivity(share);
                        }

                        break;

                    case R.id.two://feedback
                        openDialog();
                        break;

                    case R.id.three://about us
                        Uri uri1 = Uri.parse("https://www.facebook.com/Team-Voyager-100284605056678"); // missing 'http://' will cause crashed
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(intent1);
                        break;
                    case R.id.four://rate us
                        Uri uri2 = Uri.parse("https://play.google.com/store/apps/details?id=com.uninstall.removespam"); // missing 'http://' will cause crashed
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, uri2);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(MainActivity.this, (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();

    }

    private void initViews() {
        scanButton = findViewById(R.id.scan_again);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_LONG).show();
                bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.demo_layout, findViewById(R.id.id_for_demo_layout));
                progressBar = bottomSheetView.findViewById(R.id.progressBar);
                scanningText = bottomSheetView.findViewById(R.id.scanning);
                noAppsAnimation = bottomSheetView.findViewById(R.id.no_apps);
                hint = bottomSheetView.findViewById(R.id.hint_xml);
                recyclerView = bottomSheetView.findViewById(R.id.recyclerView_ListOfApps);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                adapter = new DataAdapter(MainActivity.this, final_name, final_packageName, getPackageManager(), appIcons);
                recyclerView.setAdapter(adapter);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "Dismiss", Toast.LENGTH_SHORT).show();
                        hint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                        hint.setTextColor(Color.parseColor("#000000"));//Color.parseColor("#FFFFFF")
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        installedApps();
                    }
                }, 500);
            }
        });
    }

//    private void loadJSON() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://bit.ly/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
//        Call<DataList> call = apiInterface.getInfo();
//        call.enqueue(new Callback<DataList>() {
//            @Override
//            public void onResponse(Call<DataList> call, Response<DataList> response) {
//                DataList dataList = response.body();
//                Log.d(TAG, "Received Response ");
//
//                data = new ArrayList<>(Arrays.asList(dataList.getSheet1()));
//
//                installedApps();
//
//            }
//
//            @Override
//            public void onFailure(Call<DataList> call, Throwable t) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//
//    }

    private void installedApps() {
        int count = 0;
        final_name.clear();
        final_packageName.clear();
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);
        scanDataBase();
        for (int i = 0; i < packageList.size(); i++) {
            PackageInfo packageInfo = packageList.get(i);
            String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            int result = binarySearch(packageName, localDataBase);
            if (result != -1) {
                final_name.add(appName);
                final_packageName.add(packageName);
//                Log.d(TAG, "FOUND " + appName + " " + packageName);
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
        if (final_packageName.size() == 0) {
//            Log.d(TAG, "installedApps: LIST SIZE ZERO");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    noAppsAnimation.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    hint.animate()
                            .translationY(hint.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);

                                    hint.setText("Congratulations! No Spam Apps Found️");
                                    hint.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);
                                    hint.setTextColor(Color.parseColor("#01311F"));//Color.parseColor("#FFFFFF")
                                }
                            });
                    hint.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(300);
                }
            }, 500);
        }
        progressBar.setVisibility(View.GONE);
        scanningText.setVisibility(View.GONE);
    }

    int binarySearch(String x, ArrayList<String> arr) {
        int l = 0, r = arr.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            int res = x.compareTo(arr.get(m));
            if (res == 0)
                return m;
            if (res > 0)
                l = m + 1;
            else
                r = m - 1;
        }
        return -1;
    }

    @Override
    public void OnListClick(int position) {
//        Toast.makeText(this, "  UNINSTALL  ", Toast.LENGTH_LONG).show();
        p = position;
        uninstallAppName = final_packageName.get(position);
        appRemoveIndex = position;
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + uninstallAppName));
        intent.putExtra("uninstallApp", final_packageName.get(position));
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d(TAG, requestCode + " >> " + resultCode + " <<  " + String.valueOf(data));
        try {
            getPackageManager().getPackageInfo(uninstallAppName, 0);
//            Log.d(TAG, "onActivityResult: APP NOT REMOVED");
        } catch (PackageManager.NameNotFoundException e) {
//            Log.d(TAG, "onActivityResult: APP REMOVED");
            final_packageName.remove(appRemoveIndex);
            final_name.remove(appRemoveIndex);
            adapter.notifyItemRemoved(appRemoveIndex);
            appRemoveIndex = -1;
            if (final_packageName.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        noAppsAnimation.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        hint.setText("Congratulations! No Spam Apps Found️");
                        hint.setTextSize(20);
                        hint.setTextColor(Color.parseColor("#01311F"));//Color.parseColor("#FFFFFF")
                    }
                }, 500);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume invoked");

    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.d(TAG, "onPause invoked");
    }

    void scanDataBase() { //adds package name from dataBase to an Array List
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("name.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                localDataBase.add(mLine.trim());
//                Log.d(TAG, "scanData      " + mLine);
            }

        } catch (IOException e) {
//            Log.d(TAG, "Exception");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
//                    Log.d(TAG, "Exception");

                }
            }
        }
    }


    private void addItemToSheet(String message) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://script.google.com/macros/s/AKfycbyLy9TkQPJBKVceYsxeH08n5I4BhWiIk454SI7piulOPCrlvsI/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action", "addItem");
                parmas.put("feedback", message);

                return parmas;
            }
        };

        int socketTimeOut = 5000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }

    @Override
    public void sendFeedback(String message) {
        addItemToSheet(message);

    }
    public void openDialog() {
        FeedbackDialog dialog=new FeedbackDialog();
        dialog.show(getSupportFragmentManager(),"feedback");
    }
}
