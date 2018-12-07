package com.example.dldke.foodbox.DataBaseFiles;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.S3Link;

import java.util.List;

@DynamoDBTable(tableName = "foodboxv-mobilehub-1561206289-Info")

public class InfoDO {
    private String _name;
    private String _section;
    private Integer _dueDate;
    private String _kindOf;
    private List<String> productName;
    private Boolean _isFrozen;
    private S3Link infoImage;


    public S3Link getInfoImage() {
        return infoImage;
    }

    public void setInfoImage(S3Link infoImage) {
        this.infoImage = infoImage;
    }

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

    @DynamoDBAttribute(attributeName = "productName")
    public List<String> getProductName() {
        return productName;
    }

    public void setIsFrozen(final Boolean _isFrozen) {
        this._isFrozen = _isFrozen;
    }
    @DynamoDBIndexRangeKey(attributeName = "isFrozen", globalSecondaryIndexName = "name-kindOf")
    public Boolean getisFrozenf() {
        return _isFrozen;
    }

    public void setProductName(final List<String> productName) {
        this.productName = productName;
    }

}
