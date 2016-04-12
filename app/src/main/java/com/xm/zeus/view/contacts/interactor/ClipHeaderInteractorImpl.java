package com.xm.zeus.view.contacts.interactor;

import android.text.TextUtils;

import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.db.app.helper.UserHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.contacts.entity.UpdateAvatarResult;
import com.xm.zeus.view.contacts.entity.UploadAvatarResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lvxia on 2016-04-12.
 */
public class ClipHeaderInteractorImpl implements IClipHeaderInteractor {

    private UserHelper userHelper;
    private ColleagueHelper colleagueHelper;

    public ClipHeaderInteractorImpl() {
        userHelper = new UserHelper();
        colleagueHelper = new ColleagueHelper();
    }

    @Override
    public void updateAvatar(File file, ApiSubscriber<Colleague> subscriber) {

        final User user = userHelper.getLastLoggedUser();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        Network.getZeusApis().uploadAvatar(Constant.RequestUrl + "uploadavatar", user.getToken(), user.getOrg(), user.getUserId(), body)
                .subscribeOn(Schedulers.io())
                .compose(Network.<UploadAvatarResult>check())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<UploadAvatarResult, Observable<Colleague>>() {
                    @Override
                    public Observable<Colleague> call(final UploadAvatarResult result) {
                        if (result == null || TextUtils.isEmpty(result.getAvatarid())) {
                            return Observable.error(new Throwable("头像上传失败"));
                        } else {
                            return Network.getZeusApis().updateAvatar(user.getToken(), user.getUserId(), user.getOrg(), user.getUserId(), "0", result.getAvatarid())
                                    .compose(Network.<UpdateAvatarResult>check())
                                    .subscribeOn(Schedulers.io())
                                    .flatMap(new Func1<UpdateAvatarResult, Observable<Colleague>>() {
                                        @Override
                                        public Observable<Colleague> call(UpdateAvatarResult resultUpdate) {

                                            if (resultUpdate == null || !resultUpdate.isok()) {
                                                return Observable.error(new Throwable("头像更新失败"));
                                            } else {
                                                Colleague colleague = colleagueHelper.findById(user.getUserId());
                                                colleague.setAvatarid(result.getAvatarid());
                                                colleagueHelper.update(colleague);
                                                return Observable.just(colleague);
                                            }
                                        }
                                    });
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
