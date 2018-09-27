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

@DynamoDBTable(tableName = "foodboxtest-mobilehub-942131300-Info")

public class InfoDO {
    private String _name;
    private String _section;
    private Integer _dueDate;
    private String _kindOf;

    @DynamoDBHashKey(attributeName = "name")
    @DynamoDBIndexHashKey(attributeName = "name", globalSecondaryIndexName = "name-kindOf")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBRangeKey(attributeName = "section")
    @DynamoDBAttribute(attributeName = "section")
    public String getSection() {
        return _section;
    }

    public void setSection(final String _section) {
        this._section = _section;
    }
    @DynamoDBAttribute(attributeName = "dueDate")
    public Integer getDueDate() {
        return _dueDate;
    }

    public void setDueDate(final Integer _dueDate) {
        this._dueDate = _dueDate;
    }
    @DynamoDBIndexRangeKey(attributeName = "kindOf", globalSecondaryIndexName = "name-kindOf")
    public String getKindOf() {
        return _kindOf;
    }

    public void setKindOf(final String _kindOf) {
        this._kindOf = _kindOf;
    }

}
