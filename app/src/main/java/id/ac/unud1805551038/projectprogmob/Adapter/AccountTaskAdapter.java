package id.ac.unud1805551038.projectprogmob.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.R;

public class AccountTaskAdapter extends RecyclerView.Adapter<AccountTaskAdapter.AccountTaskHolder> {

    private Context context;
    private ArrayList<Task> arrayList;

    public AccountTaskAdapter(Context context, ArrayList<Task> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AccountTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_account_task, parent,false);
        return new AccountTaskHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountTaskHolder holder, int position) {
        //Picasso.get().load(arrayList.get(position).getPhoto()).into(holder.imageView);
    }

    public int getItemCount(){
        return arrayList.size();
    }

    class AccountTaskHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public AccountTaskHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAccountTask);
        }
    }
}

