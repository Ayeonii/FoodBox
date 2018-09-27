package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxtest-mobilehub-942131300-recipe")

public class RecipeDO {
    private String _recipeId;
    private String _date;
    private Map<String, String> _detail;
    private String _foodName;
    private List<String> _ingredient;
    private String _specFire;
    private List<String> _specIngredient;
    private List<String> _specList;
    private String _specMethod;
    private Double _specMinute;

    @DynamoDBHashKey(attributeName = "recipeId")
    @DynamoDBAttribute(attributeName = "recipeId")
    public String getRecipeId() {
        return _recipeId;
    }

    public void setRecipeId(final String _recipeId) {
        this._recipeId = _recipeId;
    }
    @DynamoDBAttribute(attributeName = "date")
    public String getDate() {
        return _date;
    }

    public void setDate(final String _date) {
        this._date = _date;
    }
    @DynamoDBAttribute(attributeName = "detail")
    public Map<String, String> getDetail() {
        return _detail;
    }

    public void setDetail(final Map<String, String> _detail) {
        this._detail = _detail;
    }
    @DynamoDBAttribute(attributeName = "foodName")
    public String getFoodName() {
        return _foodName;
    }

    public void setFoodName(final String _foodName) {
        this._foodName = _foodName;
    }
    @DynamoDBAttribute(attributeName = "ingredient")
    public List<String> getIngredient() {
        return _ingredient;
    }

    public void setIngredient(final List<String> _ingredient) {
        this._ingredient = _ingredient;
    }
    @DynamoDBAttribute(attributeName = "specFire")
    public String getSpecFire() {
        return _specFire;
    }

    public void setSpecFire(final String _specFire) {
        this._specFire = _specFire;
    }
    @DynamoDBAttribute(attributeName = "specIngredient")
    public List<String> getSpecIngredient() {
        return _specIngredient;
    }

    public void setSpecIngredient(final List<String> _specIngredient) {
        this._specIngredient = _specIngredient;
    }
    @DynamoDBAttribute(attributeName = "specList")
    public List<String> getSpecList() {
        return _specList;
    }

    public void setSpecList(final List<String> _specList) {
        this._specList = _specList;
    }
    @DynamoDBAttribute(attributeName = "specMethod")
    public String getSpecMethod() {
        return _specMethod;
    }

    public void setSpecMethod(final String _specMethod) {
        this._specMethod = _specMethod;
    }
    @DynamoDBAttribute(attributeName = "specMinute")
    public Double getSpecMinute() {
        return _specMinute;
    }

    public void setSpecMinute(final Double _specMinute) {
        this._specMinute = _specMinute;
    }

}
