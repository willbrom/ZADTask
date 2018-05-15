package com.wilbrom.zadtask.model;

/**
 * Created by user on 5/15/2018.
 */

public class Repo {
    private String name;
    private String description;
    private String owner;
    private String repoUrl;
    private String ownerUrl;

    public Repo(String name, String description, String owner, String repoUrl, String ownerUrl) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.repoUrl = repoUrl;
        this.ownerUrl = ownerUrl;
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

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getOwnerUrl() {
        return ownerUrl;
    }
}
