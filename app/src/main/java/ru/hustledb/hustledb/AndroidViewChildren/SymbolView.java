package ru.hustledb.hustledb.AndroidViewChildren;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sergey on 04.03.17.
 */

public class SymbolView extends android.support.v7.widget.AppCompatImageView {

    private char symbol = 'S';
    private final Paint textPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getWidth();
        int height = this.getHeight();
        int fontSize = Math.min(width, height) * 5 / 8;
        textPaint.setTextSize(fontSize);
        canvas.drawText(String.valueOf(symbol), width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
    }
    public void setSymbol(char symbol){
        this.symbol = symbol;
        this.invalidate();
    }
    public SymbolView(Context context) {
        this(context, null);
    }
    public SymbolView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SymbolView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SymbolView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                     int defStyleRes) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(2);
    }
}
