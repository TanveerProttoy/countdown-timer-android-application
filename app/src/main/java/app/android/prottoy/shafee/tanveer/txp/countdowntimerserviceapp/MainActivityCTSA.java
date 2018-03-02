package app.android.prottoy.shafee.tanveer.txp.countdowntimerserviceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivityCTSA extends AppCompatActivity {
    private EditText countdownTimerEditTxt;
    private Button countdownTimerStartBtn;
    private TextView countdownTimerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countdownTimerEditTxt = (EditText) findViewById(R.id.countdown_edit_txt);
        countdownTimerStartBtn = (Button) findViewById(R.id.countdown_btn);
        countdownTimerText = (TextView) findViewById(R.id.countdown_timer_txt);
        countdownTimerText.setTypeface(Typeface.DEFAULT_BOLD);
        countdownTimerStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = countdownTimerEditTxt.getText().toString();
                if(!val.equals("")) {
                    try {
                        long timerSetter = Long.parseLong(val);
                        startService(new Intent(MainActivityCTSA.this,
                                CountdownTimerService.class).putExtra("setTimer",
                                TimeUnit.MINUTES.toMillis(timerSetter)));
                        countdownTimerEditTxt.setEnabled(false);
                        countdownTimerStartBtn.setEnabled(false);
                    }
                    catch(Exception e) {
                        Toast.makeText(MainActivityCTSA.this,
                                "Something went wrong please try again! Check your input",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(MainActivityCTSA.this,
                            "Please enter valid time in minute", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private BroadcastReceiver countdownTimerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCountdownTimer(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(countdownTimerReceiver,
                new IntentFilter(AppComponent.Constant.COUNTDOWN_TIMER_BROADCAST_RECEIVER));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(countdownTimerReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(countdownTimerReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivityCTSA.this, CountdownTimerService.class));
    }

    private void updateCountdownTimer(Intent intent) {
        if(intent.getExtras() != null) {
            try {
                String timerValue = intent.getStringExtra("countdownTimer");
                if(timerValue != null) {
                    countdownTimerText.setText(timerValue);
                }
                else {
                    countdownTimerText.setText("Something went wrong please try again!");
                }
            }
            catch(Exception e) {
                Toast.makeText(MainActivityCTSA.this,
                        "Something went wrong please try again!", Toast.LENGTH_LONG).show();
            }
            try {
                if(countdownTimerText.getText().toString().equals("Countdown Finished")) {
                    countdownTimerEditTxt.setText("");
                    countdownTimerEditTxt.setEnabled(true);
                    countdownTimerStartBtn.setEnabled(true);
                }
            }
            catch(Exception e) {
                Toast.makeText(MainActivityCTSA.this,
                        "Something went wrong please try again!", Toast.LENGTH_LONG).show();
            }
        }
    }
}