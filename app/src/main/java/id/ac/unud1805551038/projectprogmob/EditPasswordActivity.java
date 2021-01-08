package id.ac.unud1805551038.projectprogmob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPasswordActivity extends AppCompatActivity {

    private Button button, btnEditPass;
    private TextInputLayout currentLayout, newLayout, confirmLayout;
    private EditText currentPass, newPass, confirmPass;
    String idLogin, tokenLogin;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        init();
    }

    public void init(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
        tokenLogin = userPref.getString("token", null);
        currentLayout = (TextInputLayout)findViewById(R.id.txtCurrentPassLayout);
        newLayout = (TextInputLayout)findViewById(R.id.txtNewPassLayout);
        confirmLayout = (TextInputLayout)findViewById(R.id.txtConfirmPassLayout);

        currentPass = (EditText)findViewById(R.id.txtCurrentPass);
        newPass = (EditText)findViewById(R.id.txtNewPass);
        confirmPass = (EditText)findViewById(R.id.txtConfirmPass);

        button = (Button) findViewById(R.id.btnCancel);
        btnEditPass = (Button)findViewById(R.id.btnEditPassword);

        dialog = new ProgressDialog(EditPasswordActivity.this);
        dialog.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    editPass();
                }
            }
        });

        currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (currentPass.getText().length()>7){
                    currentLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (newPass.getText().length()>7){
                    newLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (confirmPass.getText().toString().equals(newPass.getText().toString())){
                    confirmLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void editPass(){
        dialog.setMessage("Updating Password");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.UPDATE_PASSWORD, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "Password Has Been Changed", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else if(!object1.getBoolean("success")){
                    Toast.makeText(getApplicationContext(), "Current Password is Invalid", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Edit Data Failed", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        },error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + tokenLogin);
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("password", currentPass.getText().toString());
                map.put("newPass", newPass.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private boolean validate(){
        if(currentPass.getText().toString().length()<8){
            currentLayout.setErrorEnabled(true);
            currentLayout.setError("Current Password must be at least 8 characters");
            return false;
        }
        if(newPass.getText().toString().length()<8){
            newLayout.setErrorEnabled(true);
            newLayout.setError("New Password must be at least 8 characters");
            return false;
        }
        if(!newPass.getText().toString().equals(confirmPass.getText().toString())){
            confirmLayout.setErrorEnabled(true);
            confirmLayout.setError("Password not match");
            return false;
        }
        return true;
    }
}