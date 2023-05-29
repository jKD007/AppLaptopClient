package com.user.ckapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.user.ckapp.R;
import com.user.ckapp.model.DanhGia;

import java.util.List;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.MyViewHolder> {
    Context context;
    List<DanhGia> array;

    public DanhGiaAdapter(Context context, List<DanhGia> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhgia, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DanhGia danhGia = array.get(position);
        holder.danhgia_ten.setText(danhGia.getTen());
        holder.danhgia_danhgia.setText(danhGia.getDanhgia());

    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView danhgia_ten, danhgia_danhgia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            danhgia_ten = itemView.findViewById(R.id.danhgia_ten);
            danhgia_danhgia = itemView.findViewById(R.id.danhgia_danhgia);
        }
    }
}
