package com.windwarriors.appetite.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windwarriors.appetite.BusinessDetailsActivity;
import com.windwarriors.appetite.R;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.DownloadImageTask;

import java.util.ArrayList;

import static com.windwarriors.appetite.utils.Constants.BUSINESS_DISTANCE;
import static com.windwarriors.appetite.utils.Constants.BUSINESS_ID;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    private ArrayList<Business> businessList;
    private static Context context;

    class BusinessViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView businessName;
        TextView totalReviews;
        TextView distance;
        TextView foodCategory;
        TextView address;
        ImageView ratingStar;

        BusinessViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image);
            businessName = itemView.findViewById(R.id.business_name);
            totalReviews = itemView.findViewById(R.id.total_reviews);
            distance = itemView.findViewById(R.id.distance);
            foodCategory = itemView.findViewById(R.id.food_category);
            address = itemView.findViewById(R.id.address);
            ratingStar = itemView.findViewById(R.id.imageStar);

            // on item click
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int position = getAdapterPosition();

                    // check if item still exists
                    if(position != RecyclerView.NO_POSITION){
                        String businessId = businessList.get(position).getId();
                        String businessDistance = businessList.get(position).getDistance();
                        //Toast.makeText(v.getContext(), "You clicked "+position+":"+businessId, Toast.LENGTH_SHORT).show();
                        Intent next = new Intent( context, BusinessDetailsActivity.class);
                        next.putExtra(BUSINESS_ID, businessId);
                        next.putExtra(BUSINESS_DISTANCE, businessDistance);
                        context.startActivity(next);
                    }
                }
            });

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

        String priceAndCategory;
        if (currentBusiness.getPrice() == null)
        {
            priceAndCategory = currentBusiness.listFoodCategories();
        }
        else
        {
            priceAndCategory = currentBusiness.getPrice() + " - " + currentBusiness.listFoodCategories();
        }
        holder.foodCategory.setText(priceAndCategory);
        holder.address.setText(currentBusiness.getAddress());
        if (currentBusiness.getRating().equals(5.0)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_5);
        } else if (currentBusiness.getRating().equals(4.5)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_4_half);
        } else if (currentBusiness.getRating().equals(4.0)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_4);
        } else if (currentBusiness.getRating().equals(3.5)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_3_half);
        } else if (currentBusiness.getRating().equals(3.0)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_3);
        } else if (currentBusiness.getRating().equals(2.5)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_2_half);
        } else if (currentBusiness.getRating().equals(2.0)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_2);
        } else if (currentBusiness.getRating().equals(1.5)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_1_half);
        } else if (currentBusiness.getRating().equals(1.0)) {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_1);
        } else {
            holder.ratingStar.setImageResource(R.drawable.stars_regular_0);}



//        holder.foodImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            //Toast.makeText(context, businessList.get(position).getName(), Toast.LENGTH_SHORT).show();
//
//            Intent next = new Intent( context, BusinessDetailsActivity.class);
//            next.putExtra(BUSINESS_ID, businessList.get(position).getId());
//            context.startActivity(next);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

}
