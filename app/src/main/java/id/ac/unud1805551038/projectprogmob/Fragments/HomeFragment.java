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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.ac.unud1805551038.projectprogmob.Adapter.TaskAdapter;
import id.ac.unud1805551038.projectprogmob.AddTaskActivity;
import id.ac.unud1805551038.projectprogmob.AuthActivity;
import id.ac.unud1805551038.projectprogmob.Constant;
import id.ac.unud1805551038.projectprogmob.Database.RoomDB;
import id.ac.unud1805551038.projectprogmob.HomeActivity;
import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.Models.User;
import id.ac.unud1805551038.projectprogmob.R;

public class HomeFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    public static ArrayList<Task> taskList;
    private ArrayList<User> userList;
    private SwipeRefreshLayout refreshLayout;
    private TaskAdapter taskAdapter, taskAdapter1;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    private ArrayList<Task> taskListBackup;
    private ArrayList<User> userListBackup;
    RoomDB database;

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
        taskListBackup = new ArrayList<>();
        userListBackup = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        database = RoomDB.getInstance(getContext().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.GET, Constant.TASK, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    database.taskDao().deleteAll();
                    database.userDao().deleteAll();
                    userListBackup.clear();
                    taskList.clear();
                    JSONArray array = new JSONArray(object.getString("task"));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject taskObject = array.getJSONObject(i);
                        JSONObject userObject = taskObject.getJSONObject("user");
                        Log.d("aaa", userObject.getString("name"));

                        User user = new User();
                        user.setIdNya(i);
                        user.setId(userObject.getInt("id"));
                        user.setName(userObject.getString("name"));
                        user.setLastname(userObject.getString("lastname"));
                        user.setEmail(userObject.getString("email"));
                        user.setPhoto(userObject.getString("photo"));
                        userList.add(user);
                        database.userDao().insertUser(user);

                        Task task = new Task();
                        task.setId(taskObject.getInt("id"));
                        task.setName_class(taskObject.getString("name_class"));
                        task.setDate(taskObject.getString("date"));
                        task.setMeeting(taskObject.getString("meeting"));
                        task.setCreated_at(taskObject.getString("created_at"));
                        task.setUser_id(taskObject.getInt("user_id"));
                        taskList.add(task);
                        database.taskDao().insetTask(task);
                    }

                    taskAdapter = new TaskAdapter(getContext(), taskList, userList);
                    recyclerView.setAdapter(taskAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            refreshLayout.setRefreshing(false);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError){
                    taskListBackup = (ArrayList<Task>) database.taskDao().loadAllPosts();
                    userListBackup = (ArrayList<User>) database.userDao().loadAllUsers();
                    taskAdapter1 = new TaskAdapter(getContext(), taskListBackup, userListBackup);
                    recyclerView.setAdapter(taskAdapter1);
                    refreshLayout.setRefreshing(false);
                }
            }
    }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
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


}
