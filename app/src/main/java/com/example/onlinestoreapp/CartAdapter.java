package com.example.onlinestoreapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.inappmessaging.model.Button;

import java.util.List;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemsViewHolder>{
    private Context mCtx;
    private List<CartItems> cartList;
    String quantity_available="1";
    AlertDialog.Builder builder;
    public CartAdapter(Context mCtx, List<CartItems> cartList) {
        this.mCtx = mCtx;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.cart_layout,null);
        return new CartItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        CartItems cartItems=cartList.get(position);
        holder.textView.setText(cartItems.getTitle());
        holder.textView1.setText(String.format("₹%s", cartItems.getPrice()));
        holder.textView2.setText(cartItems.getQuantity());
        holder.button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(cartItems.getQuantity())==1)
                {
                    builder = new AlertDialog.Builder(mCtx);
                    builder.setMessage("Are you sure you want to remove this item from the cart?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Individual_Product function=new Individual_Product();
                                    Cart cart=new Cart();
                                    cart.cost=0;
                                    cart.gst=0;
                                    cart.total=0;
                                    cart.total_cost=0;
                                    function.add_to_cart_function(1,2,cartItems.getKey());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Remove from Cart?");
                    alert.show();
                    //Delete the item from cart
                }
                else
                {
                    int quantity=Integer.parseInt(cartItems.getQuantity())-1;
                    Log.d("test",String.valueOf(quantity));
                    holder.textView2.setText(String.valueOf(quantity));
                    //update number
                    Individual_Product function=new Individual_Product();
                    Cart cart=new Cart();
                    cart.cost=0;
                    cart.gst=0;
                    cart.total=0;
                    cart.total_cost=0;
                    function.add_to_cart_function(quantity,1,cartItems.getKey());
                }
            }
        });
        holder.button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_quantity_available(cartItems.getCategory(),cartItems.getKey());
                Log.d("Quantity",quantity_available);
                if(Integer.parseInt(quantity_available)>Integer.parseInt(cartItems.getQuantity()))
                {
                    int quantity=Integer.parseInt(cartItems.getQuantity())+1;
                    Log.d("test",String.valueOf(quantity));
                    holder.textView2.setText(String.valueOf(quantity));
                    //update number
                    Individual_Product function=new Individual_Product();
                    Cart cart=new Cart();
                    cart.cost=0;
                    cart.gst=0;
                    cart.total=0;
                    cart.total_cost=0;
                    function.add_to_cart_function(quantity,1,cartItems.getKey());
                }
            }
        });
        holder.setIsRecyclable(false);
        Glide.with(mCtx)
                .load(cartItems.getImageURL())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    static class CartItemsViewHolder extends RecyclerView .ViewHolder{
        TextView textView,textView1,textView2;
        ImageView imageView;
        CardView cardView;
        View button_minus,button_plus;
        public CartItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            button_minus=itemView.findViewById(R.id.button_minus);
            button_plus=itemView.findViewById(R.id.button_plus);
            textView2=itemView.findViewById(R.id.quantity_count);
            cardView=itemView.findViewById(R.id.cv_cart);
            textView=itemView.findViewById(R.id.title_product);
            textView1=itemView.findViewById(R.id.Price);
            imageView=itemView.findViewById(R.id.product_image);
        }
    }
    public void check_quantity_available(String category, String key)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products").child(category).child("Items").child(key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> value=(Map<String, String>)snapshot.getValue();
                quantity_available=value.get("Quantity");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}