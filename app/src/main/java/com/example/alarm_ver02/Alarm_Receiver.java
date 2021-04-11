package com.example.alarm_ver02;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Alarm_Receiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent){
        /*
        try {
            intent = new Intent(context, AlarmScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            pendingIntent.send();
        }
        catch(PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
         */

        this.context = context;
        String get_your_string = intent.getExtras().getString("state");
        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("state", get_your_string);
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_intent);
        }
        else{
            this.context.startService(service_intent);
        }
    }
}
