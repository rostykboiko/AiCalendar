package com.nulp.rostykboiko.aicalendar.utils.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.nulp.rostykboiko.aicalendar.utils.firebase.dao.Event;

public class Receiver extends BroadcastReceiver {
    private static final String SURVEY_KEY = "SURVEY_KEY";
    public static final String QUESTION_RECEIVED_FILTER = "QUESTION_RECEIVED_FILTER";

    @NonNull
    private QuestionReceivedCallback callback;

    public Receiver(@NonNull QuestionReceivedCallback callback){
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getStringExtra(SURVEY_KEY);
        Event event = new Gson().fromJson(json, Event.class);
        callback.onQuestionReceived(event);
    }

    public interface QuestionReceivedCallback {
        void onQuestionReceived(@NonNull Event event);
    }
}