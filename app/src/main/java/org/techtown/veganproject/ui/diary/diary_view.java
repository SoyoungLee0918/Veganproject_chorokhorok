package org.techtown.veganproject.ui.diary;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.techtown.veganproject.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;


public class diary_view extends AppCompatActivity{



    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_view);

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);


        /*Intent intent =getIntent();
        int cYear = intent.getExtras().getInt("year");
        int cMonth = intent.getExtras().getInt("month");
        int cDay = intent.getExtras().getInt("day");

        int year = intent.getExtras().getInt("s_year");
        int monthOfYear = intent.getExtras().getInt("s_month");
        monthOfYear=monthOfYear+1;
        int dayOfMonth = intent.getExtras().getInt("s_day");*/

       /* Bundle args = new Bundle();
        args.putInt("year",cYear);
        args.putInt("month", cMonth);
        args.putInt("day",cDay);

        args.putInt("s_year",year);
        args.putInt("s_month",monthOfYear);
        args.putInt("s_day",dayOfMonth);

        Fragment fragment = new blackfastFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        fragment.setArguments(args);
        fmt.replace(R.id.blackfast_tab,fragment).addToBackStack(null).commit();*/



    }




}
