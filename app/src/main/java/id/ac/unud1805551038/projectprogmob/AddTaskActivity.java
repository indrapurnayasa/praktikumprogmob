package id.ac.unud1805551038.projectprogmob;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.ac.unud1805551038.projectprogmob.Database.RoomDB;
import id.ac.unud1805551038.projectprogmob.Models.Task;

public class AddTaskActivity extends AppCompatActivity {

    private Button btnAdd, btnTgl;
    private EditText nameClass, date, meeting;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String tokenLogin;
//    RoomDB database;
//    public static ArrayList<Task> ListTask = new ArrayList<>();
//    public static ArrayList<Task> ListTaskBackup = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        init();
    }

    private void init() {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
        tokenLogin = userPref.getString("token", null);

        dateFormatter = new SimpleDateFormat("YYYY-MM-dd",Locale.US);
        nameClass = (EditText)findViewById(R.id.txtNameClass);
        date = (EditText)findViewById(R.id.txtDateClass);
        meeting = (EditText)findViewById(R.id.txtMeetingClass);
        btnAdd = (Button)findViewById(R.id.btnAddClass);
        btnTgl = (Button)findViewById(R.id.btnTanggal);


        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void showDateDialog() {
        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


    public void cancelTask(View view) {

        super.onBackPressed();
    }

    private void addTask(){
//        ListTask.clear();
//        database = RoomDB.getInstance(getApplicationContext());
        FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");
        StringRequest request =  new StringRequest(Request.Method.POST, Constant.ADD_TASK, response ->{

            try {
                JSONObject object = new JSONObject(response);
                if(object.getInt("message_id")!=0){
//                    database.taskDao().deleteAll();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Add Class Success", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + tokenLogin);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("name_class",nameClass.getText().toString());
                map.put("date",date.getText().toString());
                map.put("meeting",meeting.getText().toString());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
