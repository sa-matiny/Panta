package com.iust.panta;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

public class NotificationIntentService extends IntentService {
   // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.iust.panta.action.FOO";
    private static final String ACTION_BAZ = "com.iust.panta.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.iust.panta.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.iust.panta.extra.PARAM2";
    private String msgtype;
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String title;
    String sentence;
    private int taskID;
    private String username;
    private String manager;
    SqliteController controller = new SqliteController(this);
  //  private Boolean

    public static final String TAG = "Panta";
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public NotificationIntentService() {

          super("NotificationIntentService");
          Log.d("notification","IntentNotificationNasi");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            username = controller.getMe().getString("username");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("nastaran","****************");
        Bundle extras= intent.getExtras();

        //taskID=1;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        String msg="";

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.

                Log.i("bundle","yes"+extras.toString());
                msgtype=extras.getString("msg_type");
                Log.d("messageType",(msgtype));

                sentence=extras.getString("message");
           //     Log.d("titlee",title);
                if(msgtype.equals("1")) {
                    try {
                        JSONObject TaskInfo = new JSONObject(extras.getString("task_info"));
                      //  TaskInfo=TaskInfo.getJSONObject("taskInfo");
                         manager=TaskInfo.getString("managerUser");

                         Log.d("taskInfoBundle", TaskInfo.toString());
                         taskID = TaskInfo.getInt("taskID");
                         Log.d("taskID", Integer.toString(taskID));
                         msg="کاربر "+" "+sentence+" "+" وظیفه ی خود را انجام داده است";
                        title="انجام وظیفه";
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Problem in catching ", "hichi");


                    }
                }
                if(msgtype.equals("2")) {

                        msg="شما به پروژه "+" "+ sentence+" "+" اضافه شدید";
                        title="پروژه ی جدید ";


                }

                if(msgtype.equals("3")) {
                    try {
                        JSONObject TaskInfo = new JSONObject(extras.getString("task_info"));
                        //  TaskInfo=TaskInfo.getJSONObject("taskInfo");
                       // manager=TaskInfo.getString("managerUser");

                        Log.d("taskInfoBundle", TaskInfo.toString());
                        taskID = TaskInfo.getInt("taskID");

                        manager=TaskInfo.getString("managerUser");

                       // Log.d("taskID", Integer.toString(taskID));
                        msg="برای شما وظیفه ی جدید در پروژه ی "+" "+sentence+" "+"تعریف شده است .";
                        title="وظیفه جدید";
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        //Log.d("Problem in catching ", "hichi");


                    }


                }
                if(msgtype.equals("4"))
                {
                    msg="زمان پروژه "+" "+sentence +" "+"به پایان رسیده است .";
                    title="اتمام زمان پروژه";


                }
                if(msgtype.equals("5"))
                {
                    try {


                        String task_name="";
                        JSONObject TaskInfo = new JSONObject(extras.getString("task_info"));
                        //  TaskInfo=TaskInfo.getJSONObject("taskInfo");
                        // manager=TaskInfo.getString("managerUser");

                        Log.d("taskInfoBundle", TaskInfo.toString());
                        taskID = TaskInfo.getInt("taskID");
                        task_name=TaskInfo.getString("taskName");

                        manager = TaskInfo.getString("manager");

                        msg ="زمان وظیفه ی "+task_name+"در پروژه ی"+" "+sentence+" "+"به پایان رسیده است .";
                        title ="اتمام زمان وظیفه شما";

                    }
                    catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Problem in catching ", "hichi");


                }


                }
                if(msgtype.equals("6"))
                {
                    try {

                        String task_name="";

                        JSONObject TaskInfo = new JSONObject(extras.getString("task_info"));
                        //  TaskInfo=TaskInfo.getJSONObject("taskInfo");
                        // manager=TaskInfo.getString("managerUser");

                        Log.d("taskInfoBundle", TaskInfo.toString());
                        taskID = TaskInfo.getInt("taskID");
                        task_name=TaskInfo.getString("taskName");

                        manager = TaskInfo.getString("manager");

                        msg="زمان وظیفه ی "+task_name+"برای کاربر "+" "+sentence+" "+" به پایان رسیده است.";

                        title ="اتمام زمان وظیفه یک کاربر";
                    }

                    catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Problem in catching ", "hichi");


                }

                }

                Log.i(TAG, "Received: " + extras.toString());
                sendNotification(msg);
            }
        }
        NotificationReceiver.completeWakefulIntent(intent);
       /*if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }*/
   }
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent=new Intent();
        if(msgtype.equals("1"))//taskCard
        {
            Log.d("mesgType1","1");
            intent = new Intent(this, TaskCard.class);
            intent.putExtra("taskID", taskID);
            Log.d("intenUsername",username);
            Log.d("intentManger",manager);
            if (username.equals(manager))
            {
                Log.d("manager is","yes");
                intent.putExtra("manager", true);
            } else
                intent.putExtra("manager", false);
        }

        if(msgtype.equals("2"))
        {
            intent = new Intent(this, Home.class);


        }

        if(msgtype.equals("3"))
        {
            intent = new Intent(this, TaskCard.class);
            intent.putExtra("taskID", taskID);
            if (username.equals(manager))
            {
                Log.d("manager is","yes");
                intent.putExtra("manager", true);
            } else
                intent.putExtra("manager", false);

        }
        if(msgtype.equals("4"))
        {
            intent = new Intent(this, Home.class);


        }
        if(msgtype.equals("5"))
        {
            intent = new Intent(this, Home.class);


        }


            //  startActivity();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(""))
                        .setContentText(msg);
        mBuilder.setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        NOTIFICATION_ID=NOTIFICATION_ID+1;
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

