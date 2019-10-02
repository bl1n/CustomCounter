package team.lf.task423;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CompositeView extends LinearLayout {
    private static final String TAG = "CompositeView";

    private TextView mCurrentCountTV;
    private TextView mTotalCountTV;
    private int mCurrentTextColor;
    private int mHeight;
    private String mCurrentCount;
    private String mTotalCount;
    private int mTextColor;
    private TextView mSlashTV;


    public CompositeView(Context context) {
        this(context, null);
    }

    public CompositeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.composite_view, this);
        mCurrentCountTV = findViewById(R.id.currentCount);
        mTotalCountTV = findViewById(R.id.totalCount);
        mSlashTV = findViewById(R.id.slash);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CompositeView, 0, 0);

        //changing total
        mTotalCount = typedArray.getString(R.styleable.CompositeView_total);
        if (!TextUtils.isEmpty(mTotalCount) && TextUtils.isDigitsOnly(mTotalCount)) {
            if (Integer.parseInt(mTotalCount) >= 0) {
                mTotalCountTV.setText(mTotalCount);
            }
        } else mTotalCountTV.setText("10");
        setCurrentCountText("5");

        //changing text color
        mTextColor = typedArray.getColor(R.styleable.CompositeView_textColor, Color.BLACK);
        mSlashTV.setTextColor(mTextColor);
        mTotalCountTV.setTextColor(mTextColor);
        mCurrentCountTV.setTextColor(mTextColor);


        //changing text style
        int typeFace = typedArray.getInt(R.styleable.CompositeView_textStyle, 0);
        setTypeFace(typeFace);

    }

    private void setTypeFace(int typeFace){
        mTotalCountTV.setTypeface(null, typeFace);
        mCurrentCountTV.setTypeface(null, typeFace);
        mSlashTV.setTypeface(null, typeFace);
    }

    private void setCurrentCountText(String currentCountText) {
        if ((Integer.parseInt(currentCountText) <= Integer.parseInt(mTotalCount)) && (Integer.parseInt(currentCountText) >= 0))
            mCurrentCountTV.setText(currentCountText);
    }


    public void changeCount(Boolean reverse) {
        final int currentValue = Integer.parseInt((String) mCurrentCountTV.getText());
        final int nextValue = reverse ? currentValue - 1 : currentValue + 1;

        mHeight = mCurrentCountTV.getHeight();
        mHeight = reverse ? -mHeight : mHeight;

        ObjectAnimator fadeToTransparent = ObjectAnimator.ofArgb(mCurrentCountTV, "textColor", mTextColor, Color.TRANSPARENT);
        ObjectAnimator fadeFromTransparent = ObjectAnimator.ofArgb(mCurrentCountTV, "textColor", Color.TRANSPARENT, mTextColor);
        AnimatorSet tcAnim = new AnimatorSet();
        tcAnim.playSequentially(fadeToTransparent, fadeFromTransparent);

        ObjectAnimator oldTextMove = ObjectAnimator.ofFloat(mCurrentCountTV, View.Y, 0, mHeight / 2);
        ObjectAnimator newTextMove = ObjectAnimator.ofFloat(mCurrentCountTV, View.Y, -mHeight / 2, 0);


        oldTextMove.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setCurrentCountText(String.valueOf(currentValue));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setCurrentCountText(String.valueOf(nextValue));
            }
        });
        AnimatorSet textAnim = new AnimatorSet();
        textAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        textAnim.playSequentially(oldTextMove, newTextMove);
        AnimatorSet overallAnim = new AnimatorSet();
        overallAnim.setDuration(250).playTogether(tcAnim, textAnim);
        overallAnim.start();
    }
}
