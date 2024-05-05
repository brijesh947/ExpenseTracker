package com.example.splitwise.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.example.splitwise.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        try {
            tvContent.setText("" + ((PieEntry) (e)).getLabel() + " " + e.getY());
            // set the entry-value as the display text
            super.refreshContent(e, highlight);

        } catch (Exception exception) {
            Log.d("sdfvfr", "exception in markerView " + exception.getMessage());
        }
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        try {
            if (mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }
        } catch (Exception e) {
            Log.d("sdfvfr", "getOffset: " + e.getMessage());
        }

        return mOffset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
