package com.example.onlinestoreapp;

import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> implements Filterable {
    private Context mCtx;
    private List<Categories> categoriesList;

    public CategoriesAdapter(Context mCtx, List<Categories> categoriesList) {
        this.mCtx = mCtx;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.category_layout,null);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
            Categories categories=categoriesList.get(position);
            holder.textView.setText(categories.getCategory());
            holder.setIsRecyclable(false);
            Glide.with(mCtx)
                .load(categories.getImageURL())
                .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mCtx, Products_Details.class);
                    intent.putExtra("category",categories.getCategory());
                    mCtx.startActivity(intent);
                }

            });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class CategoriesViewHolder extends RecyclerView .ViewHolder{
        TextView textView;
        ImageView imageView;
        CardView cardView;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cv_category);
            textView=itemView.findViewById(R.id.category_text);
            imageView=itemView.findViewById(R.id.category_image);
        }
    }
}