package com.randev.elegantmediaassignment.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.randev.elegantmediaassignment.R;
import com.randev.elegantmediaassignment.model.Friend;
import com.randev.elegantmediaassignment.model.UserFriendListResponse;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private static final String TAG = FriendListActivity.class.getSimpleName();
    private ProfileTracker profileTracker;
    private GraphResponse graphResponse;
    private GraphRequest.Callback graphCallback;
    private RecyclerView friendListView;
    private List<Friend> friendList;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        friendListView = (RecyclerView) findViewById(R.id.list_friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        friendListView.setLayoutManager(linearLayoutManager);

        friendList = new ArrayList<>();

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                offset = totalItemsCount;
                getNextPage();
            }
        };
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(friendListView.getContext(),
                linearLayoutManager.getOrientation());
        friendListView.addOnScrollListener(scrollListener);
        FriendListAdapter friendListAdapter = new FriendListAdapter(friendList, FriendListActivity.this);
        friendListView.setAdapter(friendListAdapter);
        friendListView.addItemDecoration(dividerItemDecoration);

        initGraphRequestCallback();
        getFriendList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            LoginManager.getInstance().logOut();
            showLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Do nothing, since user should not be taken to login activity unless the user presses the logout button
    }

    private void getFriendList() {
        if (Profile.getCurrentProfile() == null) {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    Log.i(TAG, "Logged in as: " + profile2.getFirstName());
                    profileTracker.stopTracking();
                    requestFriendList(profile2);
                }
            };
        } else {
            Profile profile = Profile.getCurrentProfile();
            Log.i(TAG, "Logged in as: " + profile.getFirstName());
            requestFriendList(profile);
        }
    }

    private void initGraphRequestCallback() {
        graphCallback = new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                graphResponse = response;
                deserializeAndAddToList();

            }
        };
    }

    private void getNextPage() {
        GraphRequest requestForPagedResults = graphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        requestForPagedResults.setCallback(graphCallback);
        requestForPagedResults.executeAsync();

    }

    private void requestFriendList(Profile profile) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + profile.getId() + "/taggable_friends",
                null,
                HttpMethod.GET,
                graphCallback
        );
        request.executeAsync();
    }

    private void deserializeAndAddToList() {
        Gson gson = new Gson();
        if (graphResponse.getJSONObject() == null) {
            Toast.makeText(FriendListActivity.this, getString(R.string.looks_like_you_have_no_friends), Toast
                    .LENGTH_LONG).show();
            return;
        }
        UserFriendListResponse userFriendListResponse = gson.fromJson(graphResponse.getJSONObject().toString(), UserFriendListResponse
                .class);
        friendList.addAll(userFriendListResponse.getData());
        friendListView.getAdapter().notifyItemRangeChanged(offset, userFriendListResponse.getData().size());
        Log.i(TAG, "Friend List size : " + friendList.size());
    }

    private void showLogin() {
        Intent intent = new Intent(FriendListActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}
