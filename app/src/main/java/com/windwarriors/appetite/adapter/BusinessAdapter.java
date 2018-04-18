package com.windwarriors.appetite.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private Context context;
    private View.OnClickListener loadMoreClickListener;

    class BusinessViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView businessName;
        TextView totalReviews;
        TextView distance;
        TextView foodCategory;
        TextView address;
        ImageView ratingStar;
        ProgressBar listProgressBar;
        Button loadMoreButton;


        BusinessViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image);
            businessName = itemView.findViewById(R.id.business_name);
            totalReviews = itemView.findViewById(R.id.total_reviews);
            distance = itemView.findViewById(R.id.distance);
            foodCategory = itemView.findViewById(R.id.food_category);
            address = itemView.findViewById(R.id.address);
            ratingStar = itemView.findViewById(R.id.imageStar);
            listProgressBar = itemView.findViewById(R.id.listProgress);
            loadMoreButton = itemView.findViewById(R.id.load_more);


            // on item click
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int position = getAdapterPosition();

                    // check if item still exists
                    if(position != RecyclerView.NO_POSITION){
                        String businessId = businessList.get(position).getId();
                        String businessDistance = businessList.get(position).getFormattedDistance();
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


    public BusinessAdapter(Context context) {
        this.context = context;
        this.businessList = new ArrayList<>();
    }

    public void refreshBusinessList(ArrayList<Business> businessList) {
        this.businessList.clear();
        this.businessList.addAll(businessList);
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_item, parent, false);
        return new BusinessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, final int position) {
        final Business currentBusiness = businessList.get(position);

        Log.v("BusinessAdapter: ", currentBusiness.toString());
        new DownloadImageTask(holder.foodImage).execute(currentBusiness.getImageLink());

        holder.businessName.setText(currentBusiness.getName());
        holder.totalReviews.setText(currentBusiness.getTotalReviews().toString());
        holder.distance.setText(currentBusiness.getFormattedDistance());

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

        // Last item in recycle view, then show "Load More" button
        if(//businessListSizeWithoutPaging > Constants.PAGE_SIZE &&
            position == businessList.size()-1
            //&& position != Constants.DEFAULT_YELP_SERVICE_LIST_SIZE-1
        ){
            holder.loadMoreButton.setText( context.getString( R.string.next ) );
            holder.loadMoreButton.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                holder.loadMoreButton.setBackgroundColor( context.getColor( R.color.buttonBackground) );
            else
                holder.loadMoreButton.setBackgroundColor(context.getResources().getColor(R.color.buttonBackground));
                //holder.loadMoreButton.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonBackground));

            holder.loadMoreButton.setVisibility(View.VISIBLE);
            // Setup the listener for "Load More" button
            if( this.loadMoreClickListener != null )
                holder.loadMoreButton.setOnClickListener(this.loadMoreClickListener);
        }

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

    public void setLoadMoreClickListener(View.OnClickListener loadMoreClickListener) {
        this.loadMoreClickListener = loadMoreClickListener;
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
