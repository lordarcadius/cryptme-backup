package com.hexoncode.cryptit.data;

public class Donation {

    private String skuId;
    private String name;

    public Donation(String skuId, String name) {
        this.skuId = skuId;
        this.name = name;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
