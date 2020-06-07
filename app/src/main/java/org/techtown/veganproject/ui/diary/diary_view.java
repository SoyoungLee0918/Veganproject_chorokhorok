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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.veganproject.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;


public class diary_view extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;

    private ImageView iv_UserPhoto;

    private int id_view;

    private String absoultePath;

    DatePicker datePicker;  //  datePicker - 날짜를 선택하는 달력
    TextView viewDatePick;  //  viewDatePick - 선택한 날짜를 보여주는 textView
    EditText edtDiary;   //  edtDiary - 선택한 날짜의 일기를 쓰거나 기존에 저장된 일기가 있다면 보여주고 수정하는 영역
    Button btnSave;   //  btnSave - 선택한 날짜의 일기 저장 및 수정(덮어쓰기) 버튼

    String fileName;   //  fileName - 돌고 도는 선택된 날짜의 파일 이름


//    private DB_Manger dbmanger;


    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_view);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        viewDatePick = (TextView) findViewById(R.id.diary_date);
//        edtDiary = (EditText) findViewById(R.id.diary_view_content);
        btnSave = (Button) findViewById(R.id.diary_save_btn);
     //   dbmanger = new DB_Manger();
        iv_UserPhoto = (ImageView) this.findViewById(R.id.user_image);

        Button btn_agreeJoin = (Button) this.findViewById(R.id.btn_UploadPicture);
        btn_agreeJoin.setOnClickListener(this);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 이미 선택한 날짜에 일기가 있는지 없는지 체크해야할 시간이다
                checkedDay(year, monthOfYear, dayOfMonth);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveDiary(fileName);
            }


        });
    }

    public void checkedDay(int year, int monthOfYear, int dayOfMonth) {


        // 받은 날짜로 날짜 보여주는
        viewDatePick.setText(year + " - " + monthOfYear + " - " + dayOfMonth);

        // 파일 이름을 만들어준다. 파일 이름은 "20170318.txt" 이런식으로 나옴
        fileName = year + "" + monthOfYear + "" + dayOfMonth + ".txt";

        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
       FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            String str = new String(fileData, "EUC-KR");
            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(getApplicationContext(), "일기 써둔 날", Toast.LENGTH_SHORT).show();
            edtDiary.setText(str);
            btnSave.setText("수정하기");
        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(getApplicationContext(), "일기 없는 날", Toast.LENGTH_SHORT).show();
            edtDiary.setText("");
            btnSave.setText("새 일기 저장");
            e.printStackTrace();
        }

    }


    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기

    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        // 임시로 사용할 파일의 경로를 생성

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));


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

                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());

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

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +

                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";


                if (extras != null) {

                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP

                    iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌


                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.

                    absoultePath = filePath;

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


    @Override
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
        new AlertDialog.Builder(this)

                .setTitle("업로드할 이미지 선택")

                .setPositiveButton("사진촬영", cameraListener)

                .setNeutralButton("앨범선택", albumListener)

                .setNegativeButton("취소", cancelListener)

                .show();



    }

    private void storeCropImage(Bitmap bitmap, String filePath) {

        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";

        File directory_SmartWheel = new File(dirPath);


        if (!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)

            directory_SmartWheel.mkdir();


        File copyFile = new File(filePath);

        BufferedOutputStream out = null;


        try {


            copyFile.createNewFile();

            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,

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
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE
            String content = edtDiary.getText().toString();

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos.write(content.getBytes());
            //fos.flush();
            fos.close();

            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(getApplicationContext(), "일기 저장됨", Toast.LENGTH_SHORT).show();

        } catch (Exception e) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "오류오류", Toast.LENGTH_SHORT).show();
        }
    }



}
