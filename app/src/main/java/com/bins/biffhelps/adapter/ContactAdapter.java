package com.bins.biffhelps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bins.biffhelps.R;
import com.bins.biffhelps.model.ContactModel;
import com.bins.biffhelps.service.ContactManager;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @Desc:
 *
 * @author zhouxingbin
 * @date 2022/8/23
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context mContext;

    private List<ContactModel> contactModelList;

    public ContactAdapter(List<ContactModel> contactModelList) {
        this.contactModelList = contactModelList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView contactImage;
        TextView contactName;
        TextView contactPhone;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            contactImage = view.findViewById(R.id.contact_image);
            contactName = view.findViewById(R.id.contact_name);
            contactPhone = view.findViewById(R.id.contact_phone);
        }
    }

    // onCreateViewHolder 是用于创建 ViewHolder 实例的。在这个方法中将 fruit_item 布局加载进来
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            ContactModel contactModel = contactModelList.get(position);
            Toast.makeText(v.getContext(), "你点击了view " + contactModel.getName(), Toast.LENGTH_SHORT).show();
            // 拨打电话
            ContactManager.callPhone(mContext, contactModel.getMobileNumber());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel contactModel = contactModelList.get(position);
        holder.contactName.setText(contactModel.getName());
        holder.contactPhone.setText(contactModel.getMobileNumber());
        Glide.with(mContext).load(R.drawable.nav_icon).into(holder.contactImage);
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }
}
