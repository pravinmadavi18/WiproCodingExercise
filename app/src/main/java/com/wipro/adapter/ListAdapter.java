package com.wipro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wipro.Utility.ApplicationConstants;
import com.wipro.codingexercise.R;
import com.wipro.model.ItemDetails;
import com.wipro.networkhandler.ConnectionManager;

import java.util.List;

/**
 * ListAdapter used as the adapter for RecyclerView in MainActivity
 * @author Pravin Madavi on 7/16/2016.
 * @version 1.0
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    //created for getting the context from class constructor
    private Context mContext;

    //created for getting the list data from class constructor
    private List<ItemDetails> mItemDetailsList;

    //Class constructor with @params as Context and List<ItemDetails>
    public ListAdapter(Context mContext, List<ItemDetails> mItemDetailsList) {
        this.mContext = mContext;
        this.mItemDetailsList = mItemDetailsList;
    }

    //Inner ViewHolder class
    public class ListViewHolder extends RecyclerView.ViewHolder {
        //For ViewHolder views
        public TextView mTitle, mDescription;
        public NetworkImageView mNetworkImageView;

        public ListViewHolder(View view) {
            super(view);
            this.mTitle = (TextView) view.findViewById(R.id.title);
            this.mDescription = (TextView) view.findViewById(R.id.description);
            this.mNetworkImageView = (NetworkImageView) view.findViewById(R.id.networkImageView);
        }
    }


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the row item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        ItemDetails mItemDetails = mItemDetailsList.get(position);

        // Set the title from list item if not null and empty
        if (mItemDetails.getTitle() != null && !mItemDetails.getTitle().isEmpty()) {
            holder.mTitle.setText(mItemDetails.getTitle());
        } else {
            // Set the title as NOT_AVAILABLE if null or empty
            holder.mTitle.setText(ApplicationConstants.NOT_AVAILABLE);
        }

        // Set the Description from list item if not null and empty
        if (mItemDetails.getDescription() != null && !mItemDetails.getDescription().isEmpty()) {
            holder.mDescription.setText(mItemDetails.getDescription());
        } else {
            // Set the Description as NOT_AVAILABLE if null or empty
            holder.mDescription.setText(ApplicationConstants.NOT_AVAILABLE);
        }

        // Set the ErrorImage if fails to load image from server
        holder.mNetworkImageView.setErrorImageResId(R.drawable.image_not_found);

        // Set the ErrorImage if fails to load image from server
        holder.mNetworkImageView.setDefaultImageResId(R.drawable.loading);

        // Set the ImageUrl from list item if not null and empty which will lazily get downloaded
        if (mItemDetails.getImageUrl() != null && !mItemDetails.getImageUrl().isEmpty()) {
            holder.mNetworkImageView.setImageUrl(mItemDetails.getImageUrl(), ConnectionManager.getImageLoader(mContext));
        }

    }

    @Override
    public int getItemCount() {
        // Return count as list size
        return mItemDetailsList.size();
    }


}
