package com.wipro.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ResponseData class is Model class used to map title and row data from JSON response using GSON
 * @author Pravin Madavi
 * @version 1.0
 */
public class ResponseData {

   @SerializedName("title")
    public String title;

    public List<ItemDetails> rows;
    public List<ItemDetails> getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }


}
