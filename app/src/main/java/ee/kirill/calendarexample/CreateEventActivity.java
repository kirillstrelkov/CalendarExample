package ee.kirill.calendarexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CreateEventActivity extends Activity {
    public static final java.text.DateFormat DATE_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private static final String TAG = "CreateEventActivity";
    private EditText textStartDate;
    private EditText textEndDate;
    private EditText textTitle;
    private EditText textLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        textStartDate = (EditText) findViewById(R.id.textStartDate);
        textEndDate = (EditText) findViewById(R.id.textEndDate);
        textTitle = (EditText) findViewById(R.id.textTitle);
        textLocation = (EditText) findViewById(R.id.textLocation);

        OnDateTimeClickListener listener = new OnDateTimeClickListener();
        textStartDate.setOnClickListener(listener);
        textEndDate.setOnClickListener(listener);
    }

    public void createEvent(View view) {
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);

        try {
            Date startDate = DATE_FORMAT.parse(textStartDate.getText().toString());
            Date endDate = DATE_FORMAT.parse(textEndDate.getText().toString());

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);

            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startCalendar.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endCalendar.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.Events.TITLE, textTitle.getText().toString());
            calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, textLocation.getText().toString());

            startActivity(Intent.createChooser(calendarIntent, "Create event"));
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    class OnDateTimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View textView) {
            Log.v(TAG, "onClick");

            final Context context = textView.getContext();
            final Calendar c = Calendar.getInstance();
            final int year = c.get(Calendar.YEAR);
            final int month = c.get(Calendar.MONTH);
            final int day = c.get(Calendar.DAY_OF_MONTH);
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            final int minute = c.get(Calendar.MINUTE);

            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                    Log.v(TAG, "onDateSet");
                    calendar.set(year, monthOfYear, dayOfMonth);
                    Log.v(TAG, "Selected date:" + calendar.getTime());

                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Log.v(TAG, "onTimeSet");
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            Log.v(TAG, "Selected time:" + calendar.getTime());
                            EditText editText = (EditText) textView;
                            editText.setText(DATE_FORMAT.format(calendar.getTime()));
                        }
                    }, hour, minute, DateFormat.is24HourFormat(context));

                    timePickerDialog.show();
                }
            }, year, month, day);

            datePickerDialog.show();
        }
    }
}
