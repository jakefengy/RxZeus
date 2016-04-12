package com.xm.zeus.view.contacts.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.xm.zeus.R;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.utils.Tip;
import com.xm.zeus.view.contacts.iview.IFriendEditView;
import com.xm.zeus.view.contacts.presenter.FriendEditPresenterImpl;
import com.xm.zeus.view.contacts.presenter.IFriendEditPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_Friend_Edit extends AppCompatActivity implements IFriendEditView {

    @Bind(R.id.toolbar_edit_friend)
    Toolbar toolbar;
    @Bind(R.id.et_friend_name)
    TextInputEditText etFriendName;
    @Bind(R.id.et_friend_phone)
    TextInputEditText etFriendPhone;
    @Bind(R.id.et_friend_email)
    TextInputEditText etFriendEmail;
    @Bind(R.id.et_friend_company_name)
    TextInputEditText etFriendCompanyName;
    @Bind(R.id.et_friend_department)
    TextInputEditText etFriendDepartment;
    @Bind(R.id.et_friend_post)
    TextInputEditText etFriendPost;
    @Bind(R.id.rl_action_bottom_save)
    RelativeLayout btnSave;
    @Bind(R.id.rl_action_bottom_delete)
    RelativeLayout btnDelete;
    @Bind(R.id.rl_action_bottom_edit)
    RelativeLayout btnEdit;

    private final static String ISADDFriend = "isAddFriend";
    private final static String FRIENDID = "friend_id";

    public static Intent getIntent(Context ctx, boolean isAddFriend, String friendId) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(ctx, Activity_Friend_Edit.class);
        intent.putExtra(ISADDFriend, isAddFriend);
        intent.putExtra(FRIENDID, friendId);

        return intent;
    }

    private boolean isAddFriend;
    private String friendId;

    private IFriendEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_edit);
        ButterKnife.bind(this);
        presenter = new FriendEditPresenterImpl(this, this);
        initViews();
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

        isAddFriend = getIntent().getBooleanExtra(ISADDFriend, true);
        friendId = getIntent().getStringExtra(FRIENDID);

        if (isAddFriend) {
            btnSave.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);

            presenter.getFriend(friendId);
        }

    }

    @OnClick({R.id.rl_action_bottom_save, R.id.rl_action_bottom_delete, R.id.rl_action_bottom_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_action_bottom_save:
                Friend friend = new Friend();
                friend.setUsername(String.valueOf(etFriendName.getText()));
                friend.setMobile(String.valueOf(etFriendPhone.getText()));
                friend.setEmail(String.valueOf(etFriendEmail.getText()));
                friend.setCompany(String.valueOf(etFriendCompanyName.getText()));
                friend.setDept(String.valueOf(etFriendDepartment.getText()));
                friend.setPost(String.valueOf(etFriendPost.getText()));
                presenter.addFriend(friend);
                break;
            case R.id.rl_action_bottom_delete:
                presenter.deleteFriend(friendId);
                break;
            case R.id.rl_action_bottom_edit:
                Friend friendUpdate = new Friend();
                friendUpdate.setUid(friendId);
                friendUpdate.setUsername(String.valueOf(etFriendName.getText()));
                friendUpdate.setMobile(String.valueOf(etFriendPhone.getText()));
                friendUpdate.setEmail(String.valueOf(etFriendEmail.getText()));
                friendUpdate.setCompany(String.valueOf(etFriendCompanyName.getText()));
                friendUpdate.setDept(String.valueOf(etFriendDepartment.getText()));
                friendUpdate.setPost(String.valueOf(etFriendPost.getText()));
                presenter.updateFriend(friendUpdate);
                break;
        }
    }

    @Override
    public void onGetComplete(Friend friend) {
        etFriendName.setText(friend.getUsername());
        etFriendPhone.setText(friend.getMobile());
        etFriendEmail.setText(friend.getEmail());
        etFriendCompanyName.setText(friend.getCompany());
        etFriendDepartment.setText(friend.getDept());
        etFriendPost.setText(friend.getPost());
    }

    @Override
    public void onAddComplete(Friend friend) {
        Tip.toast(Activity_Friend_Edit.this, "onAddComplete");
    }

    @Override
    public void onUpdateComplete(Friend friend) {
        Tip.toast(Activity_Friend_Edit.this, "onUpdateComplete");
    }

    @Override
    public void onDeleteComplete(String friendId) {
        Tip.toast(Activity_Friend_Edit.this, "onDeleteComplete");
    }

    @Override
    public void error(String msg) {
        Tip.toast(Activity_Friend_Edit.this, msg);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
