package id.ac.unud1805551038.projectprogmob.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.unud1805551038.projectprogmob.Adapter.AccountTaskAdapter;
import id.ac.unud1805551038.projectprogmob.AuthActivity;
import id.ac.unud1805551038.projectprogmob.Constant;
import id.ac.unud1805551038.projectprogmob.EditPasswordActivity;
import id.ac.unud1805551038.projectprogmob.EditProfileActvity;
import id.ac.unud1805551038.projectprogmob.EditTaskActivity;
import id.ac.unud1805551038.projectprogmob.HomeActivity;
import id.ac.unud1805551038.projectprogmob.MainActivity;
import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.R;
import id.ac.unud1805551038.projectprogmob.UserInfoActivity;

public class AccountFragment extends Fragment {
    private View view;
    private MaterialToolbar toolbar;
    private CircleImageView imgProfile;
    private TextView txtName, txtTaskCount;
    private Button btnEditAccount, btnLogout;
    private RecyclerView recyclerView;
    private ArrayList<Task> arrayList;
    private AccountTaskAdapter adapter;
    private String imgUrl ="";
    ImageButton btnOption;

    private SharedPreferences preferences;

    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account, container, false);
        init();
        return view;
    }

    private void init() {
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        toolbar = view.findViewById(R.id.toolbarAccount);
        imgProfile = view.findViewById(R.id.imgAccountProfile);
        txtName = view.findViewById(R.id.txtAccountName);
        txtTaskCount = view.findViewById(R.id.txtAccounTaskCount);
        btnLogout = view.findViewById(R.id.item_logout);
        btnOption = view.findViewById(R.id.btnTaskOption);


        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), btnOption);
                popupMenu.inflate(R.menu.menu_account);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit: {
                                Intent i = new Intent(getActivity(), EditProfileActvity.class);
                                startActivity(i);
                                return true;
                            }
                            case R.id.item_delete: {
                                Intent i = new Intent(getActivity(), EditPasswordActivity.class);
                                startActivity(i);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }    });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void getData() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_PROFILE, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    txtName.setText(user.getString("name")+" "+user.getString("lastname"));
                    txtTaskCount.setText(object.getString("totalTask"));
                    Picasso.get().load(Constant.URL+"storage/profiles/"+user.getString("photo")).into(imgProfile);
                    imgUrl = Constant.URL+"storage/profiles/"+user.getString("photo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error->{
            error.printStackTrace();
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure for log out?");
        builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(getContext(),"Logout Success", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
