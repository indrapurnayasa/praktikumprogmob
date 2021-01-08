package id.ac.unud1805551038.projectprogmob;

import androidx.appcompat.app.AppCompatActivity;
import id.ac.unud1805551038.projectprogmob.Adapter.TaskAdapter;
import id.ac.unud1805551038.projectprogmob.Fragments.HomeFragment;
import id.ac.unud1805551038.projectprogmob.Models.Task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {

    private Button btnEdit, btnCancel, btnTgl;
    private EditText nameClass, date, meeting;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String tokenLogin;
    int position, idClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        init();
    }

    private void init(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
        tokenLogin = userPref.getString("token", null);
        dateFormatter = new SimpleDateFormat("YYYY-MM-dd", Locale.US);
        nameClass = (EditText)findViewById(R.id.txtNameClassEdit);
        date = (EditText)findViewById(R.id.txtDateClassEdit);
        meeting = (EditText)findViewById(R.id.txtMeetingClassEdit);
        btnEdit = (Button)findViewById(R.id.btnEditTask);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnTgl = (Button)findViewById(R.id.btnTanggal);

        getTask();
        setTask();


        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditTaskActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
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

    private void getTask(){
        if(getIntent().hasExtra("position")){
            position = getIntent().getIntExtra("position", 0);
        }
    }

    private void setTask(){
        Task task = HomeFragment.taskList.get(position);
        Log.d("ssss", task.getName_class());
        idClass = task.getId();
        nameClass.setText(task.getName_class());
        date.setText(task.getDate());
        meeting.setText(task.getMeeting());
    }

    private void editTask(){
        StringRequest request =  new StringRequest(Request.Method.POST, Constant.UPDATE_TASK, response ->{

            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Edit Class Success", Toast.LENGTH_SHORT).show();
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
                map.put("id", String.valueOf(idClass));
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