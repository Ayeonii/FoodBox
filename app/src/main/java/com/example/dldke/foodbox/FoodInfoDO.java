package com.example.dldke.foodbox;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxtest-mobilehub-942131300-foodInfo")

public class FoodInfoDO {
    private String _칸이름;

    @DynamoDBHashKey(attributeName = "칸이름")
    @DynamoDBAttribute(attributeName = "칸이름")
    public String get칸이름() {
        return _칸이름;
    }

    public void set칸이름(final String _칸이름) {
        this._칸이름 = _칸이름;
    }

}
