package com.zhangyf.bidirectio.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.zhangyf.bidirectio.net.BaseResponse;
import com.zhangyf.bidirectio.net.Optional;
import com.zhangyf.bidirectio.net.transformer.RemoteTransformer;
import com.zhangyf.bidirectio.views.MLoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by zhangyf on 2016/12/30.
 * fragment基类
 */

public abstract class BaseFragment extends Fragment implements LifecycleProvider<FragmentEvent> {

    protected final String TAG = getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    /**
     * 是否第一次可见
     */
    private boolean isFirstVisible = true;
    /**
     * 标志位，标志View已经初始化完成。
     */
    private boolean isPrepared;
    protected BaseActivity mActivity;
    private Unbinder unBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
        mActivity = (BaseActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = null;
        if (getContentViewLayoutID() != 0) {
            contentView = inflater.inflate(getContentViewLayoutID(), null);
            unBinder = ButterKnife.bind(this, contentView);
            return contentView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 非懒加载UI初始化
        onInitFastView();
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 注册EventBus
        if (shouldBindEvent()) {
            EventBus.getDefault().register(this);
        }
        lazyLoad();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 非懒加载数据初始化
        onInitFastData();
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    protected void otherViewInit() {

    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
        unBinder.unbind();
        if (shouldBindEvent()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                isFirstVisible = false;
                lazyLoad();
            } else {
                onRepeatLazyLoad();
            }
        } else {
            onUserInvisible();
        }
    }

    protected void lazyLoad() {
        if (isPrepared) {
            onInitLazyData();
        } else {
            isPrepared = true;
        }
    }

    protected void showLoadingPop(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            MLoadingDialog.showLoadingDialog(activity);
        }
    }

    protected void dismissLoadingPop() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            MLoadingDialog.cancelAllLoadingDialog();
        }
    }

    protected <Z extends BaseResponse.ErrorResponse> ObservableTransformer<Optional<Z>, Optional<Z>> applyProgressBar() {
        return ((BaseActivity) getActivity()).applyProgressBar();
    }

    public <Z extends BaseResponse.ErrorResponse> RemoteTransformer<Z> applyLoading() {
        return ((BaseActivity) getActivity()).applyLoading();
    }

    protected abstract void onInitLazyData();

    protected abstract void onInitFastData();

    protected abstract void onInitFastView();

    protected abstract int getContentViewLayoutID();

    protected boolean shouldBindEvent() {
        return false;
    }

    protected void onRepeatLazyLoad() {
    }

    protected void onUserInvisible() {
    }

    protected void initPrePageLoader() {
    }


    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

}
