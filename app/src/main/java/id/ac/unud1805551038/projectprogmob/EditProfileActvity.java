package id.ac.unud1805551038.projectprogmob;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.unud1805551038.projectprogmob.Fragments.AccountFragment;
import id.ac.unud1805551038.projectprogmob.Fragments.SignInFragment;

public class EditProfileActvity extends AppCompatActivity {

    private Button button, btnEdit;
    private TextView txtName, txtfirst, txtlast, txthp, photo;
    TextInputLayout layoutName, layoutfirst, layoutLastName, layouthp;
    int idlogin;
    String tokenLogin;
    ProgressDialog dialog;
    CircleImageView circleImageView;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
        idlogin = userPref.getInt("id", 0);
        tokenLogin = userPref.getString("token", null);
        isLogin();
        init();
    }

    public void openActivity() {
        Intent intent1 = new Intent(EditProfileActvity.this, AccountFragment.class);
        startActivity(intent1);
    }

    private void init() {
        button = findViewById(R.id.btnEditSave);
        btnEdit = findViewById(R.id.btnEditSave);
        photo = findViewById(R.id.txtEditSelectPhoto);
        circleImageView = findViewById(R.id.imgEditUserInfo);

        layoutfirst = findViewById(R.id.txtEditLayoutFirstUserInfo);
        layoutLastName = findViewById(R.id.txtEditLayoutLastnameUserInfo);
        layoutName = findViewById(R.id.txtEditLayoutNameUserInfo);
        layouthp = findViewById(R.id.txtEditLayoutHp);

        txtfirst = findViewById(R.id.txtEditFirstUserInfo);
        txtlast = findViewById(R.id.txtEditLastnameUserInfo);
        txtName = findViewById(R.id.txtEditNameUserInfo);
        txthp = findViewById(R.id.txtEditHp);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_ADD_PROFILE);
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity();
//            }
//        });

        getUserInfo();
        setUserInfo();

        dialog = new ProgressDialog(EditProfileActvity.this);
        dialog.setCancelable(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    edit();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openActivity();
                        }
                    }, 1000);
                }
            }
        });

        txtfirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtfirst.getText().toString().isEmpty()) {
                    layoutfirst.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtlast.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtlast.getText().toString().isEmpty()) {
                    layoutLastName.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txthp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getUserInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_USER, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    JSONObject user = object1.getJSONObject("user");
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("name", user.getString("name"));
                    editor.putString("first_name", user.getString("first_name"));
                    editor.putString("last_name", user.getString("last_name"));
                    editor.putString("no_hp", user.getString("no_hp"));
                    editor.putString("photo", user.getString("photo"));
                    editor.apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Get Data Failed", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + tokenLogin);
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", idlogin + "");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void setUserInfo() {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);

        String firstName = userPref.getString("first_name", null);
        String lastName = userPref.getString("last_name", null);
        String name = userPref.getString("name", null);
        String hp = userPref.getString("no_hp", null);

        Picasso.get().load(Constant.URL + "storage/profiles/" + userPref.getString("photo", null)).into(circleImageView);

        txtfirst.setText(firstName);
        txtlast.setText(lastName);
        txtName.setText(name);
        txthp.setText(hp);
        //email.setText(emailLogin);
    }

    private void isLogin() {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            isFirstTime();
        }
    }

    public void isFirstTime() {
        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            startActivity(new Intent(EditProfileActvity.this, EditProfileActvity.class));
        } else {
            startActivity(new Intent(EditProfileActvity.this, SignInFragment.class));
            finish();
        }
    }

    private void edit() {
        dialog.setMessage("Updating Data");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.SET_USER, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    JSONObject user = object1.getJSONObject("user");
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("first_name", user.getString("first_name"));
                    editor.putString("last_name", user.getString("last_name"));
                    //editor.putString("email", user.getString("email"));
                    editor.putString("photo", user.getString("photo"));
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Edit Success", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Edit Data Failed", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }, error -> {
            error.printStackTrace();
            dialog.dismiss();
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);


                headers.put("Authorization", "Bearer " + tokenLogin);
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", idlogin + "");
                map.put("name", txtName.getText().toString());
                map.put("first_name", txtfirst.getText().toString());
                map.put("last_name", txtlast.getText().toString());
                map.put("no_hp", txthp.getText().toString());
                map.put("photo", bitmapToString(bitmap));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_PROFILE && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            circleImageView.setImageURI(imgUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }

        return "";

    }

    private boolean validate() {
        if (txtfirst.getText().toString().length() < 3) {
            layoutfirst.setErrorEnabled(true);
            layoutfirst.setError("Name must be at least 8");
            return false;
        }
        if (txtlast.getText().toString().length() < 3) {
            layoutLastName.setErrorEnabled(true);
            layoutLastName.setError("Name must be at least 8");
            return false;
        }
        return true;
    }
}
