package com.example.vpmanager.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.vpmanager.R;

public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener {

        public HomeFragment fragment;
        public Dialog d;
        public Button yes, no;
        public EditText vps, matrikelnumber;
        public String vpString, matrikelString;
        public View cancelView;


        public CustomAlertDialog(HomeFragment a , String vp, String martikelnumber ) {
                super(a.getContext());
                fragment = a;
                vpString = vp;
                matrikelString = martikelnumber;
        }

        //Parameters:
        //Return Values
        //sets up the view and listeners for this instance. Adds given values to the edittexts
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.custom_dialog);
                yes = (Button) findViewById(R.id.addAnyways);
                no = (Button) findViewById(R.id.abort);
                yes.setOnClickListener(this);
                no.setOnClickListener(this);
                vps = findViewById(R.id.dialog_input_vps);
                matrikelnumber = findViewById(R.id.dialog_input_number);
                cancelView = findViewById(R.id.btn_close);
                cancelView.setOnClickListener(this);

                vps.setText(vpString);
                matrikelnumber.setText(matrikelString);
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.addAnyways:
                                fragment.closeDialog(vps.getText().toString(), matrikelnumber.getText().toString());
                                break;
                        case R.id.abort:
                                dismiss();
                                break;
                        case R.id.btn_close:
                                dismiss();
                        default:
                                break;
                }
                dismiss();
        }
}