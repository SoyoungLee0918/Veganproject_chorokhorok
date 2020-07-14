package org.techtown.veganproject.ui.barcode;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.techtown.veganproject.R;

import java.util.ArrayList;
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView barcode;
        protected TextView img;
        protected TextView vegantype;
        protected TextView raw;


        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.textView_list_id);
            this.barcode = (TextView) view.findViewById(R.id.textView_list_barcode);
            this.img = (TextView) view.findViewById(R.id.textView_list_img);
            this.vegantype = (TextView) view.findViewById(R.id.textView_list_vegantype);
            this.raw = (TextView) view.findViewById(R.id.textView_list_raw);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.id.setText(mList.get(position).getMember_id());
        viewholder.barcode.setText(mList.get(position).getMember_barcode());
        viewholder.img.setText(mList.get(position).getMember_img());
        viewholder.vegantype.setText(mList.get(position).getMember_vegantype());
        viewholder.raw.setText(mList.get(position).getMember_raw());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}