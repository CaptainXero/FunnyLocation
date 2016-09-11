package com.example.administrator.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import com.example.administrator.hook.R;
import com.example.administrator.utils.DbHelper;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity implements BaiduMap.OnMapClickListener{
    private MapView mapView;
    private BaiduMap mBaidumap;
    private BitmapDescriptor bitmapDescriptor;
    private int lac = 0, cid = 0;
    private String pacakgeName;
    private SQLiteDatabase mSQLiteDatabase;
    private LatLng latLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pacakgeName = getIntent().getStringExtra("package_name");
        mSQLiteDatabase = new DbHelper(this).getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query(DbHelper.APP_TABLE_NAME, new String[]{"latitude,longitude"}, "package_name=?", new String[]{pacakgeName}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            //拿到经纬度
            double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));
            //设置标记
            LatLng latLng1 = new LatLng(lat, lon);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng1);
            markerOptions.draggable(true);
            markerOptions.title("经度：" + latLng1.longitude + ",纬度：" + latLng1.latitude);
            cursor.close();

        }
        pacakgeName = getIntent().getStringExtra("package_name");
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
        mBaidumap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaidumap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("经度：" + latLng.longitude + ",纬度：" + latLng.latitude);
//        mBaidumap.addMarker(markerOptions);
        this.latLng = latLng;
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ok:
                if (latLng == null) {
                    Toast.makeText(this, "请点击地图选择一个地点！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage("利用基站定位的应用（qq钉钉等）需填写基站信息")
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                ContentValues contentValues = new ContentValues();
                contentValues.put("package_name", pacakgeName);
                contentValues.put("latitude", latLng.latitude);
                contentValues.put("longitude", latLng.longitude);
                contentValues.put("lac", lac);
                contentValues.put("cid", cid);
                mSQLiteDatabase.insertWithOnConflict(DbHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case R.id.search:
                //实现搜索位置功能
                break;
            case R.id.lac:
                View view1 = getLayoutInflater().inflate(R.layout.dialog_lac_cid, null, false);
                final TextInputEditText etLac = (TextInputEditText) view1.findViewById(R.id.lac);
                final TextInputEditText etCid = (TextInputEditText) view1.findViewById(R.id.cid);
                new AlertDialog.Builder(MainActivity.this).setTitle("填写基站信息").setView(view1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canCloseDialog(dialog, false);
                        if (TextUtils.isEmpty(etLac.getText())) {
                            etLac.setError("lac的值不应该为空");
                        }
                        if (TextUtils.isEmpty(etCid.getText())) {
                            etCid.setError("cid的值不应该为空");
                        }
                        if (!TextUtils.isEmpty(etLac.getText()) && !TextUtils.isEmpty(etCid.getText())) {
                            int lac1 = Integer.parseInt(etLac.getText().toString());
                            int cid1 = Integer.parseInt(etCid.getText().toString());
                            if (lac1 <= 0 || lac1 >= 65535) {
                                etLac.setError("lac的值应该是0~65535");
                                lac1 = 0;
                            }
                            if (cid1 <= 0 || cid1 >= 65535) {
                                etCid.setError("cid的值应该是0~65535");
                                cid1 = 0;
                            }
                            lac = lac1;
                            cid = cid1;
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("package_name", pacakgeName);
                        contentValues.put("lac", lac);
                        contentValues.put("cid", cid);
                        mSQLiteDatabase.insertWithOnConflict(DbHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
