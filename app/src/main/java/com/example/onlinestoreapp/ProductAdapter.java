package com.example.onlinestoreapp;

import android.content.Context;
import android.content.Intent;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mCtx;
    private List<Products> productsList;

    public ProductAdapter(Context mCtx, List<Products> productsList) {
        this.mCtx = mCtx;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.list_layout,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Products product=productsList.get(position);
            holder.title.setText(product.getTitle());
            holder.description.setText(product.getDescription());
            holder.price.setText(String.format("₹%s", product.getPrice()));
            Glide.with(mCtx)
                    .load(product.getImage())
                    .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mCtx,Individual_Product.class);
                    intent.putExtra("key",product.getKey());
                    intent.putExtra("category",product.getCategory());
                    intent.putExtra("name",product.getTitle());
                    intent.putExtra("long_description",product.getLong_description());
                    intent.putExtra("imageURL",product.getImage());
                    intent.putExtra("Quantity",product.getQuantity());
                    intent.putExtra("Price",product.getPrice());
                    mCtx.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
    public void filterList(ArrayList<Products> filteredList)
    {
        productsList=filteredList;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView .ViewHolder{
        ImageView imageView;
        TextView title,description,price;
        CardView cardView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cv);
            imageView=itemView.findViewById(R.id.product_image);
            title=itemView.findViewById(R.id.title_product);
            description=itemView.findViewById(R.id.Description);
            price=itemView.findViewById(R.id.Price);

        }
    }
}
