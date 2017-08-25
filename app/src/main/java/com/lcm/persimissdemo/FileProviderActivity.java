package com.lcm.persimissdemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileProviderActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivPhoto;
    private Button btnTackPhoto;
    private Button btnPickPhoto;
    //    private static final int REQUEST_CODE_TACK_PHOTO = 0x11;
    public static final int REQUEST_FILE_PICKER = 0x01; //打开相册
    public static final int REQUEST_FILE_CHOOSER = 0x02; //调用相机


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_provider);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        btnTackPhoto = (Button) findViewById(R.id.btn_tack_photo);
        btnPickPhoto = (Button) findViewById(R.id.btn_pick_photo);


        btnTackPhoto.setOnClickListener(this);
        btnPickPhoto.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tack_photo:
                openCamera();
                break;

            case R.id.btn_pick_photo:
                chosePic();
                break;
        }
    }


    /**
     * 打开相机
     */
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Uri imageUri = null;
            try {
                imageUri = FileProvider7.getUriForFile(getApplicationContext(), createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_FILE_CHOOSER);
        }
    }

    /**
     * 打开相册
     */
    private void chosePic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                REQUEST_FILE_PICKER);
    }


    String mCurrentPhotoPath = null;
    String FileName = "test";

    //创建文件夹包装图片
    private File createImageFile() throws IOException {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + "/" + FileName);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + "/" + FileName + "/", System.currentTimeMillis() + ".png");
        //保存当前图片路径
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }




    /**
     * 拍照结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE_CHOOSER) {  //拍照
                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            } else if (requestCode == REQUEST_FILE_PICKER) { //相册
                String path = MediaUtility.getPath(this,
                        data.getData());
                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }
    }
}
