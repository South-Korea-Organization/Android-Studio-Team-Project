package com.himedia.androidshoppingmall.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.himedia.androidshoppingmall.R;


public class ProductDBHelper extends SQLiteOpenHelper {

    private static ProductDBHelper dbHelper = null;

    private static final String DATABASE_NAME = "productdb";
    private static final String TABLE_NAME = "product";
    private static final int DB_VERSION = 1;

    public static final String COL_0 = "serialNumber";
    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    public static final String COL_3 = "price";
    public static final String COL_4 = "image";
    public static final String COL_5 = "type";

    private Context mContext;

    public static ProductDBHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new ProductDBHelper(context.getApplicationContext());
        }

        return dbHelper;
    }

    private ProductDBHelper(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.mContext = context;

        deleteAllProduct();
        initProduct();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + " ( "
                + COL_0 + " integer primary key autoincrement, "
                + COL_1 + " integer unique, "
                + COL_2 + " text not null, "
                + COL_3 + " integer, "
                + COL_4 + " blob, "
                + COL_5 + " text not null "
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public long insertProduct(ProductBean product){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_1, product.getId());
        values.put(COL_2, product.getName());
        values.put(COL_3, product.getPrice());
        values.put(COL_4, product.getImage());
        values.put(COL_5, product.getType());

        return db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<ProductBean> getAllProduct() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ProductBean> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            ProductBean product = new ProductBean();

            product.setSerialNumber(cursor.getInt(cursor.getColumnIndex(COL_0)));
            product.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
            product.setName(cursor.getString(cursor.getColumnIndex(COL_2)));
            product.setPrice(cursor.getInt(cursor.getColumnIndex(COL_3)));
            product.setImage(cursor.getBlob(cursor.getColumnIndex(COL_4)));
            product.setType(cursor.getString(cursor.getColumnIndex(COL_5)));

            result.add(product);
        }

        return result;
    }

    public ArrayList<ProductBean> getRandomProduct(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME  + " order by random() limit 6 ", null );
        ArrayList<ProductBean> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            ProductBean product = new ProductBean();

            product.setSerialNumber(cursor.getInt(cursor.getColumnIndex(COL_0)));
            product.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
            product.setName(cursor.getString(cursor.getColumnIndex(COL_2)));
            product.setPrice(cursor.getInt(cursor.getColumnIndex(COL_3)));
            product.setImage(cursor.getBlob(cursor.getColumnIndex(COL_4)));
            product.setType(cursor.getString(cursor.getColumnIndex(COL_5)));

            result.add(product);
        }

        return result;
    }

    public ArrayList<ProductBean> getProductbyType(String type){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "type = ?", new String[] {type.toLowerCase()}, null, null, null);
        ArrayList<ProductBean> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            ProductBean product = new ProductBean();

            product.setSerialNumber(cursor.getInt(cursor.getColumnIndex(COL_0)));
            product.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
            product.setName(cursor.getString(cursor.getColumnIndex(COL_2)));
            product.setPrice(cursor.getInt(cursor.getColumnIndex(COL_3)));
            product.setImage(cursor.getBlob(cursor.getColumnIndex(COL_4)));
            product.setType(cursor.getString(cursor.getColumnIndex(COL_5)));

            result.add(product);
        }

        return result;
    }

    public long deleteAllProduct(){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    private void initProduct(){
        init("product", 1, "근조화환1", 1000, getByteArrayFromDrawable(R.drawable.g_haohan), "화환");
        init("product", 2,"근조화환2", 2000, getByteArrayFromDrawable(R.drawable.g_haohan_2), "화환");
        init("product", 3, "근조화환3", 1000, getByteArrayFromDrawable(R.drawable.g_haohan_3), "화환");
        init("product", 4, "근조화환4", 2000, getByteArrayFromDrawable(R.drawable.g_haohan_4), "화환");
        init("product", 5, "근조화환5", 10000, getByteArrayFromDrawable(R.drawable.g_haohan_5), "화환");
        init("product", 6, "동양란1", 2000, getByteArrayFromDrawable(R.drawable.d_yangran_1), "관상식물");
        init("product", 7, "동양란2", 10000, getByteArrayFromDrawable(R.drawable.d_yangran_2), "관상식물");
        init("product", 8, "동양란3", 2000, getByteArrayFromDrawable(R.drawable.d_yangran_3), "관상식물");
        init("product", 9, "동양란4", 2000, getByteArrayFromDrawable(R.drawable.d_yangran_4), "관상식물");
        init("product", 10, "동양란5", 2000, getByteArrayFromDrawable(R.drawable.d_yangran_5), "관상식물");
        init("product", 11, "공기정화식물1", 10000, getByteArrayFromDrawable(R.drawable.k_jeonghao_1), "기능성식물");
        init("product", 12, "공기정화식물2", 2000, getByteArrayFromDrawable(R.drawable.k_jeonghao_2), "기능성식물");
        init("product", 13, "공기정화식물3", 10000, getByteArrayFromDrawable(R.drawable.k_jeonghao_3), "기능성식물");
        init("product", 14, "공기정화식물4", 3000, getByteArrayFromDrawable(R.drawable.k_jeonghao_4), "기능성식물");
        init("product", 15, "공기정화식물5", 10000, getByteArrayFromDrawable(R.drawable.k_jeonghao_5), "기능성식물");
        init("product", 16, "꽃다발1", 3000, getByteArrayFromDrawable(R.drawable.f_bal_1), "꽃배달서비스");
        init("product", 17, "꽃다발2", 10000, getByteArrayFromDrawable(R.drawable.f_bal_2), "꽃배달서비스");
        init("product", 18, "꽃다발3", 3000, getByteArrayFromDrawable(R.drawable.f_bal_3), "꽃배달서비스");
        init("product", 19, "꽃다발4", 3000, getByteArrayFromDrawable(R.drawable.f_bal_4), "꽃배달서비스");
        init("product", 20, "꽃다발5", 10000, getByteArrayFromDrawable(R.drawable.f_bal_5), "꽃배달서비스");
        init("product", 21, "개업식화분6", 3000, getByteArrayFromDrawable(R.drawable.g_haobun_06), "부가제품");
        init("product", 22, "개업식화분7", 10000, getByteArrayFromDrawable(R.drawable.g_haobun_07), "부가제품");
        init("product", 23, "개업식화분8", 3000, getByteArrayFromDrawable(R.drawable.g_haobun_08), "부가제품");
        init("product", 24, "개업식화분9", 10000, getByteArrayFromDrawable(R.drawable.g_haobun_09), "부가제품");
        init("product", 25, "개업식화분10", 3000, getByteArrayFromDrawable(R.drawable.g_haobun_10), "부가제품");
    }

    private void init(String tableName, int id, String pName, int pPrice, byte[] pImage, String type){
        ProductBean productBean = new ProductBean();

        productBean.setId(id);
        productBean.setName(pName);
        productBean.setPrice(pPrice);
        productBean.setImage(pImage);
        productBean.setType(type);

        insertProduct(productBean);
    }

    // drawable 이미지를 sqlite에 넣기 위해 byteArray로 변환하는 함수
    private byte[] getByteArrayFromDrawable(int image){
        Drawable drawable = mContext.getDrawable(image);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] dataByte = stream.toByteArray();

        return dataByte;
    }
}
