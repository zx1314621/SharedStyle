package scu.edu.sharedstyle.recyclerview;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.Item;

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Item> itemList;

    public GridRecyclerViewAdapter(Context context, ArrayList<Item> ar) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.itemList = ar;
    }

    @Override
    public GridRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.layout_grid_recycleview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(GridRecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.item_name.setText(itemList.get(position).getItemName());
        holder.item_brand.setText(itemList.get(position).getBrand());
        holder.item_price.setText(itemList.get(position).getPrice() + "$");
        holder.item_image.setImageResource(itemList.get(position).getImg_url());
        //Glide.with(mContext).load("http://image.bitautoimg.com/appimage/media/20170317/w1600_h900_f6a5cad43dda443184bc2a6d5ecf44b4.jpeg").into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_image;
        private TextView item_name;
        private TextView item_brand;
        private TextView item_price;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_brand = itemView.findViewById(R.id.item_brand);
            item_price = itemView.findViewById(R.id.item_price);
        }
    }

}
