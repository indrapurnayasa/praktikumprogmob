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
        recyclerView = view.findViewById(R.id.recyclerAccount);
        btnEditAccount = view.findViewById(R.id.btnEditAccount);
        btnLogout = view.findViewById(R.id.item_logout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));



        btnEditAccount.setOnClickListener(v -> {
            Intent i = new Intent(((HomeActivity)getContext()), UserInfoActivity.class);
            i.putExtra("imgUrl",imgUrl);
            startActivity(i);
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void getData() {
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.MY_TASK, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    JSONArray tasks = object.getJSONArray("tasks");
                    for (int i = 0; i < tasks.length(); i++){
                        JSONObject p = tasks.getJSONObject(i);

                        Task task = new Task();
                        //task.setPhoto(Constant.URL+"storage/task/"+p.getString("file"));
                        arrayList.add(task);
                    }
                    JSONObject user = object.getJSONObject("user");
                    txtName.setText(user.getString("name"));
                    txtTaskCount.setText(arrayList.size()+"");
                    Picasso.get().load(Constant.URL+"storage/profiles/"+user.getString("photo")).into(imgProfile);
                    adapter = new AccountTaskAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(adapter);
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
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
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
        builder.setMessage("Are You Sure to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
