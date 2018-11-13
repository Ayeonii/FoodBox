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

@DynamoDBTable(tableName = "foodboxv-mobilehub-1561206289-myCommunity")

public class MyCommunityDO {
    private String _userId;
    private List<String> _favorites = new ArrayList<String>();
    private List<String> _myRecipes = new ArrayList<String>();

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "favorites")
    public List<String> getFavorites() {
        return _favorites;
    }

    public void setFavorites(final List<String> _favorites) {
        this._favorites = _favorites;
    }
    @DynamoDBAttribute(attributeName = "myRecipes")
    public List<String> getMyRecipes() {
        return _myRecipes;
    }

    public void setMyRecipes(final List<String> _myRecipes) {
        this._myRecipes = _myRecipes;
    }

}
