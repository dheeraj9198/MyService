package mebeerhu.dheeraj.sachan.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
simport java.util.UUID;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by dheeraj on 21/8/15.
 */
public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private static boolean isRunning = false;
    private ScheduledExecutorService executorService;

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning) {
            Log.e(TAG, "already running fuck off");
            stopSelf();
            return START_NOT_STICKY;
        }
        Log.e(TAG, "not running starting");
        isRunning = true;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable() {
            private String random = UUID.randomUUID().toString();
            @Override
            public void run() {
                Log.e("", random);
            }
        }, 1, 5, TimeUnit.SECONDS);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "stopping service");
        if (executorService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG,"terminate");
                        executorService.shutdownNow();
                        executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {

                    }
                }
            }).start();
        }
        isRunning = false;
        super.onDestroy();
    }
}
