package com.example.kyanro.minite_repeat_timer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;


public class MainActivity extends ActionBarActivity {

    @Bind(R.id.input1_etext)
    EditText input1;
    @Bind(R.id.input2_etext)
    EditText input2;
    @Bind(R.id.output_text)
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        BehaviorSubject<String> input1subject = BehaviorSubject.create();
        BehaviorSubject<String> input2subject = BehaviorSubject.create();

        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                input1subject.onNext(s.toString());
            }
        });

        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                input2subject.onNext(s.toString());
            }
        });

        input1subject.subscribe(str -> Log.d("test", "str"));

        // どっちも入力されていたら　ボタンを有効にする
        Observable.combineLatest(input1subject.startWith(""), input2subject.startWith(""),
                (id, password) -> {
                    if (TextUtils.isEmpty(id) || TextUtils.isEmpty(password)) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(output::setEnabled);


        input1subject
                .throttleLast(1, TimeUnit.SECONDS)    // 最初の入力を受けたら、以後の入力を３秒間無視する
                .filter(s -> s.startsWith("あ"))      // あ　から始まるテキストのみ有効
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(output::setText);            // 結果はテキストとして表示する

    }


}
