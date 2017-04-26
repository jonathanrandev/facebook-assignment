package com.randev.elegantmediaassignment.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.randev.elegantmediaassignment.R;
import com.randev.elegantmediaassignment.model.Friend;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jse on 2/8/2017.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<Friend> friendList;
    private Context context;

    public FriendListAdapter(List<Friend> friends, Context context) {
        friendList = friends;
        this.context = context;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtProfileName.setText(friendList.get(position).getName());
        Picasso.with(context)
                .load(friendList.get(position).getPicture().getData().getUrl())
                .resize(70, 70)
                .into(holder.imgProfilePicture);

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProfileName;
        ImageView imgProfilePicture;

        ViewHolder(View v) {
            super(v);
            txtProfileName = (TextView) v.findViewById(R.id.text_profile_name);
            imgProfilePicture = (ImageView) v.findViewById(R.id.image_profile_picture);
        }
    }
}