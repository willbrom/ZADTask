package com.wilbrom.zadtask.utilities;

import com.wilbrom.zadtask.model.Repo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<Repo> parseJson(String rawJson) throws Exception {
        ArrayList<Repo> repoList = new ArrayList<>();

        String repoName;
        String description;
        String owner;
        String repoUrl;
        String ownerUrl;
        String fork;

        JSONArray root = new JSONArray(rawJson);

        for (int i = 0; i < root.length(); i++) {
            JSONObject rootObj = root.getJSONObject(i);
            JSONObject ownerObj = rootObj.getJSONObject("owner");

            repoName = rootObj.getString("name");
            description = rootObj.getString("description");
            repoUrl = rootObj.getString("html_url");
            fork = rootObj.getString("fork");
            owner = ownerObj.getString("login");
            ownerUrl = ownerObj.getString("html_url");

            Repo repo = new Repo(repoName, description, owner, repoUrl, ownerUrl, fork);
            repoList.add(repo);
        }

        return repoList;
    }
}
