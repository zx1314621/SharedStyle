package scu.edu.sharedstyle.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.activities.Front_page;
import scu.edu.sharedstyle.activities.LogIn;

public class ProfileFragment extends Fragment {


    private Button resetPW;
    private Button logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frofile, container, false);

        resetPW = view.findViewById(R.id.bt_resetPW);
        resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Reset Password click", Toast.LENGTH_SHORT).show();
            }
        });

        logout = view.findViewById(R.id.bt_Logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Front_page.class);
                Toast.makeText(getActivity(), "Log out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        return view;
    }
}