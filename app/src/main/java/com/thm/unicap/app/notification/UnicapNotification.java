package com.thm.unicap.app.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.grade.GradesActivity;
import com.thm.unicap.app.model.SubjectTest;

import java.util.List;

public class UnicapNotification {

    public static void notifyNewGrades(Context context) {

        if (UnicapApplication.getCurrentStudent() == null) return;

        List<SubjectTest> newSubjectTests = UnicapApplication.getCurrentStudent().getNewSubjectTests();

        for (SubjectTest subjectTest : newSubjectTests) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_unicap)
                    .setContentTitle(subjectTest.subject.name)
                    .setSubText(context.getString(R.string.app_name))
                    .setAutoCancel(true);

            switch (subjectTest.degree) {
                case FIRST_DEGREE:
                    builder.setContentText(context.getString(R.string.first_degree_available));
                    break;
                case SECOND_DEGREE:
                    builder.setContentText(context.getString(R.string.second_degree_available));
                    break;
                case FINAL_DEGREE:
                    builder.setContentText(context.getString(R.string.final_degree_available));
                    break;
            }

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, GradesActivity.class);
            resultIntent.putExtra("subject_id", subjectTest.subject.getId());

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.

            int mId = subjectTest.getId().intValue();
            mNotificationManager.notify(mId, builder.build());
            subjectTest.notify = false;
            subjectTest.save();
        }
    }
}
