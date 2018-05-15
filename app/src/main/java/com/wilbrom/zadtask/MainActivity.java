package com.wilbrom.zadtask;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.AsyncTaskLoader;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wilbrom.zadtask.adapter.RepoAdapter;
import com.wilbrom.zadtask.model.Repo;
import com.wilbrom.zadtask.utilities.JsonUtils;
import com.wilbrom.zadtask.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        RepoAdapter.OnLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 11;

    private ArrayList<Repo> repoList;
    private RepoAdapter mAdapter;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new RepoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            private String data;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (data != null)
                    deliverResult(data);
                else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                URL url = null;
                String response = null;
                try {
                    url = new URL("https://api.github.com/users/square/repos");
                    response = NetworkUtils.getHttpResponse(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            public void deliverResult(String data) {
                this.data = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, data);

        try {
            repoList = JsonUtils.parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.setmRepoList(repoList);
        Log.d(TAG, "Length is: " + repoList.size());
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onLongClick(Repo repo) {
        Log.d(TAG, repo.getOwnerUrl());

        AlertDialog.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
            builder = new AlertDialog.Builder(this);
//        }
        builder.setTitle("Go to")
                .setView(getLayoutInflater().inflate(R.layout.dialog_layout, null))
                .setMessage("Repository url or owner url?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View alertLayout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                        RadioGroup radioGroup = (RadioGroup) alertLayout.findViewById(R.id.radio_group);

                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioRepoGroup = (RadioButton) alertLayout.findViewById(selectedId);

                    }
                })
                .show();
    }
}
