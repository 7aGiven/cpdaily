package buzz.giiiiiv.cpdaily.ui.login;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import buzz.giiiiiv.cpdaily.R;

public class Work extends androidx.work.Worker{
    Context context;
    int hour;

    public Work(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    public Result doWork() {
        try{
            var resource=0;
            hour=LocalTime.now().getHour();
            if (hour>=6&&hour<10){
                resource= R.raw.morning;
            }
            if (hour>=22&&hour<=23){
                resource=R.raw.evening;
            }
            if (resource==0){
                return Result.failure();
            }
//            context.getSharedPreferences("user",Context.MODE_PRIVATE);
            var in=context.getResources().openRawResource(resource);
            var buff=new byte[in.available()];
            in.read(buff);
            in.close();
            var data=context.getSharedPreferences("user",context.MODE_PRIVATE);
            buff=String.format(new String(buff),data.getString("username","%1$s"),data.getString("password","%2$s")).getBytes();
            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(context));
            }
            var path=Paths.get(context.getFilesDir().getAbsolutePath(),"/chaquopy/AssetFinder/app/config.yml");
            Files.write(path,buff);
            Python.getInstance().getModule("index").callAttr("main");
            return Result.success();
        }catch (Exception e){
            return Result.failure();
        }
    }
}