package scu.edu.sharedstyle.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.activities.AddressActivity;
import scu.edu.sharedstyle.activities.Front_page;
import scu.edu.sharedstyle.activities.LogIn;
import scu.edu.sharedstyle.activities.Posted;
import scu.edu.sharedstyle.activities.PurchasedItem;
import scu.edu.sharedstyle.activities.SettingsActivity;

public class ProfileFragment extends Fragment {


    private Button resetPW;
    private Button logout;
    private TableRow tr_posted;
    private TableRow tr_purchased;
    private TableRow tr_setting;
    private TextView tv_user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        //Go to setting
        tr_setting=view.findViewById(R.id.tr_setting);
        tr_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        //Set user name
        tv_user=view.findViewById(R.id.tv_user);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference userRef= FirebaseFirestore.getInstance().collection("users");
        userRef.whereEqualTo("uid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tv_user.setText(document.getString("name"));
                            }
                        }
                    }
                });


        //Go to posted
        tr_posted=view.findViewById(R.id.tr_posted);
        tr_posted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Posted.class);
                startActivity(intent);
            }
        });

        //Go to purchased
        tr_purchased=view.findViewById(R.id.tr_purchased);
        tr_purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PurchasedItem.class);
                startActivity(intent);
            }
        });




        return view;
    }
}