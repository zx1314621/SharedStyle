package scu.edu.sharedstyle.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import scu.edu.sharedstyle.R;

public class Purchase extends AppCompatActivity {
    TextView tv_ProductName;
    TextView tv_Price;
    EditText cardholder;
    EditText cardnum;
    EditText expdate;
    EditText cvs;
    private double price;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_page);
        Bundle bundle =getIntent().getExtras();
        tv_ProductName = findViewById(R.id.tv_name);
        tv_Price = findViewById(R.id.tv_pricex);
        name = bundle.getString("name");
        price = bundle.getDouble("price");
        tv_ProductName.setText(name);
        tv_Price.setText(String.valueOf(price));

        cardholder = findViewById(R.id.et_holdername);
        cardnum = findViewById(R.id.et_cardnum);
        expdate = findViewById(R.id.et_expdate);
        cvs = findViewById(R.id.et_cvs);

    }

    public void alert(View view) {
        if(cardholder.getText().toString().matches("")|| cardnum.getText().toString().matches("")
                || expdate.getText().toString().matches("") || cvs.getText().toString().matches("")){
            Toast.makeText(this, "Please enter valid credit card information", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);//this here just means the current object == Main Activity
            // this here is the same as getActivityContext -- very common way to get context
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

    private void gotomain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
//        Intent intent = new Intent(this, ProductDetailActivity.class);
//        startActivity(intent);
        finish();
    }
}
