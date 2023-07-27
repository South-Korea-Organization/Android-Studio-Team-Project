package com.himedia.androidshoppingmall.Data;

public class ProductBean {
    String goods_id;
    String goods_title;

    String goods_price;
    String imageRes;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getImageRes() {
        return imageRes;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }

    public ProductBean() {
    }

    public ProductBean(String goods_id, String goods_title, String goods_price, String imageRes) {
        this.goods_id = goods_id;
        this.goods_title = goods_title;
        this.goods_price = goods_price;
        this.imageRes = imageRes;
    }
}
