package com.example.alarm_ver02;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;



public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    CancellationSignal cancellationSignal;
    private Context appContext;

    public FingerprintHandler(Context context)
    {
        this.appContext = context;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString){
        //Toast.makeText(appContext, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
        this.update(false);
    }
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString){
        //Toast.makeText(appContext, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
        this.update(false);
    }
    @Override
    public void onAuthenticationFailed(){
        //Toast.makeText(appContext, "Authentication failed\n", Toast.LENGTH_LONG).show();
        this.update(false);
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result){
        //Toast.makeText(appContext, "Authentication success\n" + result, Toast.LENGTH_LONG).show();
        this.update(true);
    }

    /*
    public void stopAuth() {
        if(cancellationSignal != null && !cancellationSignal.isCanceled()){
            cancellationSignal.cancel();
        }
    }
     */

    private void update(boolean b){
        final TextView tv_message = (TextView)((Activity)appContext).findViewById(R.id.tv_message);
        if(b == false){
            //?????? ?????? ??????
            tv_message.setText("?????? ?????? ??????");
            tv_message.setTextColor(ContextCompat.getColor(appContext, R.color.colorAccent));
        }
        else{
            //?????? ?????? ??????
            tv_message.setText("?????? ?????? ??????");
            tv_message.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimaryDark));
            AlarmScreen.danger = 0;

            //?????? ??????
            RingtonePlayingService.mediaPlayer.stop();
            RingtonePlayingService.mediaPlayer.reset();
            RingtonePlayingService.mediaPlayer.release();
            RingtonePlayingService.isRunning = false;
            RingtonePlayingService.startId = 0;

            //???????????? ??????
            AlarmScreen AS = (AlarmScreen)AlarmScreen.activity;
            AS.finish();
        }
    }
}
