package buzz.giiiiiv.cpdaily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    WorkManager workManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        var username=(EditText)findViewById(R.id.username);
        var password=(EditText)findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                var editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                editor.putString(username.getText().toString(),"%1$s");
                editor.putString(password.getText().toString(),"%2$s");
                editor.apply();
                exec(8);
                exec(23);
            }
        });
    }
    protected void exec(int hour){
        var now= LocalDateTime.now();
        var time=now;
        if (time.getHour()>=hour){
            time=time.plusDays(1);
        }
        time=time.withHour(hour).withMinute(0);
        var diff=Duration.between(now,time).toMinutes();
        var workRequest=new PeriodicWorkRequest.Builder(Work.class,1, TimeUnit.DAYS,1,TimeUnit.HOURS).setInitialDelay(diff,TimeUnit.MINUTES).build();
        if (workManager==null){
            workManager=WorkManager.getInstance(this);
        }
        workManager.enqueueUniquePeriodicWork(Integer.toString(hour), ExistingPeriodicWorkPolicy.REPLACE,workRequest);
    }
}