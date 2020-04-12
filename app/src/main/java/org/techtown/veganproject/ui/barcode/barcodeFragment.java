package org.techtown.veganproject.ui.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.veganproject.MainActivity;
import org.techtown.veganproject.R;
//import org.techtown.veganproject.ui.barcode.barcodeViewModel;
//import org.techtown.veganproject.ui.barcode.barcodeFragment;

import static java.security.AccessController.getContext;


public class barcodeFragment extends Fragment {

    private org.techtown.veganproject.ui.barcode.barcodeViewModel barcodeViewModel;
    View root;
    barcodeFragment bf = this;//<<this에서 오류나서 생성함!!!!
    //view Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress, textViewResult;
    private IntentIntegrator qrScan;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // barcodeViewModel =
         //       ViewModelProviders.of(this).get(org.techtown.veganproject.ui.barcode.barcodeViewModel.class);

        root = inflater.inflate(R.layout.fragment_barcode, container, false);

        //barcodeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
         //   @Override
           // public void onChanged(@Nullable String s) {

           // }
        //});


        buttonScan = root.findViewById(R.id.buttonScan);
        qrScan = new IntentIntegrator(getActivity());     ///getActivity()가 관건
        textViewName = root.findViewById(R.id.textViewName);
        textViewAddress = root.findViewById(R.id.textViewAddress);
        textViewResult = root.findViewById(R.id.textViewResult);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                //qrScan.initiateScan();
                IntentIntegrator.forSupportFragment(bf).initiateScan();  //<<이부분 함수를 바꿨습니다!!!!!
                // IntentIntegrator.forFragment(bf).initiateScan();
                //Log.d("Scan..: ", "Scanning..");
            }

        });
         //여기까진 돌아감
        return root;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {
                //qrcode 가 없으면

                if (result.getContents() == null) {
                    Toast.makeText(getContext(), "취소!", Toast.LENGTH_SHORT).show();
                } else {
                    //qrcode 결과가 있으면
                    Toast.makeText(getContext(), "스캔완료!", Toast.LENGTH_SHORT).show();
                    //Log.d("성공: ", "성공!");
                    try {
                        //data를 json으로 변환
                        JSONObject obj = new JSONObject(result.getContents());
                        textViewName.setText(obj.getString("name")); //이름
                        textViewAddress.setText(obj.getString("address")); //url
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                        textViewResult.setText(result.getContents()); //바코드
                    }
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

    }
}