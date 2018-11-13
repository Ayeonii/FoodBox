package com.example.dldke.foodbox.DataBaseFiles;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.S3Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxv-mobilehub-1561206289-recipe")

public class RecipeDO {
    private String _recipeId;
    private String _date;
    private Detail _detail;
    private List<Ingredient> _ingredient = new ArrayList<Ingredient>();
    private S3Link recipeImage;

    public S3Link getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(S3Link recipeImage) {
        this.recipeImage = recipeImage;
    }

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
    public Detail getDetail() {
        return _detail;
    }

    public void setDetail(final Detail _detail) {
        this._detail = _detail;
    }

    @DynamoDBAttribute(attributeName = "ingredient")
    public List<Ingredient> getIngredient() {
        return _ingredient;
    }

    public void setIngredient(final List<Ingredient> _ingredient) {
        this._ingredient = _ingredient;
    }



    @DynamoDBDocument
    public static class Detail{
        private String _foodName;
        private List<Spec> _specList = new ArrayList<Spec>();

        @DynamoDBAttribute(attributeName = "foodName")
        public String getFoodName() {
            return _foodName;
        }

        public void setFoodName(final String _foodName) {
            this._foodName = _foodName;
        }
        @DynamoDBAttribute(attributeName = "specList")
        public List<Spec> getSpecList() {
            return _specList;
        }

        public void setSpecList(final List<Spec> _specList) {
            this._specList = _specList;
        }
    }

    @DynamoDBDocument
    public static class Spec{
        private String _specFire;
        private List<Ingredient> _specIngredient = new ArrayList<Ingredient>();
        private String _specMethod;
        private Integer _specMinute;

        @DynamoDBAttribute(attributeName = "specFire")
        public String getSpecFire() {
            return _specFire;
        }

        public void setSpecFire(final String _specFire) {
            this._specFire = _specFire;
        }
        @DynamoDBAttribute(attributeName = "specIngredient")
        public List<Ingredient> getSpecIngredient() {
            return _specIngredient;
        }

        public void setSpecIngredient(final List<Ingredient> _specIngredient) {
            this._specIngredient = _specIngredient;
        }
        @DynamoDBAttribute(attributeName = "specMethod")
        public String getSpecMethod() {
            return _specMethod;
        }

        public void setSpecMethod(final String _specMethod) {
            this._specMethod = _specMethod;
        }
        @DynamoDBAttribute(attributeName = "specMinute")
        public Integer getSpecMinute() {
            return _specMinute;
        }

        public void setSpecMinute(final Integer _specMinute) {
            this._specMinute = _specMinute;
        }
    }

    @DynamoDBDocument
    public static class Ingredient {
        private String _ingredientName;
        private Double _ingredientCount;
        private String _ingredientDuedate;

        @DynamoDBAttribute(attributeName = "ingredientName")
        public String getIngredientName() {
            return _ingredientName;
        }

        public void setIngredientName(final String _ingredientName) {
            this._ingredientName = _ingredientName;
        }
        @DynamoDBAttribute(attributeName = "ingredientCount")
        public Double getIngredientCount() {
            return _ingredientCount;
        }

        public void setIngredientCount(final Double _ingredientCount) {
            this._ingredientCount = _ingredientCount;
        }
        @DynamoDBAttribute(attributeName = "ingredientDuedate")
        public String getIngredientDuedate() {
            return _ingredientDuedate;
        }

        public void setIngredientDuedate(final String _ingredientDuedate) {
            this._ingredientDuedate = _ingredientDuedate;
        }
    }
}
