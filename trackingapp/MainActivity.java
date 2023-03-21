package com.example.trackingapp;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.Settings;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.trackingapp.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String counter = "App counter";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private TextView viewcounter;
    private TextView viewcounter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("AppDuration",MODE_PRIVATE);
        Button button = (Button) findViewById(R.id.button_first);
        button.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
                       Intent intent = new Intent(MainActivity.this, TableActivity.class);
                       MainActivity.this.startActivity(intent);
           }
        });
        setSupportActionBar(binding.toolbar);

        if(!checkUsageStatsAllowedOrNot()){
            Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            usageAccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(usageAccessIntent);
            if(checkUsageStatsAllowedOrNot()){
                startService(new Intent(MainActivity.this,BackgroundService.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"Please give access",Toast.LENGTH_SHORT).show();
            }
        }else{
            startService(new Intent(MainActivity.this,BackgroundService.class));
        }
        TimerTask updateView = new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        long app_time=sharedPreferences.getLong(counter,0);
                        long second=(app_time/1000)%60;
                        long minute=(app_time/(60*1000))%60;
                        long hours = (app_time/(60*1000*60));
                        String values = "hours: "+hours+"."+minute+"."+second+".";
                        System.out.println(values);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(updateView,0,86400000);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public boolean checkUsageStatsAllowedOrNot(){
        try{
            PackageManager packageManager =getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(),0);
            AppOpsManager appOpsManager = (AppOpsManager)getSystemService(APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,applicationInfo.uid,applicationInfo.packageName);
            return (mode==AppOpsManager.MODE_ALLOWED);
        } catch (Exception c){
            Toast.makeText(getApplicationContext(),"Error cant find any usage sstats manager",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onDestroy()
    {
        if(checkUsageStatsAllowedOrNot()){
            startService((new Intent(MainActivity.this,BackgroundService.class)));

        }
        super.onDestroy();
    }
}