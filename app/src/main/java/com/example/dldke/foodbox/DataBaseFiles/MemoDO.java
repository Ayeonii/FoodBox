package com.example.dldke.foodbox.DataBaseFiles;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxv-mobilehub-1561206289-memo")

public class MemoDO {
    private String _userId;
    private List<RecipeDO.Ingredient> _tobuy= new ArrayList<RecipeDO.Ingredient>();
    private List<RecipeDO.Ingredient> _urgent= new ArrayList<RecipeDO.Ingredient>();

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "tobuy")
    public List<RecipeDO.Ingredient> getTobuy() {
        return _tobuy;
    }

    public void setTobuy(final List<RecipeDO.Ingredient> _tobuy) {
        this._tobuy = _tobuy;
    }
    @DynamoDBAttribute(attributeName = "urgent")
    public List<RecipeDO.Ingredient> getUrgent() {
        return _urgent;
    }

    public void setUrgent(final List<RecipeDO.Ingredient> _urgent) {
        this._urgent = _urgent;
    }
}
