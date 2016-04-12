package com.xm.zeus.view.contacts.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xm.zeus.R;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.extend.RecyclerViewFastScroller;
import com.xm.zeus.utils.Tip;
import com.xm.zeus.view.contacts.adapter.FriendAdapter;
import com.xm.zeus.view.contacts.iview.IFriendView;
import com.xm.zeus.view.contacts.presenter.FriendPresenterImpl;
import com.xm.zeus.view.contacts.presenter.IFriendPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_Friends extends AppCompatActivity implements IFriendView {


    @Bind(R.id.friend_toolbar)
    Toolbar toolbar;
    @Bind(R.id.friend_list)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.friend_fast_scroller)
    RecyclerViewFastScroller friendFastScroller;
    // view
    private FriendAdapter adapter;
    private IFriendPresenter presenter;

    public static Intent getIntent(Context ctx) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(ctx, Activity_Friends.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);

        initViews();

        presenter = new FriendPresenterImpl(this, this);
        presenter.getFriends();
    }

    private void initViews() {

        toolbar.setTitle("名片夹");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new FriendAdapter(Activity_Friends.this, null);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_Friends.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        friendFastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                friendFastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        friendFastScroller.setRecyclerView(recyclerView);
        friendFastScroller.setViewsToUse(R.layout.handle_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                presenter.refreshFriends();
            }
        });

        adapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Friend friend) {
                Tip.toast(Activity_Friends.this, "To friend detail");
            }
        });

    }

    @Override
    public void onGetFriendComplete(List<Friend> friends) {
        adapter.updateSource(friends);
    }

    @Override
    public void onRefreshComplete(List<Friend> friends) {
        adapter.updateSource(friends);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshFail() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void error(String msg) {
        Tip.toast(Activity_Friends.this, msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Tip.toast(Activity_Friends.this, "add friend");
        return true;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
