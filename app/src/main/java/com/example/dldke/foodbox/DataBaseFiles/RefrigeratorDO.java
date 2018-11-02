package com.example.dldke.foodbox.DataBaseFiles;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxtest-mobilehub-942131300-refrigerator")

public class RefrigeratorDO {
    private String _userId = "";
    private List<Item> _item = new ArrayList<Item>();


    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }
    public void setUserId(final String _userId) {
        this._userId = _userId;
    }


    @DynamoDBAttribute(attributeName = "item")
    public List<Item> getItem() {
        return _item;
    }
    public void setItem(final List<Item> _item) {
        this._item = _item;
    }


    @DynamoDBDocument
    public static class Item {
        private String _name;
        private Double _count;
        private String _section;
        private String _kindOf;
        private Integer _dueDate;

        @DynamoDBAttribute(attributeName = "name")
        public String getName() { return _name; }
        public void setName(final String _name) { this._name = _name; }

        @DynamoDBAttribute(attributeName = "count")
        public Double getCount() {
            return _count;
        }
        public void setCount(final Double _count) {
            this._count = _count;
        }

        @DynamoDBAttribute(attributeName = "section")
        public String getSection() { return _section; }
        public void setSection(final String _section) {
            this._section = _section;
        }

        @DynamoDBAttribute(attributeName = "kindOf")
        public String getKindOf() {
            return _kindOf;
        }
        public void setKindOf(final String _kindOf) { this._kindOf = _kindOf; }

        @DynamoDBAttribute(attributeName = "dueDate")
        public Integer getDueDate() {
            return _dueDate;
        }
        public void setDueDate(final Integer _dueDate) {
            this._dueDate = _dueDate;
        }

    }
}
