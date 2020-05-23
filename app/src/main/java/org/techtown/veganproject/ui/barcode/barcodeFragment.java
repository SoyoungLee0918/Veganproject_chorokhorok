package org.techtown.veganproject.ui.barcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.veganproject.MainActivity;
import org.techtown.veganproject.R;
//import org.techtown.veganproject.ui.barcode.barcodeViewModel;
//import org.techtown.veganproject.ui.barcode.barcodeFragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class barcodeFragment extends Fragment {
    private static String IP_ADDRESS = "192.168.43.76";   //10.0.2.2  //192.168.200.102  //192.168.43.76
    private static String TAG = "test100";

    private TextView mTextViewResult;
    private ArrayList<PersonalData> mArrayList;
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText mEditTextSearchKeyword;
    private String mJsonString;

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


        mTextViewResult = root.findViewById(R.id.textView_main_result);
        mRecyclerView = root.findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mEditTextSearchKeyword = root.findViewById(R.id.editText_main_searchKeyword);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
        mArrayList = new ArrayList<>();

        mAdapter = new UsersAdapter(getActivity(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        Button button_search = root.findViewById(R.id.button_main_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();
                mAdapter.notifyDataSetChanged();

                String Keyword =  mEditTextSearchKeyword.getText().toString();
                mEditTextSearchKeyword.setText("");

                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/query.php", Keyword);
            }
        });


        buttonScan = root.findViewById(R.id.buttonScan);
        qrScan = new IntentIntegrator(getActivity());     ///getActivity()가 관건
        mEditTextSearchKeyword = root.findViewById(R.id.editText_main_searchKeyword);

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
                    mEditTextSearchKeyword.setText(result.getContents()); //바코드
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    private class GetData extends AsyncTask<String, Void, String> {  //바코드 번호 검색하기

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "barcode=" + params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }


    private void showResult(){   //입력한 바코드에 해당하는 제품명, 바코드번호, 성분 출력

        String TAG_JSON="webnautes";
        String TAG_ID = "prdlstNm";
        String TAG_NAME = "barcode";
        String TAG_COUNTRY ="rawmtrl";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String name = item.getString(TAG_NAME);
                String address = item.getString(TAG_COUNTRY);

                PersonalData personalData = new PersonalData();

                personalData.setMember_id(id);
                personalData.setMember_name(name);
                personalData.setMember_country(address);

                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


}