package com.example.dldke.foodbox.DataBaseFiles;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.S3Link;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxv-mobilehub-1561206289-user")

public class UserDO {
    private String _userId;
    private Boolean _isCookingClass;
    private String _nickname;
    private Integer _point;
    private String _registerNumber;
    private String _theme;
    private List<String> _themeList;
    private S3Link profileImage;

    public S3Link getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(S3Link profileImage) {
        this.profileImage = profileImage;
    }

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }
    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

    @DynamoDBAttribute(attributeName = "isCookingClass")
    public Boolean getIsCookingClass() {
        return _isCookingClass;
    }
    public void setIsCookingClass(final Boolean _isCookingClass) {
        this._isCookingClass = _isCookingClass;
    }

    @DynamoDBAttribute(attributeName = "nickname")
    public String getNickname() {
        return _nickname;
    }
    public void setNickname(final String _nickname) {
        this._nickname = _nickname;
    }

    @DynamoDBAttribute(attributeName = "point")
    public Integer getPoint() {
        return _point;
    }
    public void setPoint(final Integer _point) {
        this._point = _point;
    }

    @DynamoDBAttribute(attributeName = "registerNumber")
    public String getRegisterNumber() {
        return _registerNumber;
    }
    public void setRegisterNumber(final String _registerNumber) {
        this._registerNumber = _registerNumber;
    }

    @DynamoDBAttribute(attributeName = "theme")
    public String getTheme() {
        return _theme;
    }
    public void setTheme(final String _theme) {
        this._theme = _theme;
    }

    @DynamoDBAttribute(attributeName = "themeList")
    public List<String> getThemeList() {
        return _themeList;
    }
    public void setThemeList(final List<String> _themeList) {
        this._themeList = _themeList;
    }



}
