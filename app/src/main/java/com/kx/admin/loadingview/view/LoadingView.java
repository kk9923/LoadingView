package com.kx.admin.loadingview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.kx.admin.loadingview.R;


public class LoadingView extends LinearLayout {
    private ShapeView mShapeView;// 上面的形状
    private View mShadowView;// 中间的阴影
    private int mTranslationDistance = 0;
    // 动画执行的时间
    private final long ANIMATOR_DURATION = 350;
    // 是否停止动画
    private boolean mIsStopAnimator = false;
    private AnimatorSet upAnimatorSet;
    private AnimatorSet fallAnimatorSet;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.ui_loading_view,this);
        mShapeView = (ShapeView) findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
       startFallAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (upAnimatorSet != null) {
            if (upAnimatorSet.isRunning()) {
                upAnimatorSet.cancel();
            }
            upAnimatorSet.removeAllListeners();
            for (Animator animator : upAnimatorSet.getChildAnimations()) {
                animator.removeAllListeners();
            }
            upAnimatorSet = null;
        }
        if (fallAnimatorSet != null) {
            if (fallAnimatorSet.isRunning()) {
                fallAnimatorSet.cancel();
            }
            fallAnimatorSet.removeAllListeners();
            for (Animator animator : fallAnimatorSet.getChildAnimations()) {
                animator.removeAllListeners();
            }
            fallAnimatorSet = null;
        }
        removeAllViews();
    }

    /**
     * 下落动画
     */
    public void startFallAnimation() {
        // 下落位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView,"translationY",0,dip2Px(80));
        // 配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView,"scaleX",1f,0.3f);
        fallAnimatorSet = new AnimatorSet();
        fallAnimatorSet.playTogether(translationAnimator,scaleAnimator);
        fallAnimatorSet.setDuration(500).setInterpolator(new FastOutSlowInInterpolator());
        fallAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShapeView.exchange();
                startUpAnimation();
            }
        });
        fallAnimatorSet.start();

    }

    /**
     * 上升动画
     */
    public void startUpAnimation() {
      //  PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("translationY",dip2Px(80),0);
     //   PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("rotation",0,180);

        // 上升位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView,"translationY",dip2Px(80),0);
        // 配合中间阴影变大
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView,"scaleX",0.3f,1f);
        // 旋转
        ObjectAnimator rotationAnimator = null;
        switch (mShapeView.getCurrentShape()){
            case CIRCLE:
            case RECTANGLE:
                // 180
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView,"rotation",0,180);
                break;
            case TRIANGLE:
                // 120
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView,"rotation",0,-120);
                break;
        }

        upAnimatorSet = new AnimatorSet();
        upAnimatorSet.playTogether(translationAnimator,scaleAnimator,rotationAnimator);

     //   ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mShapeView, holder1, holder2);
        upAnimatorSet.setDuration(500).setInterpolator(new DecelerateInterpolator());
        upAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startFallAnimation();
            }
        });
        upAnimatorSet.start();
    }
    private int dip2Px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }
}
