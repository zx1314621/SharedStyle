package scu.edu.sharedstyle.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import scu.edu.sharedstyle.activities.MainActivity;
import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.Item;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //For firestore test
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference productRef = firestore.collection("products").document();
        CollectionReference productCollect = firestore.collection("products");
        productCollect.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        System.out.println(document.getId() + " => " + document.getData());
                        Item item=document.toObject(Item.class);
                        ItemList.add(item);
                        System.out.println(ItemList.isEmpty());
                        System.out.println(ItemList);
                    }
                } else {
                    System.out.println( "Error getting documents: ");
                }
            }
        });

        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        mGridRv = view.findViewById(R.id.RV_grid_id);

        mGridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));



        mGridRv.setAdapter(new GridRecyclerViewAdapter(getActivity(), ItemList));

        //search = view.findViewById(R.id.search);
        //editText = view.findViewById(R.id.et_search);
        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "This is search:" + editText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment*/
        return view;
    }

    private ArrayList<Item> getData() {

        List<Item> itemList = new ArrayList<>();
        Item item1 = new Item();
        item1.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item1.setItemName("item1");
        item1.setBrand("gucci");
        item1.setPrice(1000);
        item1.setItemDesc("This is Item1");
        itemList.add(item1);

        Item item2 = new Item();
        item2.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item2.setItemName("item2");
        item2.setBrand("lv");
        item2.setPrice(2000);
        item2.setItemDesc("This is item2");
        itemList.add(item2);

        Item item3 = new Item();
        item3.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item3.setItemName("item3");
        item3.setBrand("YSL");
        item3.setPrice(600);
        item3.setItemDesc("This is item3");
        itemList.add(item3);


        Item item4 = new Item();
        item4.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item4.setItemName("item4");
        item4.setBrand("gucci");
        item4.setPrice(1000);
        item4.setItemDesc("This is item4");
        itemList.add(item4);

        Item item5 = new Item();
        item5.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item5.setItemName("item5");
        item5.setBrand("lv");
        item5.setPrice(2000);
        item5.setItemDesc("This is item5");
        itemList.add(item5);

        Item item6 = new Item();
        item6.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item6.setItemName("item6");
        item6.setBrand("YSL");
        item6.setPrice(600);
        item6.setItemDesc("This is item6");
        itemList.add(item6);

        Item item7 = new Item();
        item7.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item7.setItemName("item7");
        item7.setBrand("gucci");
        item7.setPrice(1000);
        item7.setItemDesc("This is item7");
        itemList.add(item7);

        Item item8 = new Item();
        item8.setImg_url("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
        item8.setItemName("item8");
        item8.setBrand("gucci");
        item8.setPrice(1000);
        item8.setItemDesc("This is item8");
        itemList.add(item8);

        return (ArrayList<Item>) itemList;

    }
}