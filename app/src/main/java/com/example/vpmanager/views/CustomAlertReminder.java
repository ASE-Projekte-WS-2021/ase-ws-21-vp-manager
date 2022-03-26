package com.example.vpmanager.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.vpmanager.R;

public class CustomAlertReminder extends Dialog implements View.OnClickListener {

        public personalAccountFragment fragment;
        public Dialog d;
        public Button yes;
        private TextView text;


        public CustomAlertReminder(personalAccountFragment a) {
                super(a.getContext());
                fragment = a;
        }
        public CustomAlertReminder(personalAccountFragment a, String alerText) {
                super(a.getContext());

                fragment = a;
                text.setText(alerText);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.custom_reminder);
                text = findViewById(R.id.customAlertText);
                yes = findViewById(R.id.reminder_dialog_ok);
                yes.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                dismiss();
        }
}