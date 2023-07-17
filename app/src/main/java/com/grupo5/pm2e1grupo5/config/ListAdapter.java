package com.grupo5.pm2e1grupo5.config;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grupo5.pm2e1grupo5.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Contactos> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapter.onItemClickListener listener;

    public interface onItemClickListener{
        void onItemClick(Contactos item);
    }

    public ListAdapter(List<Contactos> mData, Context context, ListAdapter.onItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
   public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }
    public void setItems(List<Contactos> items){
        mData=items;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView name,phone,id;
        ViewHolder(View itemView){
            super (itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            name = itemView.findViewById(R.id.nameTextView);
            phone = itemView.findViewById(R.id.phoneTextView);
            id = itemView.findViewById(R.id.idTextView);

        }

        void bindData(final Contactos item){
//            iconImage.setColorFilter(Color.parseColor("#FFF"), PorterDuff.Mode.SRC_IN);
            name.setText(item.getNombres());
            phone.setText(item.getTelefono());
            id.setText(item.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
