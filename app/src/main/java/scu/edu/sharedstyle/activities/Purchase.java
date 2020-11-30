package scu.edu.sharedstyle.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.Item;

public class Purchase extends AppCompatActivity {
    TextView tv_ProductName;
    TextView tv_Price;
    EditText cardholder;
    EditText cardnum;
    EditText expdate;
    EditText cvs;
    private double price;
    private String name;
    private String itemPath;
    private String HolderName;
    private String CardNumber;
    private String ExpirationDate;
    private String CVS;
    private String TAG = "Purchase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_page);
        Bundle bundle =getIntent().getExtras();
        tv_ProductName = findViewById(R.id.tv_name);
        tv_Price = findViewById(R.id.tv_pricex);
        name = bundle.getString("name");
        price = bundle.getDouble("price");
        itemPath=bundle.getString("itemPath");
        tv_ProductName.setText(name);
        tv_Price.setText(String.valueOf(price));

        cardholder = findViewById(R.id.et_holdername);
        cardnum = findViewById(R.id.et_cardnum);
        expdate = findViewById(R.id.et_expdate);
        cvs = findViewById(R.id.et_cvs);

    }

    public void alert(View view) {
        if(!isValid()){
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Purchase Completed");
            alert.setCancelable(false);
            alert.setMessage("Thank you for your purchase!");
            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gotomain();
                }
            });
            alert.show();
        }
    }

    public Boolean isValid(){
        HolderName = cardholder.getText().toString();
        CardNumber = cardnum.getText().toString();
        ExpirationDate = expdate.getText().toString();
        CVS = cvs.getText().toString();
        if(HolderName.matches("")|| CardNumber.matches("")
                || ExpirationDate.matches("") || CVS.matches("")){
            Toast.makeText(this, "Credit card information cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        } else try{
            Integer i = Integer.parseInt(CVS);
            if(CVS.length() < 3 || CVS.length() > 4){
                Toast.makeText(this, "Please enter valid CVS number.", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NumberFormatException e){
            Toast.makeText(this, "Please enter valid CVS number.", Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            Integer.parseInt(CardNumber);

        } catch (NumberFormatException er){
            Toast.makeText(this, "Please enter valid card number", Toast.LENGTH_LONG).show();
            return false;
        }

        return checkdate(ExpirationDate);

    }

    private Boolean checkdate(String ExpirationDate) {
        if (ExpirationDate.length() != 5){
            Toast.makeText(this, "Please enter valid expiration date", Toast.LENGTH_LONG).show();
            return false;
        } else if(!ExpirationDate.contains("/")){
            Toast.makeText(this, "Please enter valid expiration date", Toast.LENGTH_LONG).show();
            return false;
        } else try {
            Integer month = Integer.parseInt(ExpirationDate.substring(0,2));
            Integer year = Integer.parseInt(ExpirationDate.substring(3));
            if (month < 1 || month > 12){
                Toast.makeText(this, "Please enter valid expiration date", Toast.LENGTH_LONG).show();
                return false;
            }
            if (year < 21){
                Toast.makeText(this, "Please enter valid expiration date", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NumberFormatException e){
            Toast.makeText(this, "Please enter valid expiration date, not number", Toast.LENGTH_LONG).show();
            return false; }

        return true;

    }

    private void gotomain() {

        final DocumentReference productRef= FirebaseFirestore.getInstance().document(itemPath);
        final DocumentReference purchasedRef=FirebaseFirestore.getInstance().collection("sold").document();
        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Item item=document.toObject(Item.class);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        item.setUserID(user.getUid());
                        productRef.delete();
                        purchasedRef.set(item);
                    }

                }
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
//        Intent intent = new Intent(this, ProductDetailActivity.class);
//        startActivity(intent);
        finish();
    }
}
