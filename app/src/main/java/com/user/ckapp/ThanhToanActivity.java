package com.user.ckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.user.ckapp.R;
import com.user.ckapp.model.GioHang;
import com.user.ckapp.model.NotiSendData;
import com.user.ckapp.retrofit.ApiBanHang;
import com.user.ckapp.retrofit.ApiPushNofication;
import com.user.ckapp.retrofit.RetrofitClient;
import com.user.ckapp.retrofit.RetrofitClientNoti;
import com.user.ckapp.utils.Utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail, txttongcong, txtvanchuyen;
    EditText edtdiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien, tonggiasp;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for(int i = 0; i< Utils.mangmuahang.size(); i++){
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        tongtien = getIntent().getLongExtra("tongtien",0);
        txttongcong.setText(decimalFormat.format(tongtien));
        tonggiasp = tongtien + 30000;

        long vanchuyen = getIntent().getLongExtra("vanchuyen",30000);
        txtvanchuyen.setText(decimalFormat.format(vanchuyen));
        //txtvanchuyen.setPaintFlags(txtvanchuyen.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        txttongtien.setText( decimalFormat.format(tonggiasp));
        txtemail.setText(Utils.user_current.getEmail());
        txtsodt.setText(Utils.user_current.getMobile());

        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    edtdiachi.setError("Địa chỉ trống!");
                }else{
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOder(str_email, str_sdt, String.valueOf(tonggiasp), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        pushNotiToUser();
                                        //clear mang gio hang bang cach chay qua manggiohang va clear item trung
                                        for (int i = 0; i<Utils.mangmuahang.size(); i++){
                                            GioHang gioHang = Utils.mangmuahang.get(i);
                                            if(Utils.manggiohang.contains(gioHang)){
                                                Utils.manggiohang.remove(gioHang);
                                            }
                                        }
                                        Utils.mangmuahang.clear();
                                        Paper.book().write("giohang", Utils.manggiohang);
                                        Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        //Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_LONG).show();
                                        Utils.mangmuahang.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                            ));
                }
            }
        });
    }

    private void pushNotiToUser() {
        //gettoken
        compositeDisposable.add(apiBanHang.gettoken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for(int i = 0; i<userModel.getResult().size();i++){
                                    Map<String, String> data = new HashMap<>();
                                    data.put("title", "thong bao");
                                    data.put("body", "Bạn có đơn hàng mới");
                                    NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                                    ApiPushNofication apiPushNofication = RetrofitClientNoti.getInstance().create(ApiPushNofication.class);
                                    compositeDisposable.add(apiPushNofication.sendNofitication(notiSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiRespone -> {

                                                    },
                                                    throwable -> {
                                                        Log.d("logg", throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },throwable -> {
                            Log.d("loggg", throwable.getMessage());
                        }
                ));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar);
        txttongtien = findViewById(R.id.txttongtien);
        txtsodt = findViewById(R.id.txtsodienthoai);
        txtemail = findViewById(R.id.txtemail);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);
        txttongcong = findViewById(R.id.txttongcong);
        txtvanchuyen = findViewById(R.id.txtvanchuyen);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}