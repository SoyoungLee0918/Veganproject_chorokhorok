package org.techtown.veganproject.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;


import org.techtown.veganproject.R;
import org.techtown.veganproject.ui.diary.diaryViewModel;

import java.util.Calendar;

public class diaryFragment extends Fragment {

   // private org.techtown.veganproject.ui.diary.diaryViewModel diaryViewModel;
    View root;
    Button viewBtn;
    DatePicker datePicker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_diary, container, false);
        final DatePicker textView = root.findViewById(R.id.datePicker);
        datePicker = (DatePicker) root.findViewById(R.id.datePicker);

        Calendar c = Calendar.getInstance();
        final int cYear = c.get(Calendar.YEAR);
        final int cMonth = c.get(Calendar.MONTH);
        final int cDay = c.get(Calendar.DAY_OF_MONTH);

        viewBtn = root.findViewById(R.id.view_button);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Bundle args= new Bundle();
                args.putInt("year",cYear);
                args.putInt("month",cMonth);
                args.putInt("day",cDay);

                args.putInt("s_year",datePicker.getYear());
                args.putInt("s_month",datePicker.getMonth());
                args.putInt("s_day",datePicker.getDayOfMonth());

                Fragment fm = new Fragment();
                fm = new blackfastFragment();
                FragmentManager frm = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = frm.beginTransaction();
                fragmentTransaction.replace(R.id.blackfast_tab,fm);
                fragmentTransaction.commit();
                fm.setArguments(args);*/

               Intent intent = new Intent(getActivity(), diary_view.class);
                intent.putExtra("year",cYear);
                intent.putExtra("month",cMonth);
                intent.putExtra("day",cDay);

                intent.putExtra("s_year",datePicker.getYear());
                intent.putExtra("s_month",datePicker.getMonth());
                intent.putExtra("s_day",datePicker.getDayOfMonth());
                startActivity(intent);


            }
        });
        return root;
    }



}