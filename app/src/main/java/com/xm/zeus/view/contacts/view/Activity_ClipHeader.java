package com.xm.zeus.view.contacts.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.xm.zeus.R;
import com.xm.zeus.extend.ClipView;
import com.xm.zeus.utils.BitmapUtil;
import com.xm.zeus.utils.Tip;
import com.xm.zeus.view.contacts.iview.IClipHeaderView;
import com.xm.zeus.view.contacts.presenter.ClipHeaderPresenterImpl;
import com.xm.zeus.view.contacts.presenter.IClipHeaderPresenter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;


/**
 * 头像截取
 */
public class Activity_ClipHeader extends AppCompatActivity implements View.OnTouchListener, IClipHeaderView {

    private final static String UPDATE_USER_ID = "";

    public static Intent getIntent(Context ctx, Uri imageUri, String userId) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(ctx, Activity_ClipHeader.class);
        intent.setData(imageUri);
        intent.putExtra(UPDATE_USER_ID, userId);

        return intent;
    }

    private String TAG = Activity_ClipHeader.class.getName();

    @Bind(R.id.src_pic)
    ImageView srcPic;
    @Bind(R.id.rl_action_bottom_chat)
    View bt_ok;
    @Bind(R.id.clipView)
    ClipView clipview;
    @Bind(R.id.toolbar_clip_header)
    Toolbar toolbar;
    private MaterialDialog dialog;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    /**
     * 动作标志：无
     */
    private static final int NONE = 0;
    /**
     * 动作标志：拖动
     */
    private static final int DRAG = 1;
    /**
     * 动作标志：缩放
     */
    private static final int ZOOM = 2;
    /**
     * 初始化动作标志
     */
    private int mode = NONE;

    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;

    private Bitmap bitmap;

    private int side_length;//裁剪区域边长

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipheader);
        ButterKnife.bind(this);
        presenter = new ClipHeaderPresenterImpl(this, this);
        initToolBar();
        init();

    }

    private void initToolBar() {
        toolbar.setTitle("选择头像");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {

        side_length = getResources().getDimensionPixelOffset(R.dimen.clipheader_radius);
        srcPic.setOnTouchListener(this);

        //clipview中有初始化原图所需的参数，所以需要等到clipview绘制完毕再初始化原图
        ViewTreeObserver observer = clipview.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                clipview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initSrcPic();
            }
        });

    }

    /**
     * 初始化图片
     * step 1: decode 出 720*1280 左右的照片  因为原图可能比较大 直接加载出来会OOM
     * step 2: 将图片缩放 移动到imageView 中间
     */
    private void initSrcPic() {
        Uri uri = getIntent().getData();
        String path = BitmapUtil.getRealFilePathFromUri(getApplicationContext(), uri);
        if (TextUtils.isEmpty(path)) {
            return;
        }
        //原图可能很大，现在手机照出来都3000*2000左右了，直接加载可能会OOM
        //这里 decode 出 720*1280 左右的照片
        bitmap = BitmapUtil.decodeSampledBitmap(path, 720, 1280);

        if (bitmap == null) {
            return;
        }


        //图片的缩放比
        float scale;
        if (bitmap.getWidth() > bitmap.getHeight()) {//宽图
            scale = (float) srcPic.getWidth() / bitmap.getWidth();

            //如果高缩放后小于裁剪区域 则将裁剪区域与高的缩放比作为最终的缩放比
            Rect rect = clipview.getClipRect();
            float minScale = rect.height() / bitmap.getHeight();//高的最小缩放比
            if (scale < minScale) {
                scale = minScale;
            }
        } else {//高图
            scale = (float) srcPic.getWidth() / 2 / bitmap.getWidth();//宽缩放到imageview的宽的1/2
        }

        // 缩放
        matrix.postScale(scale, scale);

        // 平移   将缩放后的图片平移到imageview的中心
        int midX = srcPic.getWidth() / 2;//imageView的中心x
        int midY = srcPic.getHeight() / 2;//imageView的中心y
        int imageMidX = (int) (bitmap.getWidth() * scale / 2);//bitmap的中心x
        int imageMidY = (int) (bitmap.getHeight() * scale / 2);//bitmap的中心y
        matrix.postTranslate(midX - imageMidX, midY - imageMidY);

        srcPic.setScaleType(ImageView.ScaleType.MATRIX);
        srcPic.setImageMatrix(matrix);
        srcPic.setImageBitmap(bitmap);

    }

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 获取缩放后的截图
     * 1.截取裁剪框内bitmap
     * 2.将bitmap缩放到宽高为side_length
     *
     * @return
     */
    private Bitmap getZoomedCropBitmap() {

        srcPic.setDrawingCacheEnabled(true);
        srcPic.buildDrawingCache();

        Rect rect = clipview.getClipRect();

        Bitmap cropBitmap = null;
        Bitmap zoomedCropBitmap = null;
        try {
            cropBitmap = Bitmap.createBitmap(srcPic.getDrawingCache(), rect.left, rect.top, rect.width(), rect.height());
            zoomedCropBitmap = BitmapUtil.zoomBitmap(cropBitmap, side_length, side_length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cropBitmap != null) {
            cropBitmap.recycle();
        }

        // 释放资源
        srcPic.destroyDrawingCache();

        return zoomedCropBitmap;
    }

    @OnClick(R.id.rl_action_bottom_chat)
    public void onClick(View view) {
        generateUriAndReturn();
    }

    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        Bitmap zoomedCropBitmap = getZoomedCropBitmap();
        if (zoomedCropBitmap == null) {
            Log.e(TAG, "zoomedCropBitmap == null");
            return;
        }

        File cropFile = new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg");
        Uri mSaveUri = Uri.fromFile(cropFile);

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                // TODO: report error to caller
                Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (cropFile.exists()) {
                presenter.updateAvatar(cropFile);
            }
        }
    }

    // 上传头像
    private IClipHeaderPresenter presenter;

    @Override
    public void onUpdateStart() {
        dialog = new MaterialDialog(this)
                .setTitle("正在上传头像，请稍等...")
                .setCanceledOnTouchOutside(false);

        dialog.show();
    }

    @Override
    public void onError(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Tip.toast(Activity_ClipHeader.this, msg);
    }

    @Override
    public void onComplete() {
        if (dialog != null) {
            dialog.dismiss();
        }

        Tip.toast(Activity_ClipHeader.this, "头像已更新");
        finish();

    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

}
