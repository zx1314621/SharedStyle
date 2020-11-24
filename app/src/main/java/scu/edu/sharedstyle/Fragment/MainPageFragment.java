package scu.edu.sharedstyle.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.List;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.activities.MainActivity;
import scu.edu.sharedstyle.model.Item;
import scu.edu.sharedstyle.recyclerview.FirestoreAdapter;
import scu.edu.sharedstyle.recyclerview.GridRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mGridRv;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText editText;
    private Button search;
    private ArrayList<Item> ItemList=new ArrayList<>();
    private FirestoreAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //For firestore test


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        mGridRv = view.findViewById(R.id.RV_grid_id);


        mGridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        getData();

        mGridRv.setAdapter(adapter);

//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                ItemList=(ArrayList<Item>)task.getResult().toObjects(Item.class);
//                System.out.println("Here is empty "+ItemList.isEmpty());
//                mGridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//
//
//                mGridRv.setAdapter(new GridRecyclerViewAdapter(getActivity(), getData()));
//            }
//        });

        search = view.findViewById(R.id.search);
        editText = view.findViewById(R.id.et_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "This is search:" + editText.getText(), Toast.LENGTH_SHORT).show();
                refreshData(editText.getText().toString());
                editText.setText("");

            }
        });
        // Inflate the layout for this fragment*/
        return view;
    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    private void getData() {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference productRef = firestore.collection("products").document();
        CollectionReference productCollect = firestore.collection("products");
        Query query=FirebaseFirestore.getInstance()
                .collection("products")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .limit(50);


        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter=new FirestoreAdapter(options);


    }
    private void refreshData(String pattern) {


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference productRef = firestore.collection("products").document();
        CollectionReference productCollect = firestore.collection("products");
        Query query=FirebaseFirestore.getInstance()
                .collection("products")
                .orderBy("itemName",Query.Direction.DESCENDING)
                .limit(50);

        if(pattern != null && !pattern.isEmpty()) {
            
            query = query.whereEqualTo("itemName", pattern);
        }

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter.updateOptions(options);
        adapter.notifyDataSetChanged();


    }
}