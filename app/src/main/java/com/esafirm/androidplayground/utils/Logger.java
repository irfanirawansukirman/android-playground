package com.esafirm.androidplayground.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

@SuppressWarnings("unchecked")
public class Logger {

    private static SerializedSubject subject = new SerializedSubject<>(PublishSubject.create());
    private static final StringBuilder loggerBuilder = new StringBuilder();

    public static void clear() {
        loggerBuilder.setLength(0);
    }

    public static void log(Object o) {
        String text = o != null ? o.toString() : "Object null";
        System.out.println(text);
        loggerBuilder.append(text).append("\n");
        subject.onNext(loggerBuilder.toString());
    }

    public static String getLogText() {
        return loggerBuilder.toString();
    }

    private static Observable<String> getObs() {
        return subject.startWith(getLogText());
    }

    public static ViewGroup getLogView(Context context) {
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        final TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.BLACK);
        textView.setPadding(padding, padding / 2, padding, padding / 2);

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(textView);

        getObs().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                textView.setText(null);
                textView.setText(s);
            }
        });

        return scrollView;
    }
}
