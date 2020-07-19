package org.techtown.veganproject.ui.diary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.veganproject.MainActivity;
import org.techtown.veganproject.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;


public class dinnerFragment extends Fragment implements View.OnClickListener {

    View root;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    private String absoultePath;
    String fileName;   //  fileName - 돌고 도는 선택된 날짜의 파일 이름
    String path_photo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/diary_image";
    String imageFileName;
    int flag = 0;
    File directory = new File(path_photo);
    File[] files = directory.listFiles();



    int cYear;
    int cMonth;
    int cDay;


    int year;
    int monthOfYear;
    int dayOfMonth;



    DatePicker datePicker;  //  datePicker - 날짜를 선택하는 달력
    TextView viewDatePick;  //  viewDatePick - 선택한 날짜를 보여주는 textView
    EditText edtDiary;   //  edtDiary - 선택한 날짜의 일기를 쓰거나 기존에 저장된 일기가 있다면 보여주고 수정하는 영역
    Button btnSave;   //  btnSave - 선택한 날짜의 일기 저장 및 수정(덮어쓰기) 버튼


//    private DB_Manger dbmanger;


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_blackfast);
        root = inflater.inflate(R.layout.fragment_dinner, container, false);

        Bundle args = getArguments();
        if (args!=null) {
            cYear = getArguments().getInt("year"); // 전달한 key 값
            cMonth = getArguments().getInt("month");
            cDay = getArguments().getInt("day");

            // 전달한 key 값
            year = getArguments().getInt("s_year");
            monthOfYear = getArguments().getInt("s_month");
            dayOfMonth = getArguments().getInt("s_day");
            Log.d("WORKING HERE", "onCreateView: ");
        }

        Intent intent =getActivity().getIntent();
        int cYear = intent.getExtras().getInt("year");
        int cMonth = intent.getExtras().getInt("month");
        int cDay = intent.getExtras().getInt("day");

        int year = intent.getExtras().getInt("s_year");
        int monthOfYear = intent.getExtras().getInt("s_month");
        monthOfYear=monthOfYear+1;
        int dayOfMonth = intent.getExtras().getInt("s_day");

        datePicker = (DatePicker) root.findViewById(R.id.datePicker);
        viewDatePick = (TextView) root.findViewById(R.id.diary_date);
        edtDiary = (EditText) root.findViewById(R.id.diary_view_content);
        btnSave = (Button) root.findViewById(R.id.diary_save_btn);
        //   dbmanger = new DB_Manger();
        iv_UserPhoto = (ImageView) this.root.findViewById(R.id.user_image);


        checkDangerousPermissions();



        Button btnUpload = (Button) root.findViewById(R.id.btn_UploadPicture);
        btnUpload.setOnClickListener((View.OnClickListener)this);

        //checkedDay(cYear, cMonth, cDay);

        checkedDay(year, monthOfYear, dayOfMonth);

        if(flag == 1)
            setImage(imageFileName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiary(fileName);
            }


        });
        return root;
    }

    //권한 위임 받기
    private void checkDangerousPermissions () {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(getContext(), permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                Toast.makeText(getContext(), "권한 설명 필요함", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }
    }


    // 일기의 유무를 확인 후 저장된 일기를 불러오고 수정 혹은 작성시 파일 생성
    public void checkedDay(int year, int monthOfYear, int dayOfMonth) {

        // 이미지 파일 이름 ( foodiary시간_ )
        String timeStamp = year + "" + monthOfYear + "" + dayOfMonth ;
        imageFileName = "diary_image" + timeStamp+"dinner_";

        // 받은 날짜로 날짜 보여주는
        viewDatePick.setText(year + " - " + monthOfYear + " - " + dayOfMonth);

        // 파일 이름을 만들어준다. 파일 이름은 "20170318.txt" 이런식으로 나옴
        fileName = year + "" + monthOfYear + "" + dayOfMonth + "dinner.txt";

        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput(fileName);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            String str = new String(fileData, "UTF-8");
            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(getContext(), "일기 써둔 날", Toast.LENGTH_SHORT).show();
            edtDiary.setText(str);
            btnSave.setText("수정하기");
            flag = 1;
        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(getContext(), "일기 없는 날", Toast.LENGTH_SHORT).show();
            flag = 0;
            edtDiary.setText("");
            btnSave.setText("새 일기 저장");
            e.printStackTrace();
        }

    }


    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기

    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        // 임시로 사용할 파일의 경로를 생성

        String url = "tmp_" + year + "" + monthOfYear + "" + dayOfMonth  + ".jpg";
        //  Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
        mImageCaptureUri = Uri.fromFile(new File(Environment.getDataDirectory(), url));


        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기

    {

        // 앨범 호출

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, PICK_FROM_ALBUM);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK)

            return;


        switch (requestCode) {

            case PICK_FROM_ALBUM: {

                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.

                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();

                Log.d("volley", mImageCaptureUri.getPath().toString());

            }


            case PICK_FROM_CAMERA: {

                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.

                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");

                intent.setDataAndType(mImageCaptureUri, "image/*");


                // CROP할 이미지를 200*200 크기로 저장

                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기

                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기

                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율

                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율

                intent.putExtra("scale", true);

                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동

                break;

            }

            case CROP_FROM_iMAGE: {

                // 크롭이 된 이후의 이미지를 넘겨 받습니다.

                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에

                // 임시 파일을 삭제합니다.

                if (resultCode != RESULT_OK) {

                    return;

                }


                final Bundle extras = data.getExtras();


                // CROP된 이미지를 저장하기 위한 FILE 경로

                String filePath = Environment.getDataDirectory() +

                        "/diary_image/" + year + "" + monthOfYear + "" + dayOfMonth + "_" +".jpg";




                if (extras != null) {

                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP

                    iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌


                    storeCropImage(photo, filePath, imageFileName); // CROP된 이미지를 외부저장소, 앨범에 저장한다.

                    absoultePath = filePath;
                    //Toast.makeText(getContext(), absoultePath, Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "파일경로"+filePath, Toast.LENGTH_SHORT).show();

                    break;


                }

                // 임시 파일 삭제

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) {

                    f.delete();

                }

            }

        }


    }


    //  @Override
    public void onClick(View v) {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                doTakePhotoAction();

            }

        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                doTakeAlbumAction();

            }

        };


        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {



                dialog.dismiss();

            }


        };
        new AlertDialog.Builder(getContext())

                .setTitle("업로드할 이미지 선택")

                .setPositiveButton("사진촬영", cameraListener)

                .setNeutralButton("앨범선택", albumListener)

                .setNegativeButton("취소", cancelListener)

                .show();



    }

    //사진 화면에 업로드 하는 함수
    private void storeCropImage(Bitmap bitmap, String filePath, String FileName) {

        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.

        File directory_image = new File(path_photo);

        //Toast.makeText(getContext(), "디렉토리 경로"+path_photo, Toast.LENGTH_SHORT).show();
        if (!directory_image.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            // Toast.makeText(getContext(), "해당 디렉토리가 없음", Toast.LENGTH_SHORT).show();
            directory_image.mkdirs();
        if (directory_image.exists()){
            //Toast.makeText(getContext(),"디렉토리 생성", Toast.LENGTH_SHORT).show();
        }


        File copyFile = new File(filePath);

        BufferedOutputStream out = null;


        try {


            copyFile=createImageFile(FileName);

            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,

                    Uri.fromFile(copyFile)));



            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    //다이어리 저장
    @SuppressLint("WrongConstant")
    private void saveDiary(String readDay) {

        FileOutputStream fos = null;

        try {
            fos = getActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE
            String content = edtDiary.getText().toString();

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos.write(content.getBytes());
            //fos.flush();
            fos.close();



            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(getContext(), "일기 저장됨", Toast.LENGTH_SHORT).show();

        } catch (Exception e) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace();
            Toast.makeText(getContext(), "오류오류", Toast.LENGTH_SHORT).show();
        }
    }

    //사진 파일을 생성하는 함수
    public File createImageFile(String FileName) throws IOException {

        // 이미지가 저장될 파일 이름 ( foodiary )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/diary_image/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(FileName, ".jpg", storageDir);

        Log.d("경로", "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    //사진 파일을 경로에 따라 불러와서 띄우는 함수
    private void setImage(String Name) {
        try {
            /*
             * for문 파일 리스트 크기만큼 돌려서
             * 사진을 뷰페이저에 넣는거 만들기
             * 리스틑 전역변수 지정한 거로 쓰기*/

            String fileRealName = findFile(Name);
            String path = path_photo + "/" + fileRealName;
            Log.d("URI 경로: ", path);
            Uri uri = getUriFromPath(path);
            InputStream in = getActivity().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            iv_UserPhoto.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    //Name= 내가 찾으려는 파일 이름
    public String findFile(String Name){
        /*
         * 1. File 리스트를 생성 --> 전역함수로 만들어라 그래야 위에서도 쓴다
         * 2. for문을 돌려서 사진의 갯수만큼 리스트에 추가
         *
         */
        List<String> filesNameList = new ArrayList<>();
        String EndPic = null;

        for (int i=0; i< files.length; i++) {
            if(files[i].getName().contains(Name)) {
                EndPic = files[i].getName();
            }
        }

        return EndPic;
    }


    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }


}
