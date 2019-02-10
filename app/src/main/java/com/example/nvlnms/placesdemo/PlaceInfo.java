package com.example.nvlnms.placesdemo;

/**
 * Created by NVLNMS on 22-01-2018.
 */

public class PlaceInfo {

    String placeName;
    String vicinity;
    String urlRef;
    public PlaceInfo(String n,String add,String ref)
    {
        placeName=n;
        vicinity=add;
        urlRef=ref;
    }

    public String getName()
    {
        return placeName;
    }

    public String getImgref() {
        return urlRef;
    }

    public void setUrlRef(String imgUrl) {
        this.urlRef = imgUrl;
    }

    public String getAddress()
    {
        return vicinity;
    }

    public void setName(String n)
    {
        placeName=n;
    }
    public void setVicinity(String v)
    {
        vicinity=v;
    }
}
