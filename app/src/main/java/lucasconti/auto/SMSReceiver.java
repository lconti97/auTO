package lucasconti.auto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Lucas on 6/2/2016.
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    // Deprecated in only API 23
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
//                sendSMS(messages[0].getOriginatingAddress());
            }
        }
    }

    private void sendSMS(String target) {
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(target, null, "test", null, null);
    }
}
