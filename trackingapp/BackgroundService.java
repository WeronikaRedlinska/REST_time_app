package com.example.trackingapp;

import static com.example.trackingapp.MainActivity.counter;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
public class BackgroundService extends Service {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    int count;
    List<AppList> lista;
    public BackgroundService(){

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        sharedPreferences = getSharedPreferences("AppDuration",MODE_PRIVATE);
        editor =sharedPreferences.edit();
        count = 0;
        lista = new ArrayList<AppList>();
        TimerTask detectApp  = new TimerTask(){
            @Override
            public void run(){
                sharedPreferences = getSharedPreferences("AppDuration",MODE_PRIVATE);
                editor =sharedPreferences.edit();
                UsageStatsManager usageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
                long endtime = System.currentTimeMillis();
                long begintime = endtime -(10000000);
                List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,begintime,endtime);
                 if(usageStats!=null){
                     for (UsageStats usagestat:usageStats){
                        if(usagestat.getPackageName().toLowerCase().contains("trackingapp") || usagestat.getPackageName().toLowerCase().contains("settings") ||usagestat.getPackageName().toLowerCase().contains("contacts") ||usagestat.getPackageName().toLowerCase().contains("phone")||usagestat.getPackageName().toLowerCase().contains("calendar")||usagestat.getPackageName().toLowerCase().contains("chrome")){
                            editor.putLong(counter, usagestat.getTotalTimeInForeground());
                            System.out.println(usagestat.getTotalTimeInForeground());
                            lista.add(new AppList(usagestat.getTotalTimeInForeground(),usagestat.getPackageName()));
                        }

                         editor.commit();
                     }
                     try {
                         for (int i=0; i < lista.size(); i = i+1){
                             JavaRestClient.sent_data(lista.get(i).appname,(int)lista.get(i).time);
                         }
                         JavaRestClient.get_stats();
                     }
                     catch( Exception e){
                         editor.putLong(counter,10000000);
                     }
                 }
            }
        };
        Timer detectAppTime = new Timer();
        detectAppTime.scheduleAtFixedRate(detectApp, 0,86400000);
        return super.onStartCommand(intent,flags,startId);
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
