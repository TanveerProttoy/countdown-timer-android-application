package app.android.prottoy.shafee.tanveer.txp.countdowntimerserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

public class CountdownTimerService extends Service {
    private Intent countdownServiceReceiver;
    private CountDownTimer countDownTimer;
    private long convertedValue;

    public CountdownTimerService() {
        countdownServiceReceiver = new Intent(AppComponent.Constant.COUNTDOWN_TIMER_BROADCAST_RECEIVER);
        countDownTimer = null;
        convertedValue = 0L;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        countDownTimer = new CountDownTimer(intent.getExtras().getLong("setTimer"),
                1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownServiceReceiver.putExtra("countdownTimer",
                        String.format("%02d:%02d",
                                convertedValue = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(convertedValue)));
                sendBroadcast(countdownServiceReceiver);
            }

            @Override
            public void onFinish() {
                countdownServiceReceiver.putExtra("countdownTimer", "Countdown Finished");
                sendBroadcast(countdownServiceReceiver);
                stopSelf();
            }
        };
        countDownTimer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            countDownTimer.cancel();
        }
        catch(NullPointerException n) {

        }
    }
}