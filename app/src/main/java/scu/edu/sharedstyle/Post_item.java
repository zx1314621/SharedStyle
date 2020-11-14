package scu.edu.sharedstyle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Post_item extends AppCompatActivity {
    Button cancel;
    Button post;
    final int IMAGE_OPEN = 1;        //打开图片标记
    final int CAMERA = 2;
    Bitmap bmp;                      //导入临时图片
    ArrayList<HashMap<String, Object>> images;
    SimpleAdapter simpleAdapter = null;     //适配器
    GridView gridView;
    private int windowheight;
    private int windowwidth;
    private TextView textView;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        post = findViewById(R.id.post);
        cancel = findViewById(R.id.cancel);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager wm=(WindowManager) getSystemService(WINDOW_SERVICE);
        windowheight=wm.getDefaultDisplay().getHeight();
        windowwidth=wm.getDefaultDisplay().getWidth();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
//        //锁定屏幕
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(R.layout.activity_post_item);
        //获取控件对象
        gridView = (GridView) findViewById(R.id.grid_view);
        textView = findViewById(R.id.count);
        description = findViewById(R.id.description);
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                textView.setText(String.valueOf(s.length()) + "/120");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        };
        description.addTextChangedListener(mTextEditorWatcher);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.item1);
        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.item2);
        Bitmap bmp3 = BitmapFactory.decodeResource(getResources(), R.drawable.item3);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.imageadd);
        images= new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        HashMap<String, Object> map3 = new HashMap<String, Object>();

        map1.put("itemImage", bmp1);
        map2.put("itemImage", bmp2);
        map3.put("itemImage", bmp3);
        map.put("itemImage", bmp);
        images.add(map);
        images.add(map1);
        images.add(map2);
        images.add(map3);

        simpleAdapter = new SimpleAdapter(this,
                images, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String s) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if( images.size() == 7) { //第一张为默认图片
                    Toast.makeText(Post_item.this, "The number of photos is up to seven", Toast.LENGTH_SHORT).show();
                }
                if(position == 0){
                    Toast.makeText(Post_item.this, "Add a photo", Toast.LENGTH_SHORT).show();
                    //选择图片
//                    Intent intent = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, IMAGE_OPEN);

                    final String [] strs=new String[]{"Take photo","Album"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(Post_item.this);
                    builder.setTitle("Photots");
                    builder.setItems(strs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //拍照可以用两种方法来实现
                            //1.调用系统相机 2.自定义相机
                            if (which==0) {

                                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,CAMERA);

                            }
                            //调用系统相册
                            if (which==1) {
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, IMAGE_OPEN);
                            }

                        }
                    });
                    builder.show();
                }else{
                    dialog(position);
                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Post_item.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(Post_item.this, "post successfullt", Toast.LENGTH_SHORT).show();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Post_item.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String pathImage;
        //Open the image
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {

            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //Select the image
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //return
                if (null == cursor) {
                    return;
                }
                //cursor move to the first and get the path
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                int angle = readPictureDegree(pathImage);
                Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
                Bitmap bitmap = rotaingImageView(angle, addbmp);

                Log.e("path", pathImage);
                HashMap<String, Object> new_map = new HashMap<String, Object>();
                new_map.put("itemImage", bitmap);
                images.add(new_map);

                simpleAdapter = new SimpleAdapter(this,
                        images, R.layout.griditem_addpic,
                        new String[]{"itemImage"}, new int[]{R.id.imageView1});
                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        // TODO Auto-generated method stub
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView imageView = (ImageView) view;
                            imageView.setImageBitmap((Bitmap) data);
                            return true;
                        }
                        return false;
                    }
                });
                //gridView = (GridView) findViewById(R.id.grid_view);
                gridView.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        }  //end if
        else if (resultCode == RESULT_OK && requestCode == CAMERA) {

            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Log.i("TestFile", "SD card is not avaiable right now");
                return;
            }
            String name = new DateFormat().format("yyMMdd",System.currentTimeMillis())+/*"_"+(Math.random()*1)+*/".png";
            String path = "/sdcard/myp_w_picpath/";
            Log.e("name", name);
            Bundle bundle = data.getExtras();
            FileOutputStream b = null;
            // 实现设置图片的大小，然后显示
            Intent intent1 = new Intent("com.android.camera.actioin.CROP");
            intent1.putExtra("crop", "true");
            intent1.putExtra("outputX", 250);
            intent1.putExtra("outputY", 250);
            intent1.putExtra("aspectX", 1);
            intent1.putExtra("aspectY", 1);
            Bitmap source = (Bitmap) bundle.get("data");
            File file = new File(path);
            file.mkdir();
            String Filename = path + File.separator + name;
            Matrix m = new Matrix();
            m.setRotate(90,(float) source.getWidth() / 2, (float) source.getHeight() / 2);
            final Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
            HashMap<String, Object> new_map = new HashMap<String, Object>();
            new_map.put("itemImage", bitmap);
            images.add(new_map);
            try {
                b = new FileOutputStream(Filename);
                source.compress(Bitmap.CompressFormat.JPEG, 100, b);
                b.flush();
                b.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            HashMap<String, Object> new_map = new HashMap<String, Object>();
//            new_map.put("itemImage", bitmap);
//            images.add(new_map);

            simpleAdapter = new SimpleAdapter(this,
                    images, R.layout.griditem_addpic,
                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            //gridView = (GridView) findViewById(R.id.grid_view);
            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Post_item.this);
        builder.setMessage("Are you sure you want to delete the picture you choose");
        builder.setTitle("Alert");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                images.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.e("orientation", String.valueOf(orientation));
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


}