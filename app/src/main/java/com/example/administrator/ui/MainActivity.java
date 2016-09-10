package com.example.administrator.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import com.example.administrator.hook.R;
import com.example.administrator.utils.DbHelper;


public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap mBaidumap;
    private BitmapDescriptor bitmapDescriptor;
    private String address = "";
    public static double mylatitude = 39.9531;
    public static double mylongitude = 116.2243;
    private SQLiteDatabase mySQLiteDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //地图控件引用
        mapView = (MapView) findViewById(R.id.map_view);
        mBaidumap = mapView.getMap();
        //是否显示比例尺控件
        mapView.showScaleControl(true);
        //是否显示缩放控件
        mapView.showZoomControls(true);
        //去掉百度地图Logo
        mapView.removeViewAt(1);

        //设置标记图标
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.id.map_view);

        mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mySQLiteDb = new DbHelper(MainActivity.this).getWritableDatabase();
                ContentValues values = new ContentValues();
                //点击地图事件监听
                //获取经纬度
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                values.put("latitude",latitude);
                values.put("longitude",longitude);
                mySQLiteDb.insert("Location",null,values);
                values.clear();

                mylatitude = latitude;
                mylongitude = longitude;

                Toast.makeText(MainActivity.this,"latitude: "+mylatitude+"\n"+"longitude: "+mylongitude,Toast.LENGTH_SHORT).show();
                //清楚图层
                mBaidumap.clear();

                //定义Maker坐标点
                LatLng point = new LatLng(latitude,longitude);
                //构建MakerOption，用以在地图上添加Marker

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
