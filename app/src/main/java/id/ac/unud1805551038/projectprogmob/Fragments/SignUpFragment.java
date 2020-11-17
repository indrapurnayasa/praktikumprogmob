package id.ac.unud1805551038.projectprogmob.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.ac.unud1805551038.projectprogmob.Constant;
import id.ac.unud1805551038.projectprogmob.R;

public class SignUpFragment extends Fragment {
    private View view;
    private TextInputLayout layoutEmail, layoutPassword,layoutConfirm, layoutName;
    private TextInputEditText txtEmail, txtPassword,txtConfirm,txtName;
    private TextView txtSignIn;
    private Button btnSignUp;

    private ProgressDialog dialog;

    public SignUpFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sign_up,container,false);
        init();
        return view;
    }

    private void init() {
        layoutPassword = view.findViewById(R.id.txtLayoutPasswordSignUp);
        layoutEmail = view.findViewById(R.id.txtLayoutEmailSignUp);
        layoutConfirm = view.findViewById(R.id.txtLayoutConfirmSignUp);
        layoutName = view.findViewById(R.id.txtLayoutName);
        txtName = view.findViewById(R.id.txtName);
        txtPassword = view.findViewById(R.id.txtPasswordSignUp);
        txtConfirm = view.findViewById(R.id.txtConfirmSignUp);
        txtSignIn = view.findViewById(R.id.txtSignIn);
        txtEmail = view.findViewById(R.id.txtEmailSignUp);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change fragments
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment()). commit();
            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtEmail.getText().toString().isEmpty()){
                    layoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtName.getText().toString().length()>9){
                    layoutName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtPassword.getText().toString().length()>7){
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtConfirm.getText().toString().equals(txtPassword.getText().toString())){
                    layoutConfirm.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate fields first
                if (validate()){
                    register();
                }
            }
        });
    }

        private boolean validate (){
            if (txtEmail.getText().toString().isEmpty()){
                layoutEmail.setErrorEnabled(true);
                layoutEmail.setError("Email is Required");
                return false;
            }
            if (txtName.getText().toString().length()<10){
                layoutName.setErrorEnabled(true);
                layoutName.setError("Name must be at least 10 characters");
                return false;
            }
            if (txtPassword.getText().toString().length()<8){
                layoutPassword.setErrorEnabled(true);
                layoutPassword.setError("Required at least 8 characters");
                return false;
            }
            if (!txtConfirm.getText().toString().equals(txtPassword.getText().toString())){
                layoutConfirm.setErrorEnabled(true);
                layoutConfirm.setError("Password dose not match");
                return false;
            }

            return true;
        }

        private void register(){
            dialog.setMessage("Registering");
            dialog.show();
            StringRequest request =  new StringRequest(Request.Method.POST, Constant.REGISTER, response ->{

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("success")){
                        JSONObject user = object.getJSONObject("user");
                        SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("token",object.getString("token"));
                        editor.putString("name",user.getString("name"));
                        editor.putString("email",user.getString("email"));
                        editor.apply();
                        Toast.makeText(getContext(),"Register Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            },error -> {
                error.printStackTrace();
                dialog.dismiss();
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("email",txtEmail.getText().toString().trim());
                    map.put("name",txtName.getText().toString());
                    map.put("password",txtPassword.getText().toString());
                    map.put("password_confirmation",txtConfirm.getText().toString());
                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);

        }
}




