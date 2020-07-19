package org.techtown.veganproject.ui.barcode;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.veganproject.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;
    Handler handler = new Handler();


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView barcode;
        protected TextView vegantype;
        protected TextView raw;
        protected ImageView imageView;


        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.textView_list_id);
            this.barcode = (TextView) view.findViewById(R.id.textView_list_barcode);
            //this.img = (TextView) view.findViewById(R.id.textView_list_img);
            this.imageView = (ImageView) view.findViewById(R.id.veganimg);
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
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {

        viewholder.id.setText(mList.get(position).getMember_id());
        viewholder.barcode.setText(mList.get(position).getMember_barcode());
        Thread uThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mList.get(position).getMember_img());
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    if (bm == null)
                        Log.d("비었나요?:", "네");
                    else
                        Log.d("비었나요?:", "아니?");
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            viewholder.imageView.setImageBitmap(bm);
                        }
                    });
                    //viewholder.imageView.setImageBitmap(bm);

                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        uThread.start(); // 작업 Thread 실행

        viewholder.vegantype.setText(mList.get(position).getMember_vegantype());
        viewholder.raw.setText(mList.get(position).getMember_raw());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
