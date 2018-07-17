package tl.com.tlsl.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import tl.com.tlsl.R;
import tl.com.tlsl.utils.DensityUtils;

/**
 * Created by admin on 2017/10/17.
 */

public class CircleProgressView extends View{

    private static final String TAG = "CircleProgressBar";

    private int mMaxProgress = 350;

    private int mProgress = 50;

    private final int mCircleLineStrokeWidth = 12;

    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private  RectF mRectF;

    private Paint mPaint;

    private final Context mContext;

    private String mTxtHint1;

    private String mTxtHint2;
    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
//        mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
//        mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
//        mRectF.right = width - mCircleLineStrokeWidth / 2; // 左下角x
//        mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角y
        mRectF.left = DensityUtils.dp2px(mContext,25);
        mRectF.top = DensityUtils.dp2px(mContext,17);
//        mRectF.right = width -DensityUtils.dp2px(mContext,0);
        mRectF.right = width+DensityUtils.dp2px(mContext,5);
        mRectF.bottom = height -DensityUtils.dp2px(mContext,3);
        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(getResources().getColor(R.color.title_back_color));
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

        // 绘制进度文案显示
        mPaint.setStrokeWidth(mTxtStrokeWidth);
        String text = "已通过"+50;
        int textHeight = height / 4;
        mPaint.setTextSize(textHeight);
//        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtils.dp2px(mContext,23));
        canvas.drawText(text, DensityUtils.dp2px(mContext,46), height / 2 + textHeight / 2, mPaint);
//        if (!TextUtils.isEmpty(mTxtHint1)) {
//            mPaint.setStrokeWidth(mTxtStrokeWidth);
//            text = mTxtHint1;
//            textHeight = height / 8;
//            mPaint.setTextSize(textHeight);
//            mPaint.setColor(Color.rgb(0x99, 0x99, 0x99));
//            textWidth = (int) mPaint.measureText(text, 0, text.length());
//            mPaint.setStyle(Paint.Style.FILL);
//            canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
//        }
//
//        if (!TextUtils.isEmpty(mTxtHint2)) {
//            mPaint.setStrokeWidth(mTxtStrokeWidth);
//            text = mTxtHint2;
//            textHeight = height / 8;
//            mPaint.setTextSize(textHeight);
//            textWidth = (int) mPaint.measureText(text, 0, text.length());
//            mPaint.setStyle(Paint.Style.FILL);
//            canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
//        }
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

}
