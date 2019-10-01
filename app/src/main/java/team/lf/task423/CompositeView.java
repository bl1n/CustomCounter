package team.lf.task423;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CompositeView extends LinearLayout {

    private TextView mCurrentCountTV;
    private TextView mTotalCountTV;
    private int mCurrentTextColor;
    private int mHeight;
    private int mCurrentCount;
    private int mTotalCount;


    public CompositeView(Context context) {
        this(context, null);
    }

    public CompositeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.composite_view, this);


        TypedArray typedArray = context.getTheme().obtainStyledAttributes(R.styleable.CompositeView);
        mCurrentCount = typedArray.getInt(R.styleable.CompositeView_count, 5);
        setCurrentCountText(String.valueOf(mCurrentCount));
        mTotalCount = typedArray.getInt(R.styleable.CompositeView_total, 10);
//        setTotalCountText(mTotalCount);
        mCurrentCountTV = findViewById(R.id.currentCount);
        mTotalCountTV = findViewById(R.id.totalCount);
        mCurrentTextColor = mCurrentCountTV.getCurrentTextColor();

    }

    private void setCurrentCountText(String currentCountText) {
        if ((Integer.parseInt(currentCountText) <= mTotalCount) && (Integer.parseInt(currentCountText) >= 0))
            mCurrentCountTV.setText(currentCountText);
    }

    private void setTotalCountText(int totalCountText) {
        mTotalCountTV.setText(totalCountText);
    }

    public void changeCount(Boolean reverse) {
        final int currentValue = Integer.parseInt((String) mCurrentCountTV.getText());
        mHeight = mCurrentCountTV.getHeight();
        mHeight = reverse? -mHeight: mHeight;

        ObjectAnimator fadeToTransparent = ObjectAnimator.ofArgb(mCurrentCountTV, "textColor", mCurrentTextColor, Color.TRANSPARENT);
        ObjectAnimator fadeFromTransparent = ObjectAnimator.ofArgb(mCurrentCountTV, "textColor", Color.TRANSPARENT, mCurrentTextColor);
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
                int nextValue = reverse ? currentValue - 1 : currentValue + 1;
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
