package scu.edu.sharedstyle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.Continuation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.UUID;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.Item;

public class Post_item extends AppCompatActivity {
    Button cancel;
    Button post;
    final int IMAGE_OPEN = 1;
    final int CAMERA = 2;
    Bitmap bmp;
    ArrayList<HashMap<String, Object>> images;
    SimpleAdapter simpleAdapter = null;
    GridView gridView;
    private TextView textView;
    private EditText description;

    //For firestore test
    private EditText name;
    private EditText brand;
    private EditText price;
    private FirebaseFirestore firestore;
    private DocumentReference productRef;
    private FirebaseStorage storage;
    private StorageReference itemImgRef;
    private String new_item = "A big bargain starts at:";
    String User_id;
    List<String> list;
    int size = 0;
    FirebaseAuth mAuth;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        post = findViewById(R.id.post);
        cancel = findViewById(R.id.cancel);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager wm=(WindowManager) getSystemService(WINDOW_SERVICE);


//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(R.layout.activity_post_item);

        gridView = (GridView) findViewById(R.id.grid_view);
        textView = findViewById(R.id.count);
        description = findViewById(R.id.description);
        //For firestore test
        name = findViewById(R.id.input_name);
        brand = findViewById(R.id.input_brand);
        price = findViewById(R.id.input_price);
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        itemImgRef=storage.getReference().child("Item/");
        mAuth = FirebaseAuth.getInstance();

        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                textView.setText(String.valueOf(s.length()) + "/150");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        };
        description.addTextChangedListener(mTextEditorWatcher);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.imageadd);
        images= new ArrayList<HashMap<String, Object>>();
        final HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("itemImage", bmp);
        images.add(map);

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
                if(position == 0){
                        //Toast.makeText(Post_item.this, "Add a photo", Toast.LENGTH_SHORT).show();


                    final String [] strs=new String[]{"Take a photo","Album"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(Post_item.this);
                    builder.setTitle("Pictures");
                    builder.setItems(strs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(images.size() < 6) {
                                if (which == 0) {

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, CAMERA);

                                }

                                if (which == 1) {
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                        if (ActivityCompat.checkSelfPermission(Post_item.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(Post_item.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                                        } else {
                                            Intent intent = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(intent, IMAGE_OPEN);
                                        }
                                    }
                                }
                            }else {
                                    Toast.makeText(Post_item.this, "Please upload no more than five pictures.", Toast.LENGTH_SHORT).show();

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
                //For firestore test
                if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(brand.getText())) {
                    Toast.makeText(Post_item.this, "Please fill out all information about the product.", Toast.LENGTH_SHORT).show();
                } else {
                    if (isInteger(price.getText().toString()) == false) {
                        Toast.makeText(Post_item.this, "Please enter a valid number for the price.", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (images.size()==1) {
                            System.out.println("imgURLs is empty"+images.isEmpty());
                            Toast.makeText(Post_item.this, "Please select at least one picture.", Toast.LENGTH_SHORT).show();
                        } else {
                            images.remove(0); //Remove imageAdd
                            ArrayList<String> keywords = generateKeyWord(name.getText().toString());
                            ArrayList<String> imgURLs = upLoadImg(images);
                            Item postItem = new Item(name.getText().toString(), description.getText().toString(),
                                    brand.getText().toString(), Double.parseDouble(price.getText().toString()),
                                    imgURLs.get(0), imgURLs, user.getUid(), keywords);

                            productRef = firestore.collection("products").document();
                            productRef.set(postItem);
                            //User_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

//                firestore.collection("Users").document(User_id).collection()
                            String from = mAuth.getCurrentUser().getUid();
                            final Map<String, Object> map = new HashMap<>();
                            map.put("Itemname", name.getText().toString());
                            map.put("Image", imgURLs.get(0));
                            map.put("from", from);
                            map.put("message", new_item);
                            Log.d("tag", String.valueOf(map.keySet()));
                            list = new ArrayList<String>();
                            firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            size++;
                                            list.add(document.getId());
                                        }

                                        Log.d("tag size", String.valueOf(size));
                                        for (int i = 0; i < size; i++) {
                                            String User_id = list.get(i);
                                            firestore.collection("Users/" + User_id + "/Notifications").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("tag", "add work");
                                                    // Toast.makeText(Post_item.this, "send notification", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                            Intent intent = new Intent(Post_item.this, MainActivity.class);
                            startActivity(intent);
                            //Toast.makeText(Post_item.this, "post successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Post_item.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if(grantResults[i] == -1) {
                    Toast.makeText(Post_item.this, "Please give us permission to access photo album.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_OPEN);

    }


    private ArrayList<String> generateKeyWord(String input) {
        Set<String> keywords = new HashSet<>();
        String name = input.toLowerCase();

        for (int i = 0; i < name.length(); i++) {
            for (int j = i + 1; j <= name.length(); j++) {
                String sub = name.substring(i,j);
                if (!sub.isEmpty())
                    keywords.add(sub);
            }
        }

        return new ArrayList<>(keywords);

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
            //m.setRotate(90,(float) source.getWidth() / 2, (float) source.getHeight() / 2);
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
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
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

    //For firestore test
    private ArrayList<String> upLoadImg(ArrayList<HashMap<String,Object>> imgMap){
        ArrayList<String> imgURLs=new ArrayList<>();
        for(final HashMap<String,Object> img : imgMap){
            for(final String i:img.keySet()){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = (Bitmap) img.get(i);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] data = baos.toByteArray();
                final StorageReference uploadRef=itemImgRef.child(UUID.randomUUID().toString()+".jpeg");
                UploadTask uploadTask = uploadRef.putBytes(data);
                imgURLs.add(uploadRef.toString());
            }
        }

        return imgURLs;
    }
    public static boolean isInteger(String value){
        try {
            Integer.parseInt(value);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }




}