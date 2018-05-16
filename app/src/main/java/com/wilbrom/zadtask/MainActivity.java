package com.wilbrom.zadtask;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.AsyncTaskLoader;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.wilbrom.zadtask.adapter.RepoAdapter;
import com.wilbrom.zadtask.model.Repo;
import com.wilbrom.zadtask.utilities.JsonUtils;
import com.wilbrom.zadtask.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        RepoAdapter.OnRepoItemInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 11;

    private ArrayList<Repo> mCompleteRepoList;
    private ArrayList<Repo> mRepoList = new ArrayList<>();
    private RepoAdapter mAdapter;

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private int mCurrentLastIndex = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new RepoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.setmRepoList(null);
                mCurrentLastIndex = 9;
                mRepoList.clear();
                getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
            }
        });

        if (savedInstanceState != null) {
            mCurrentLastIndex = savedInstanceState.getInt(Intent.EXTRA_TEXT);
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Intent.EXTRA_TEXT, mCurrentLastIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            private String data;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                Log.d(TAG, "onstartloading");
                if (data != null) {
                    Log.d(TAG, "data not null");
                    deliverResult(data);
                } else {
                    Log.d(TAG, "data null");
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
                } catch (Exception e) {
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
        mProgressBar.setVisibility(View.INVISIBLE);
        mRefreshLayout.setRefreshing(false);
        try {
            mCompleteRepoList = JsonUtils.parseJson(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRepoList.addAll(mCompleteRepoList.subList(0, mCurrentLastIndex));
        mAdapter.setmRepoList(mRepoList);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onLongClick(final Repo repo) {
        final ArrayList<Integer> itemClicked = new ArrayList<>();
        itemClicked.add(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go to URL")
                .setSingleChoiceItems(new String[]{"Repository", "Owner"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemClicked.clear();
                        itemClicked.add(i);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (itemClicked.get(0) == 0) {
                            intent.setData(Uri.parse(repo.getRepoUrl()));
                        } else {
                            intent.setData(Uri.parse(repo.getOwnerUrl()));
                        }
                        startActivity(intent);
                    }}).show();
    }

    @Override
    public boolean onLoadMoreData() {
        if (mCurrentLastIndex < mCompleteRepoList.size() - 2) {
            mRepoList.addAll(mCompleteRepoList.subList(mCurrentLastIndex, mCurrentLastIndex + 10));
            mAdapter.setmRepoList(mRepoList);
            mCurrentLastIndex = mCurrentLastIndex + 10;
            return true;
        }
        return false;
    }
}
