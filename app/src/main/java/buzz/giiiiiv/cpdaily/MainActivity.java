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
                editor.putString("username",username.getText().toString());
                editor.putString("password:,password.getText().toString());
                editor.apply();
                exec(8);
                exec(23);
            }
        });
    }
    protected void exec(int hour){
        try {
            var in=getResources().openRawResource(R.raw.dormitory);
            var buff=new byte[in.available()];
            in.read(buff);
            in.close();
            buff=String.format(new String(buff),user.getString("username","%1$s"),user.getString("password","%2$s")).getBytes();
            var path=Paths.get(getFilesDir().getAbsolutePath(),"/chaquopy/AssetFinder/app/config.yml");
            Files.write(path,buff);
            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }
            Python.getInstance().getModule("index").callAttr("main");
        }catch (Exception e){}
    }
}