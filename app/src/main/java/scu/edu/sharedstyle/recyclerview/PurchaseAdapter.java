package scu.edu.sharedstyle.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.activities.ProductDetailActivity;
import scu.edu.sharedstyle.activities.purchased_details;
import scu.edu.sharedstyle.model.GlideApp;
import scu.edu.sharedstyle.model.Item;

public class PurchaseAdapter extends FirestoreRecyclerAdapter<Item, PurchaseAdapter.ItemHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private StorageReference mstorageReference;
    private Context mContext;
    public boolean isClickable = true;
    public PurchaseAdapter(@NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ItemHolder holder, final int position, @NonNull final Item item) {
        holder.item_name.setText(item.getItemName());
        holder.item_brand.setText(item.getBrand());
        holder.item_price.setText("$"+item.getPrice());
        mstorageReference= FirebaseStorage.getInstance().getReferenceFromUrl(item.getImg_url());
        GlideApp.with(mContext).load(mstorageReference).into(holder.item_image);
        holder.imgURLs=item.getImgURLs();



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClickable){
                    return;
                }
                Intent intent=new Intent(mContext, purchased_details.class);
                Bundle bundle=new Bundle();
                final DocumentReference docRef= getSnapshots().getSnapshot(position).getReference();

                bundle.putString("itemPath",docRef.getPath());
                bundle.putStringArrayList("imgURLs",holder.imgURLs);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_grid_recycleview_item, parent, false);
        mContext=parent.getContext();
        return new ItemHolder(view);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView item_image;
        private TextView item_name;
        private TextView item_brand;
        private TextView item_price;
        private ArrayList<String> imgURLs;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_brand = itemView.findViewById(R.id.item_brand);
            item_price = itemView.findViewById(R.id.item_price);
        }
    }
}

