package com.wilbrom.zadtask.model;

/**
 * Created by user on 5/15/2018.
 */

public class Repo {
    private String name;
    private String description;
    private String owner;

    public Repo(String name, String description, String owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }
}
