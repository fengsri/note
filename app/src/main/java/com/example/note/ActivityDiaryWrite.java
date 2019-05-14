package com.example.note;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.note.dao.DiaryDao;
import com.example.note.util.DateUtil;
import com.example.note.util.ImageUtil;
import com.example.note.util.UserUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ActivityDiaryWrite extends AppCompatActivity implements View.OnClickListener {

    private TextView diaryTitle;
    private TextView diaryText;
    private TextView addressText;
    private TextView weatherText;
    private ImageView weatherImage;
    private ImageView xaingji;
    private TextView save;

    private Uri imageUri;
    private String title;
    private String text;
    private String address;
    private String weather;
    private String date;
    private String pic;
    private String icon;
    private File outputImage;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        ActionBar supportActionBar = getSupportActionBar();
        this.setTitle("记录日记");
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标

        diaryTitle = findViewById(R.id.diary_title);
        diaryText = findViewById(R.id.diary_text);
        addressText = findViewById(R.id.dairy_write_address_text);
        weatherText  =findViewById(R.id.dairy_wirte_weather);
        weatherImage = findViewById(R.id.dairy_write_weather_image);
        xaingji  =findViewById(R.id.dairy_write_xiangji);
        save  =findViewById(R.id.dairy_write_bao);
        weatherText.setOnClickListener(this);
        weatherImage.setOnClickListener(this);
        xaingji.setOnClickListener(this);
        save.setOnClickListener(this);

        date = DateUtil.today();

        initLocationOption();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dairy_wirte_weather:{
                show();
                break;
            }
            case R.id.dairy_write_weather_image:{
                show();
                break;
            }
            case R.id.dairy_write_xiangji:{
                setPic();
                break;
            }
            case R.id.dairy_write_bao:{
                save();
                finish();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:////主键id 必须这样写
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save(){
        title = diaryTitle.getText().toString();
        text = diaryText.getText().toString();
        weather = weatherText.getText().toString();
        address = addressText.getText().toString();
        title = diaryTitle.getText().toString();
        DiaryDao.saveBeanDiaryToYun(UserUtil.user.getObjectId(),date,title,text,pic,pic,address,weather,ActivityDiaryWrite.this);
    }

    private void show(){
        final String[] items = new String[] { "晴天","阴天","多云","雨天","雪天","雷雨"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDiaryWrite.this);
        // 设置参数
        builder.setIcon(R.drawable.tianqi).setTitle("天气")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            weather = "晴天";
                            weatherText.setText("晴天");
                        }else if(which==1){
                            weather = "阴天";
                            weatherText.setText("阴天");
                        }else if(which==2){
                            weather = "多云";
                            weatherText.setText("多云");
                        }else if(which==3){
                            weather = "雨天";
                            weatherText.setText("雨天");
                        }else if(which==4){
                            weather = "雪天";
                            weatherText.setText("雪天");
                        }else if(which==5){
                            weather = "雷雨";
                            weatherText.setText("雷雨");
                        }
                    }
                });
        builder.create().show();
    }


    public void setPic(){
        final String[] items = new String[] { "相册","照相机"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDiaryWrite.this);
        // 设置参数
        builder.setIcon(R.drawable.xuanze).setTitle("选择头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, 100);
                        }else if(which==1){
                            outputImage = new File(getExternalCacheDir(), ImageUtil.getCreateUrl() + ".jpg");
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(ActivityDiaryWrite.this, "com.example.asus.watermelonenglish.fileprovider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }
                            imageUri = Uri.fromFile(outputImage);
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                Glide.with(ActivityDiaryWrite.this)
                        .load(data.getDataString())
                        .error(R.drawable.xiangji)
                        .dontAnimate()
                        .into(xaingji);
                pic = data.getDataString();
            }
        }
        if (resultCode == RESULT_OK && requestCode == 1) {
            Glide.with(ActivityDiaryWrite.this)
                    .load(outputImage.getAbsolutePath())
                    .error(R.drawable.touxiang)
                    .dontAnimate()
                    .into(xaingji);
           // pic = imageUri.getPath();
            pic = outputImage.getAbsolutePath();
        }
    }



 /*   public void setPic(){
        final String[] items = new String[] { "相册","照相机"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDiaryWrite.this);
        // 设置参数
        builder.setIcon(R.drawable.xuanze).setTitle("选择头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, 100);
                        }else if(which==1){
                            File outputImage = new File(getExternalCacheDir(), ImageUtil.getCreateUrl() + ".jpg");
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(ActivityDiaryWrite.this, "com.example.asus.watermelonenglish.fileprovider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }
                            imageUri = Uri.fromFile(outputImage);
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
        builder.create().show();
    }*/


    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位
        locationClient.start();
    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();
            final String addr = location.getAddrStr();    //获取详细地址信息
            final String country = location.getCountry();    //获取国家
            final String province = location.getProvince();    //获取省份
            final String city = location.getCity();    //获取城市
            final String district = location.getDistrict();    //获取区县
            final String street = location.getStreet();    //获取街道信息
            if(addr!=null){
                addressText.setText(addr);
            }else{
                addressText.setText("四川省成都市西华大学");
            }
        }
    }
}
