package com.jinlin.fourcloverview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

public class CloverView extends ViewGroup {

    public static final int VIEW_STATE_SQUARE = 0;

    public static final int VIEW_STATE_RECTANGLE = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_STATE_SQUARE, VIEW_STATE_RECTANGLE})
    public @interface ViewState {
    }

    private int mWidth = 0;
    private int mSpacing;
    private int mBottomCount;
    private int mResourceId;
    @ViewState
    private int mViewState = VIEW_STATE_SQUARE;

    public CloverView(Context context) {
        this(context, null);
    }

    public CloverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloverView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CloverView);
        try {
            mBottomCount = ta.getInt(R.styleable.CloverView_bottom_count, 2);
            mSpacing = ta.getDimensionPixelSize(R.styleable.CloverView_spacing, 0);
            mViewState = ta.getInt(R.styleable.CloverView_shape, mViewState);
            mResourceId = ta.getResourceId(R.styleable.CloverView_default_img, -1);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = mBottomCount + 1;
        ImageView iv;
        for (int i = 0; i < count; i++) {
            iv = new ImageView(getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LayoutParams params = new ViewGroup.MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            if (mResourceId != -1) {
                iv.setBackgroundResource(mResourceId);
            }
            addView(iv);
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        /*孩子必须是ImageView及其子类*/
        if (!(child instanceof ImageView)) {
            throw new IllegalArgumentException("CloverView's child must be a ImageView.");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalArgumentException();
        }
        int childCount = getChildCount();
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int smallChildWidth = (mWidth - mSpacing * (mBottomCount - 1) - paddingLeft - paddingRight) / mBottomCount;
        measure(paddingLeft, paddingTop, paddingRight, paddingBottom, smallChildWidth, childCount);
        switch (mViewState) {
            case VIEW_STATE_RECTANGLE:
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mWidth + mSpacing + smallChildWidth, MeasureSpec.EXACTLY));
                break;
            default:
            case VIEW_STATE_SQUARE: // 必须是方形才能坐位置变换
                //noinspection SuspiciousNameCombination
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                break;
        }
    }

    private void measure(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom, int smallChildWidth, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (i == 0) { // 左边和上部分为大图的时候以0判断
                switch (mViewState) {
                    case VIEW_STATE_RECTANGLE:
                        child.measure(MeasureSpec.makeMeasureSpec(mWidth - paddingLeft - paddingRight, MeasureSpec.EXACTLY),
                                MeasureSpec.makeMeasureSpec(mWidth - paddingTop - paddingBottom, MeasureSpec.EXACTLY));
                        break;
                    default:
                    case VIEW_STATE_SQUARE:
                        child.measure(MeasureSpec.makeMeasureSpec(mWidth - paddingLeft - paddingRight, MeasureSpec.EXACTLY),
                                MeasureSpec.makeMeasureSpec(mWidth - paddingLeft - paddingRight - smallChildWidth - mSpacing, MeasureSpec.EXACTLY));
                        break;
                }
            } else {
                child.measure(MeasureSpec.makeMeasureSpec(smallChildWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(smallChildWidth, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childR = (mWidth - mSpacing * (mBottomCount - 1) - paddingLeft - paddingRight) / mBottomCount; // 每个小的视图宽
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (i == 0) {
                child.layout(paddingLeft, paddingTop, mWidth - paddingRight,
                        mViewState == VIEW_STATE_SQUARE ? mWidth - paddingBottom - childR - mSpacing : mWidth - paddingTop);
            } else {
                paddingTop = mViewState == VIEW_STATE_SQUARE ? mWidth - paddingBottom - childR : mWidth - paddingBottom + mSpacing;
                child.layout(paddingLeft, paddingTop, paddingLeft + childR, mViewState == VIEW_STATE_SQUARE ? mWidth - paddingBottom : mWidth + mSpacing + childR - paddingBottom);
                paddingLeft += childR + mSpacing;
            }
        }
    }

    public void setImageUrls(String[] urls) {
        setImageUrls(Arrays.asList(urls));
    }

    public void setImageUrls(List<String> urls) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            Glide.with(getContext()).load(urls.get(i)).into((ImageView) view);
        }
    }
}
