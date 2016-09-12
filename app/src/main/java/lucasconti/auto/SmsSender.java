package lucasconti.auto;

import android.app.PendingIntent;
import android.content.Context;
import android.telephony.SmsManager;

import java.util.Queue;

/**
 * Created by Lucas on 9/7/2016.
 */
public class SmsSender {
    private static SmsSender mSender;
    private Queue<Message> mMessageQueue;
    private SmsManager mSmsManager;

    private SmsSender() {
        mSmsManager = SmsManager.getDefault();
    }

    public static SmsSender get() {
        if (mSender == null) {
            return new SmsSender();
        }
        return mSender;
    }

    public void addMessageToSendQueue(String number, String text) {
        Message message = new Message(number, text);
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        String number = message.getNumber();
        if ( number.charAt(0) != '1') {
            number = "+1" + number;
        }
        else {
            number = "+" + number;
        }
//        PendingIntent intent = PendingIntent.getBroadcast(context, )
        mSmsManager.sendTextMessage(number, null, message.getText(), null, null);
    }

    private class Message {
        private String mNumber;
        private String mText;

        public Message(String number, String text) {
            mNumber = number;
            mText = text;
        }

        public String getNumber() {
            return mNumber;
        }

        public String getText() {
            return mText;
        }
    }
}
