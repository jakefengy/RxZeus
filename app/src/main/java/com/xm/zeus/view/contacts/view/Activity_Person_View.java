package com.xm.zeus.view.contacts.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xm.zeus.R;
import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.ColleagueDept;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.utils.Tip;
import com.xm.zeus.view.contacts.iview.IPersonView;
import com.xm.zeus.view.contacts.presenter.IPersonPresenter;
import com.xm.zeus.view.contacts.presenter.PersonPresenterImpl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_Person_View extends AppCompatActivity implements IPersonView {

    @Bind(R.id.toolbar_personal_info)
    Toolbar toolbar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_department)
    TextView tvDepartment;
    @Bind(R.id.ly_usercard_phone)
    LinearLayout lyUsercardPhone;
    @Bind(R.id.tv_usercard_phone)
    TextView tvUsercardPhone;
    @Bind(R.id.tv_usercard_email)
    TextView tvUsercardEmail;
    @Bind(R.id.ly_usercard_email)
    LinearLayout lyUsercardEmail;
    @Bind(R.id.tv_usercard_company)
    TextView tvUsercardCompany;
    @Bind(R.id.tv_usercard_address)
    TextView tvUsercardAddress;
    @Bind(R.id.rl_action_bottom_chat)
    RelativeLayout btnChat;
    @Bind(R.id.rl_action_bottom_history)
    RelativeLayout btnHistory;
    @Bind(R.id.rl_action_bottom_delete)
    RelativeLayout btnDelete;
    @Bind(R.id.rl_action_bottom_edit)
    RelativeLayout btnEdit;
    @Bind(R.id.rl_usercard_company)
    RelativeLayout lyCompany;
    @Bind(R.id.iv_avatar)
    SimpleDraweeView ivAvatar;

    private final static String DATAFROM = "DATAFROM";
    private final static String DATAID = "DATAID";

    @OnClick({R.id.ly_usercard_phone, R.id.ly_usercard_email, R.id.rl_action_bottom_chat, R.id.rl_action_bottom_history, R.id.rl_action_bottom_delete, R.id.rl_action_bottom_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_usercard_phone:
                Tip.toast(Activity_Person_View.this, "打电话");
                break;
            case R.id.ly_usercard_email:
                Tip.toast(Activity_Person_View.this, "发邮件");
                break;
            case R.id.rl_action_bottom_chat:
                Tip.toast(Activity_Person_View.this, "聊天");
                break;
            case R.id.rl_action_bottom_history:
                Tip.toast(Activity_Person_View.this, "聊天记录");
                break;
            case R.id.rl_action_bottom_delete:
                presenter.deleteFriend(personId);
                break;
            case R.id.rl_action_bottom_edit:
                Intent intent = Activity_Friend_Edit.getIntent(Activity_Person_View.this, false, personId);
                startActivity(intent);
                break;
        }
    }

    public enum Type {
        Friend, Colleague
    }

    public static Intent getIntent(Context context, Type type, String personId) {
        Intent intent = new Intent(context, Activity_Person_View.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DATAID, personId);
        if (type == Type.Friend) {
            intent.putExtra(DATAFROM, Type.Friend.name());
        } else if (type == Type.Colleague) {
            intent.putExtra(DATAFROM, Type.Colleague.name());
        }
        return intent;
    }

    private Type dataType;
    private String personId;
    private IPersonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_view);
        ButterKnife.bind(this);

        String txtType = getIntent().getStringExtra(DATAFROM);
        if (txtType.equals(Type.Friend.name())) {
            dataType = Type.Friend;
        } else {
            dataType = Type.Colleague;
        }

        personId = getIntent().getStringExtra(DATAID);
        presenter = new PersonPresenterImpl(this, this);

        initView();

    }

    private void initView() {

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (dataType == Type.Friend) {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            lyCompany.setVisibility(View.GONE);

            presenter.getFriend(personId);

        } else {
            btnChat.setVisibility(View.VISIBLE);
            btnHistory.setVisibility(View.VISIBLE);

            presenter.getColleague(personId);
        }
    }

    @Override
    public void onGetFriendComplete(Friend friend) {
        tvName.setText(friend.getUsername());
        tvDepartment.setText(friend.getDept());
        tvUsercardPhone.setText(friend.getMobile());
        tvUsercardEmail.setText(friend.getEmail());
        tvUsercardCompany.setText(friend.getCompany());

        Uri imageUri = Uri.parse(Constant.ImageUrl + friend.getAvatarid());
        ivAvatar.setImageURI(imageUri);

    }

    @Override
    public void onDeleteFriendComplete() {
        Tip.toast(Activity_Person_View.this, "onDeleteFriendComplete");
    }

    @Override
    public void onGetColleagueComplete(Colleague colleague) {
        tvName.setText(colleague.getUsername());
        ColleagueDept dept = new ColleagueHelper().getDefaultDeptByPersonId(colleague.getUid());
        if (dept != null) {
            tvDepartment.setText(dept.getName());
        }
        tvUsercardPhone.setText(colleague.getMobile());
        tvUsercardEmail.setText(colleague.getEmail());

        Uri imageUri = Uri.parse(Constant.ImageUrl + colleague.getAvatarid());
        ivAvatar.setImageURI(imageUri);
    }

    @Override
    public void onError(String msg) {
        Tip.toast(Activity_Person_View.this, msg);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
