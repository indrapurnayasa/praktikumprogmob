package id.ac.unud1805551038.projectprogmob.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.unud1805551038.projectprogmob.Constant;
import id.ac.unud1805551038.projectprogmob.Fragments.SignInFragment;
import id.ac.unud1805551038.projectprogmob.HomeActivity;
import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.Models.User;
import id.ac.unud1805551038.projectprogmob.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder>{


    private Context context;
    private ArrayList<Task> listTask;
    private ArrayList<User> listUser;
    private SharedPreferences userPref;

    public TaskAdapter(@NonNull Context context, ArrayList<Task> listTask, ArrayList<User> listUser) {
        this.context = context;
        this.listTask = listTask;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task,parent,false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = listTask.get(position);
        User user = listUser.get(position);
        Picasso.get().load(Constant.URL+"storage/profiles/"+user.getPhoto()).into(holder.imgProfile);
        //Picasso.get().load(Constant.URL+"storage/tasks/"+task.getPhoto()).into(holder.imgTask);
        holder.txtName.setText(user.getName()+" "+user.getLastname());
        holder.txtDate.setText("Tanggal: "+task.getDate());
        holder.txtClass.setText("Kelas: "+task.getName_class());
        holder.txtMeeting.setText("Link Meeting"+task.getMeeting());
        Log.d("xxx","aa"+userPref.getInt("id", 0));
        if(task.getUser_id()==2){
            holder.btnTaskOption.setVisibility(View.VISIBLE);
        }else{
            holder.btnTaskOption.setVisibility(View.GONE);
        }

        holder.btnTaskOption.setOnClickListener(V->{
            PopupMenu popupMenu = new PopupMenu(context, holder.btnTaskOption);
            popupMenu.inflate(R.menu.menu_task_option);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.item_edit:{
//                            nt i = new Intent(((HomeActivity)context), EditPostActivity.class);
//                            i.putExtra("id", task.getId());
//                            i.putExtra("position", position);
//                            i.putExtra("desc",task.getDesc());
//                            i.putExtra("matapelajaran", task.getMatapelajaran());
//                            context.startActivity(i);
//                            return true;
                            Toast.makeText(context,"Ini Menu Edit", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        case R.id.item_delete:{
                            deleteTask(task.getId(), position);
                            return true;
                        }
                    }
                    return false;
                }

                private void deleteTask(int task_id, int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm");
                    builder.setMessage("Delete Task?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringRequest request = new StringRequest(Request.Method.POST, Constant.DELETE_TASK, response -> {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if(object.getBoolean("success")) {
                                        listTask.remove(position);
                                        listUser.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context,"Delete Success", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, error -> {
                            }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    String token = userPref.getString("token", "");
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("Authorization","Bearer "+token);
                                    return map;
                                }

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("id", String.valueOf(task.getId()));
                                    return map;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(context);
                            queue.add(request);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    class TaskHolder extends RecyclerView.ViewHolder{

        private TextView txtName, txtDate,txtClass, txtMeeting;
        private CircleImageView imgProfile;
        private ImageView imgTask;
        private ImageButton btnTaskOption, btnComment;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            userPref =  context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            txtName = itemView.findViewById(R.id.txtTaskName);
            txtDate = itemView.findViewById(R.id.txtTanggalClass);
            txtClass = itemView.findViewById(R.id.txtNameClassList);
            txtMeeting = itemView.findViewById(R.id.txtMeetingLink);
            //txtDate = itemView.findViewById(R.id.txtTaskDate);
            //txtDesc = itemView.findViewById(R.id.txtTaskDesc);
            //txtComment = itemView.findViewById(R.id.txtTaskComment);
            imgProfile = itemView.findViewById(R.id.imgTaskProfile);
            //imgTask = itemView.findViewById(R.id.imgTaskPhoto);
            btnTaskOption = itemView.findViewById(R.id.btnTaskOption);
            //btnComment = itemView.findViewById(R.id.btnTaskComment);
            //btnTaskOption.setVisibility(View.GONE);
        }
    }
}
