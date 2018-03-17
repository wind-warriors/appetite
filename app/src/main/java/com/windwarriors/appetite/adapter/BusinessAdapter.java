package com.windwarriors.appetite.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.windwarriors.appetite.BusinessDetailsActivity;
import com.windwarriors.appetite.R;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;
import com.windwarriors.appetite.utils.DownloadImageTask;
import com.windwarriors.appetite.utils.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.windwarriors.appetite.utils.Constants.BUSINESS_ID;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    private ArrayList<Business> businessList;
    private static Context context;

    static class BusinessViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView businessName;
        TextView totalReviews;
        TextView distance;
        TextView foodCategory;
        TextView address;
//        TextView isClosed;
        TextView rating;

        BusinessViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image);
            businessName = itemView.findViewById(R.id.business_name);
            totalReviews = itemView.findViewById(R.id.total_reviews);
            distance = itemView.findViewById(R.id.distance);
            foodCategory = itemView.findViewById(R.id.food_category);
            address = itemView.findViewById(R.id.address);
//            isClosed = itemView.findViewById(R.id.isClosed);
            rating = itemView.findViewById(R.id.isClosed);

        }

    }

    public BusinessAdapter(Context context, ArrayList<Business> businessList) {
        this.businessList = businessList;
        this.context = context;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_item, parent, false);
        return new BusinessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, final int position) {
        final Business currentBusiness = businessList.get(position);


        new DownloadImageTask(holder.foodImage).execute(currentBusiness.getImageLink());

        holder.businessName.setText(currentBusiness.getName());
        holder.totalReviews.setText(currentBusiness.getTotalReviews());
        holder.distance.setText(currentBusiness.getDistance());
        holder.foodCategory.setText((currentBusiness.getFoodCategory()));
        holder.address.setText(currentBusiness.getAddress());
        holder.rating.setText(Double.toString(currentBusiness.getRating()));

//        if (currentBusiness.getIsClosed() == true){
//            holder.isClosed.setText(Constants.CLOSED);
//            holder.isClosed.setTextColor(Color.RED);
//
//        }
//        else if(currentBusiness.getIsClosed() == false) {
//            holder.isClosed.setText(Constants.OPEN);
//            holder.isClosed.setTextColor(Color.GREEN);
//        }
//        else {
//            holder.isClosed.setText("");
//        }
//
//

        holder.foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Toast.makeText(context, businessList.get(position).getName(), Toast.LENGTH_SHORT).show();

            Intent next = new Intent( context, BusinessDetailsActivity.class);
            next.putExtra(BUSINESS_ID, businessList.get(position).getId());
            context.startActivity(next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

}
