package com.nulp.rostykboiko.aicalendar.editor;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.TextView;

import com.nulp.rostykboiko.aicalendar.R;
import com.nulp.rostykboiko.aicalendar.utils.firebase.dao.Event;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditorActivity extends AppCompatActivity {
    private Event event;
    @BindView(R.id.edTitle)
    EditText editTextTitle;
    @BindView(R.id.txtTimeStart)
    TextView eventTimeStartTxt;
    @BindView(R.id.txtTimeEnd)
    TextView eventTimeEndTxt;
    @BindView(R.id.txtDateStart)
    TextView eventDateStartTxt;
    @BindView(R.id.txtDateEnd)
    TextView eventDateEndTxt;

    private Calendar mCurrentDate;
    private DateAdapter dateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ButterKnife.bind(this);
        mCurrentDate = Calendar.getInstance();

        dateAdapter = new DateAdapter();
        dateInit();
    }

    private void dateInit() {
        // DateFormat : Ср,10 серпень 2016

        eventDateStartTxt.setText(dateAdapter.dateStringText(this));
        eventDateEndTxt.setText(dateAdapter.dateStringText(this));

        if (mCurrentDate.get(Calendar.HOUR_OF_DAY) < 12) {
            eventTimeStartTxt.setText("14:00");
            eventTimeEndTxt.setText("15:00");
        } else if (mCurrentDate.get(Calendar.HOUR_OF_DAY) < 17) {
            eventTimeStartTxt.setText("18:00");
            eventTimeEndTxt.setText("19:00");
        } else {
            eventTimeStartTxt.setText("10:00");
            eventTimeEndTxt.setText("11:00");
        }
    }

    @Override
    @OnClick(R.id.backBtn)
    public void onBackPressed() {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        confirmDialog.setMessage(R.string.editor_cancel_dialog);
        confirmDialog.setPositiveButton(R.string.positive_button_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                finish();
            }
        });
        confirmDialog.setNegativeButton(R.string.negative_button_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        confirmDialog.setCancelable(true);
        confirmDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        confirmDialog.show();
    }

}
