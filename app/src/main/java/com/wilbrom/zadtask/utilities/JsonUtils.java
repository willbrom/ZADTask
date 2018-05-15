package com.wilbrom.zadtask.utilities;

import com.wilbrom.zadtask.model.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 5/15/2018.
 */

public class JsonUtils {
    public static ArrayList<Repo> parseJson(String rawJson) throws JSONException {
        ArrayList<Repo> repoList = new ArrayList<>();

        String repoName;
        String description;
        String owner;
        String repoUrl;
        String ownerUrl;

        JSONArray root = new JSONArray(rawJson);

        for (int i = 0; i < root.length(); i++) {
            JSONObject rootObj = root.getJSONObject(i);
            JSONObject ownerObj = rootObj.getJSONObject("owner");

            repoName = rootObj.getString("name");
            description = rootObj.getString("description");
            owner = ownerObj.getString("login");
            repoUrl = rootObj.getString("html_url");
            ownerUrl = ownerObj.getString("html_url");

            Repo repo = new Repo(repoName, description, owner, repoUrl, ownerUrl);
            repoList.add(repo);
        }

        return repoList;
    }
}
