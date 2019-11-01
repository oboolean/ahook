package com.oboolean.ahook;

import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import java.lang.reflect.Field;

public class MessageQueueProxy {

    private MessageQueue messageQueue;

    private Field messagesField;

    private boolean initSuccess = false;

    public MessageQueueProxy() {
        init();
    }

    private void init() {
        try {
            messagesField = MessageQueue.class.getDeclaredField("mMessages");
            messagesField.setAccessible(true);
            initSuccess = true;
        } catch (Exception e) {

        }

        if (initSuccess) {
            if (Build.VERSION.SDK_INT >= 23) {
                messageQueue = Looper.getMainLooper().getQueue();
            } else {
                try {
                    Field mQueueField = Looper.class.getDeclaredField("mQueue");
                    mQueueField.setAccessible(true);
                    messageQueue = (MessageQueue) mQueueField.get(Looper.getMainLooper());
                } catch (Exception e) {
                    initSuccess = false;
                }

            }
        }
    }

    public Message getCurrentMessage() {
        try {
            if (messagesField != null) {
                return (Message) messagesField.get(messageQueue);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }

}
