package com.example.onlinestoreapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemsViewHolder> {
    private Context mCtx;
    private List<OrderItems> orderList;
    public OrderAdapter(Context mCtx, List<OrderItems> orderList)
    {
        this.mCtx=mCtx;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public OrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.order_layout,null);
        return new OrderItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsViewHolder holder, int position) {
            OrderItems orderItems=orderList.get(position);
            holder.textView.setText(orderItems.getName());
            holder.textView1.setText("Order Number: #"+orderItems.getOrder_no());
        Glide.with(mCtx)
                .load(orderItems.getImageURl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    class OrderItemsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,textView1;
        public OrderItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.product_image_order);
            textView=itemView.findViewById(R.id.order_title);
            textView1=itemView.findViewById(R.id.order_no);
        }
    }
}
