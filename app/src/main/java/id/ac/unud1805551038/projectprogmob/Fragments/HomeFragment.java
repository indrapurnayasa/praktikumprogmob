package id.ac.unud1805551038.projectprogmob.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.ac.unud1805551038.projectprogmob.Adapter.TaskAdapter;
import id.ac.unud1805551038.projectprogmob.AddTaskActivity;
import id.ac.unud1805551038.projectprogmob.AuthActivity;
import id.ac.unud1805551038.projectprogmob.Constant;
import id.ac.unud1805551038.projectprogmob.HomeActivity;
import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.Models.User;
import id.ac.unud1805551038.projectprogmob.R;

public class HomeFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Task> taskList;
    private ArrayList<User> userList;
    private SwipeRefreshLayout refreshLayout;
    private TaskAdapter taskAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home,container,false);
        init();
        return view;
    }

    private void init() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipeHome);
        toolbar = view.findViewById(R.id.toolbarHome);
        
        getTasks();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTasks();
            }
        });
    }

    private void getTasks() {
        taskList = new ArrayList<>();
        userList = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest request = new StringRequest(Request.Method.GET, Constant.TASK,response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {

                    JSONArray array = new JSONArray(object.getString("task"));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject taskObject = array.getJSONObject(i);
                        JSONObject userObject = taskObject.getJSONObject("user");
                        Log.d("aaa", userObject.getString("name"));

                        User user = new User();
                        user.setId(userObject.getInt("id"));
                        user.setName(userObject.getString("name"));
                        user.setLastname(userObject.getString("lastname"));
                        user.setEmail(userObject.getString("email"));
                        user.setPhoto(userObject.getString("photo"));
                        userList.add(user);

                        Task task = new Task();
                        task.setId(taskObject.getInt("id"));
                        task.setName_class(taskObject.getString("name_class"));
                        task.setDate(taskObject.getString("date"));
                        task.setMeeting(taskObject.getString("meeting"));
                        task.setCreated_at(taskObject.getString("created_at"));
                        task.setUser_id(taskObject.getInt("user_id"));
                        taskList.add(task);
                    }

                    taskAdapter = new TaskAdapter(getContext(), taskList, userList);
                    recyclerView.setAdapter(taskAdapter);
                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }

            refreshLayout.setRefreshing(false);

        },error -> {
            error.printStackTrace();
            refreshLayout.setRefreshing(false);
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authoriation","Bearer "+token);
                return super.getHeaders();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


}
