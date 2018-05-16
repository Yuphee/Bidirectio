package com.zhangyf.bidirectio.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.zhangyf.bidirectio.net.BaseResponse;
import com.zhangyf.bidirectio.net.Optional;
import com.zhangyf.bidirectio.net.transformer.RemoteTransformer;
import com.zhangyf.bidirectio.views.MLoadingDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;


/**
 * Created by zhangyf on 2017/11/19.
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {

    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder mUnbinder = null;
    public boolean shouldInterceptKeyboard = false;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariablesAndDealExtras(getIntent().getExtras(), savedInstanceState);
        if (isAllowFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (isSteepStatusBar()) {
            StatusBarUtil.setTranslucent(this);
        } else {
            StatusBarUtil.setColor(this, getStatusBarColor() == 0 ?
                    ContextCompat.getColor(this, android.R.color.white) : ContextCompat.getColor(this, getStatusBarColor()), 40);
        }

        BaseAppManager.getInstance().addActivity(this);

        if (shouldBindEvent()) {
            EventBus.getDefault().register(this);
        }

        //checkSecret();

        onCreateView();
    }


    private void onCreateView() {
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            mUnbinder = ButterKnife.bind(this);
            initViews();
        } else {
            Log.e(TAG, "You must set a layoutID for activity first!");
            throw new IllegalArgumentException("You must set a layoutID for activity first!");
        }
    }


    protected Context getContext() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        /*if (!MCache.isCurrentRunningForeground) {
            //切换到前台
            long leaveTime = System.currentTimeMillis() - UserInfo.getLeaveTime();
            if (leaveTime > 5 * 60 * 1000) {
                if (UserInfo.getSecretState() && !UserInfo.getSecretPswd().equals("")) {
                    readyGo(InputSecretActivity.class);
                }
            }
        }*/
    }


    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }


    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
        if (shouldBindEvent()) {
            EventBus.getDefault().unregister(this);
        }
        BaseAppManager.getInstance().removeActivity(this);
    }


    /**
     * 此方法合并了RemoteTransformer带有loading页面的请求
     *
     * @param <Z>
     * @return the composer of error checker and scheduler.
     */
    public <Z extends BaseResponse.ErrorResponse> RemoteTransformer<Z> applyLoading() {
        return new RemoteTransformer<Z>(this) {
            @Override
            public Observable<Optional<Z>> apply(Observable<BaseResponse<Z>> observable) {
                return super.apply(observable).compose(BaseActivity.this.<Z>applyProgressBar());
            }
        };
    }

    /**
     * 带有loading页面的请求，可以单独调用
     *
     * @param <Z>
     * @return
     */
    public <Z extends BaseResponse.ErrorResponse> ObservableTransformer<Optional<Z>, Optional<Z>> applyProgressBar() {
        return new ObservableTransformer<Optional<Z>, Optional<Z>>() {
            @Override
            public Observable<Optional<Z>> apply(@NonNull Observable<Optional<Z>> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("zhangyf", "currentThread:" + Thread.currentThread().getName());
                        MLoadingDialog.showLoadingDialog(BaseActivity.this);
                    }

                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        MLoadingDialog.cancelAllLoadingDialog();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MLoadingDialog.cancelAllLoadingDialog();
                    }
                });
            }

        };
    }


    /**
     * startActivity
     *
     * @param clazz
     */
    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    public void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    public void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * [是否允许全屏]
     */
    public boolean isAllowFullScreen() {
        return false;
    }

    /**
     * [是否设置沉浸状态栏]
     */
    protected boolean isSteepStatusBar() {
        return false;
    }

    /**
     * [设置状态栏颜色]
     */
    @ColorRes
    protected int getStatusBarColor() {
        return 0;
    }


    /**
     * [获取layoutID]
     *
     * @return
     */
    protected abstract int getContentViewLayoutID();

    /**
     * [处理传递过来的参数,现场恢复，并初始化变量]
     *
     * @param extras
     * @param savedInstanceState 实际使用得判空
     */
    private void initVariablesAndDealExtras(Bundle extras, Bundle savedInstanceState) {
        if (extras == null) {
            initVariables(new Bundle(), savedInstanceState);
        } else {
            initVariables(extras, savedInstanceState);
        }
    }

    /**
     * [初始化数据]
     */
    protected abstract void initVariables(Bundle extras, Bundle savedInstanceState);

    /**
     * [初始化view]
     */
    protected abstract void initViews();


    /**
     * [是否需要绑定EventBus]
     *
     * @return
     */
    protected abstract boolean shouldBindEvent();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!shouldInterceptKeyboard && ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

}
