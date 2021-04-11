package com.example.alarm_ver02;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;


public class AlarmScreen extends AppCompatActivity {

    public static Activity activity; //전역변수 선언

    private static final String KEY_NAME = "example_key";
    private FingerprintManager fingerprintManager;
    //private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private TextView tv_message;

    public static int danger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        activity = AlarmScreen.this; //액티비티 정보 저장

        /*
        //버튼으로 종료
        Button end_alarm = findViewById(R.id.end_btn);
        end_alarm.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v){
                //Toast.makeText(MainActivity.this, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                RingtonePlayingService.mediaPlayer.stop();
                RingtonePlayingService.mediaPlayer.reset();
                RingtonePlayingService.mediaPlayer.release();
                RingtonePlayingService.isRunning = false;
                RingtonePlayingService.startId = 0;
            }
        });
         */

        tv_message = (TextView)findViewById(R.id.tv_message);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

            if(!fingerprintManager.isHardwareDetected()){
                //지문 사용 가능 여부
                tv_message.setText("지문을 사용할 수 없는 디바이스입니다.");
            }
            else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                //지문 사용 허가 여부
                tv_message.setText("지문 사용을 허용해주세요.");
            }
            else if(!fingerprintManager.hasEnrolledFingerprints()){
                //지문 등록 여부
                tv_message.setText("등록된 지문이 없습니다.");
            }
            else{
                tv_message.setText("손가락을 홈버튼에 대주세요.");
                /*
                if(cipherInit()){
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    //핸들러 실행
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                }
                 */
                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
            }
        }

        RingtonePlayingService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //SMS 전송(문자 내용 바꿔야 함)
                //Toast.makeText(AlarmScreen.this, "시간 초과", Toast.LENGTH_LONG).show();
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("01012345678", null, "시간 초과", null, null);
                    Toast.makeText(getApplicationContext(), "보호자에게 문자가 전송되었습니다.", Toast.LENGTH_LONG).show();

                    danger = 1;
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again later.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                //액티비티 종료
                AlarmScreen AS = (AlarmScreen)AlarmScreen.activity;
                AS.finish();
            }
        });
    }

    /*
    public boolean cipherInit(){
        try{
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException e){
            throw new RuntimeException("Failed to get Cipher", e);
        }
    }
     */
}
