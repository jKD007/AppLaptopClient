package com.user.ckapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.user.ckapp.R;
import com.user.ckapp.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool= new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;

    public DonHangAdapter(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }



    public DonHangAdapter(List<DonHang> listdonhang, Context context) {
        this.listdonhang = listdonhang;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.txtdonhang.setText("ID Đơn hàng: "+donHang.getId());
        holder.diachi.setText("Địa chỉ: " +donHang.getDiachi());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tongtien_donhang.setText("Tổng tiền: " + decimalFormat.format(Double.parseDouble(donHang.getTongtien())) + "đ");
        holder.trangthai.setText(trangThaiDon(donHang.getTrangthai()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
          holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adapter chi tiet
        ChitietAdapter chitietAdapter = new ChitietAdapter(context, donHang.getItem());
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chitietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);
    }

    private String trangThaiDon(int status){
        String result = "";
        switch (status){
            case 0:
                result = "Đơn hàng đang được xử lí";
                break;
            case 1:
                result = "Đơn hàng đã được chấp nhận";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Đã hoàn thành đơn hàng";
                break;
            case 4:
                result = "Đơn hàng đã hủy";
                break;
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang, trangthai, diachi, tongtien_donhang;
        RecyclerView reChitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            trangthai = itemView.findViewById(R.id.tinhtrang);
            diachi = itemView.findViewById(R.id.diachi_donhang);
            tongtien_donhang = itemView.findViewById(R.id.tongtien_donhang);
            reChitiet = itemView.findViewById(R.id.recycleview_chitiet);
        }
    }
}
