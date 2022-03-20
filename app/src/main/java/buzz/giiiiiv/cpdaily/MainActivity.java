package buzz.giiiiiv.cpdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {
    SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=getSharedPreferences("user",MODE_PRIVATE);
        var username=(EditText)findViewById(R.id.username);
        var password=(EditText)findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                var editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                editor.putString("username",username.getText().toString());
                editor.putString("password",password.getText().toString());
                editor.apply();
            }
        });
        findViewById(R.id.dormitory).setOnClickListener(view -> {
            try {
                var in=getResources().openRawResource(R.raw.dormitory);
                var buff=new byte[in.available()];
                in.read(buff);
                in.close();
                buff=String.format(new String(buff),user.getString("username","%1$s"),user.getString("password","%2$s")).getBytes();
                var path= Paths.get(getFilesDir().getAbsolutePath(),"/chaquopy/AssetFinder/app/config.yml");
                Files.write(path,buff);
                if (! Python.isStarted()) {
                    Python.start(new AndroidPlatform(this));
                }
                Python.getInstance().getModule("index").callAttr("main");
            }catch (Exception e){}
        });
    }
}