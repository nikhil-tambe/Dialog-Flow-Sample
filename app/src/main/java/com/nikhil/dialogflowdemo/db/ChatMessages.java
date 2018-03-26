package com.nikhil.dialogflowdemo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

/**
 * Created by Nikhil on 26/3/18.
 */

//@Entity
public class ChatMessages {

    //@PrimaryKey()
    private long id;

    private boolean isUser;

    //@ColumnInfo(name = "agentName")
    private String name;

    //@ColumnInfo(name = "message")
    @Nullable
    private String message;

    public ChatMessages(boolean isUser, @Nullable String name, String message) {
        this.isUser = isUser;
        this.name = name;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
