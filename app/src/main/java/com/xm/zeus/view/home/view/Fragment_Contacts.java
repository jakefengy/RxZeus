package com.xm.zeus.view.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xm.zeus.R;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.extend.RecyclerViewFastScroller;
import com.xm.zeus.utils.Tip;
import com.xm.zeus.view.home.adapter.ColleagueAdapter;
import com.xm.zeus.view.home.iview.IContactsView;
import com.xm.zeus.view.home.presenter.ContactsPresenterImpl;
import com.xm.zeus.view.home.presenter.IContactsPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Fragment_Contacts extends Fragment implements IContactsView {

    @Bind(R.id.ll_name_card)
    LinearLayout llNameCard;
    @Bind(R.id.ll_talk_group)
    LinearLayout llTalkGroup;
    @Bind(R.id.ll_org)
    LinearLayout llOrg;
    @Bind(R.id.rv_colleague)
    RecyclerView recyclerView;
    @Bind(R.id.contacts_letter)
    RecyclerViewFastScroller fastScroller;

    // Presenter
    private IContactsPresenter presenter;

    private ColleagueAdapter adapter;

    public Fragment_Contacts() {

    }

    public static Fragment_Contacts newInstance() {
        return new Fragment_Contacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        presenter = new ContactsPresenterImpl(this);
        presenter.getContacts();
    }

    @Override
    public void loadContacts(List<Colleague> colleagueList) {
        adapter.updateSource(colleagueList);
    }

    @Override
    public void loadError(Throwable e) {
        Tip.toast(getActivity(), e.getMessage());
    }

    private void initView() {
        adapter = new ColleagueAdapter(getActivity(), null);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.handle_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

        adapter.setOnItemClickListener(new ColleagueAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Colleague colleague) {
                toDetial(colleague);
            }
        });
    }

    private void toDetial(Colleague colleague) {
        Tip.toast(getActivity(), "to person detail");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_name_card, R.id.ll_talk_group, R.id.ll_org})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_name_card:
                Tip.toast(getActivity(), "to friend");
                break;
            case R.id.ll_talk_group:
                Tip.toast(getActivity(), "to group");
                break;
            case R.id.ll_org:
                Tip.toast(getActivity(), "to org");
                break;
        }
    }
}
