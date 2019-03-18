package app_utility;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.autochip.vivanflora.R;

import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;
import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.PORT_NO;
import static app_utility.StaticReferenceClass.SERVER_URL;
import static app_utility.StaticReferenceClass.USER_ID;

public class StatusCheckService extends Service {

    AlarmReceiver alarmReceiver = new AlarmReceiver();
    String channelId = "app_utility.StatusCheckService";
    String channelName = "tracking";
    NotificationManager notifyMgr;
    //NotificationCompat.Builder nBuilder;
    //NotificationCompat.InboxStyle inboxStyle;

    public StatusCheckService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();
        }
        alarmReceiver.setAlarm(this);
        new checkOrderStatusAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        alarmReceiver.setAlarm(this);

        return START_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), createNotificationChannel());
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(101, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notifyMgr != null;
        notifyMgr.createNotificationChannel(chan);
        return channelId;
    }

    @SuppressLint("StaticFieldLeak")
    private class checkOrderStatusAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            checkOrderStatus();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

        private void checkOrderStatus(){
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            List<HashMap<String, Object>> data = oc.search_read("sale.order", new Object[]{
                    new Object[]{new Object[]{"id", "=", "181"}}}, "id", "state", "list_price");
            //draft = saved/orange || sent = quotation is sent/yellow || sale = order confirmed/green || cancel/red
            for (int i = 0; i < data.size(); ++i) {

            }
        }
    }
}
