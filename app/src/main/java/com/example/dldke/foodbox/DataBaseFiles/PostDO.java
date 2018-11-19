package com.example.dldke.foodbox.DataBaseFiles;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "foodboxv-mobilehub-1561206289-post")

public class PostDO {
    private String _postId;
    private List<Comment> _commentList = new ArrayList<Comment>();

    private String _date;
    private String _recipeId;
    private String _title;
    private String _writer;
    private Integer _postLevel;

    @DynamoDBHashKey(attributeName = "postId")
    @DynamoDBAttribute(attributeName = "postId")
    public String getPostId() {
        return _postId;
    }

    public void setPostId(final String _postId) {
        this._postId = _postId;
    }
    @DynamoDBAttribute(attributeName = "commentList")
    public List<Comment> getCommentList() {
        return _commentList;
    }

    public void setCommentList(final List<Comment> _commentList) {
        this._commentList = _commentList;
    }

    @DynamoDBAttribute(attributeName = "postLevel")
    public Integer getPostLevel() {
        return _postLevel;
    }
    public void setPostLevel(final Integer _postLevel) {
        this._postLevel = _postLevel;
    }

    @DynamoDBAttribute(attributeName = "date")
    public String getDate() {
        return _date;
    }

    public void setDate(final String _date) {
        this._date = _date;
    }
    @DynamoDBAttribute(attributeName = "recipeId")
    public String getRecipeId() {
        return _recipeId;
    }

    public void setRecipeId(final String _recipeId) {
        this._recipeId = _recipeId;
    }
    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return _title;
    }

    public void setTitle(final String _title) {
        this._title = _title;
    }

    @DynamoDBIndexHashKey(attributeName = "writer", globalSecondaryIndexName = "writerIndex")
    public String getWriter() {
        return _writer;
    }

    public void setWriter(final String _writer) {
        this._writer = _writer;
    }



    @DynamoDBDocument
    public static class Comment {
        private String _content;
        private String _date;
        private String _userId;

        @DynamoDBAttribute(attributeName = "userId")
        public String getUserId() {
            return _userId;
        }

        public void setUserId(final String _userId) {
            this._userId = _userId;
        }

        @DynamoDBAttribute(attributeName = "comment_date")
        public String getDate() {
            return _date;
        }

        public void setDate(final String _date) {
            this._date = _date;
        }

        @DynamoDBAttribute(attributeName = "content")
        public String getContent() {
            return _content;
        }

        public void setContent(final String _content) {
            this._content = _content;
        }
    }
}
