package org.techtown.veganproject.ui.barcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import org.techtown.veganproject.R;

public class barcodeFragment extends Fragment {

    private barcodeViewModel barcodeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        barcodeViewModel =
                ViewModelProviders.of(this).get(barcodeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_barcode, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        barcodeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}