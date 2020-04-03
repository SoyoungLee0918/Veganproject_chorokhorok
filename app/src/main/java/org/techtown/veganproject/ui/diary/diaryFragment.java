package org.techtown.veganproject.ui.diary;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;


import org.techtown.veganproject.R;
import org.techtown.veganproject.ui.diary.diaryViewModel;

public class diaryFragment extends Fragment {

    private org.techtown.veganproject.ui.diary.diaryViewModel diaryViewModel;
    View root;
    Button viewBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diaryViewModel =
                ViewModelProviders.of(this).get(org.techtown.veganproject.ui.diary.diaryViewModel.class);

        root = inflater.inflate(R.layout.fragment_diary, container, false);
        final DatePicker textView = root.findViewById(R.id.datePicker);



        viewBtn = root.findViewById(R.id.view_button);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), diary_view.class);
                startActivity(intent);
            }
        });

        diaryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

}