package org.techtown.veganproject.ui.map;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.veganproject.MainActivity;
import org.techtown.veganproject.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    private ArrayList<bookmark_data>mDataset;
    DbHelper DbHelper;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, lan, lon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.name);

        }
    }
    public FavoriteAdapter(ArrayList<bookmark_data> myData){
        this.mDataset = myData; //bookmark_data

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_raw, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(mDataset.get(position).getName());

        //클릭이벤트
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                /*Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("Name",mDataset.get(position).getName());
                intent.putExtra("Lat",mDataset.get(position).getLat());
                intent.putExtra("Lon",mDataset.get(position).getLon());*/

                //activity.startActivity(intent);
                activity.finish();


            }
        });


    }


    @Override
    public int getItemCount() {
       // return mDataset.size();
        return (mDataset == null) ? 0 : mDataset.size();

    }
}
