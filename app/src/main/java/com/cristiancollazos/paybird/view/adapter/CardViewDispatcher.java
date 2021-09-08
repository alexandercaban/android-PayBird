package com.cristiancollazos.paybird.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemDispatchedListener;

public class CardViewDispatcher extends ItemTouchHelper.SimpleCallback {

    private Context objContext;
    private OnRecyclerItemDispatchedListener objListener;
    private int nuIdDrawable;
    private int nuIdColor;

    public CardViewDispatcher(Context objContext, OnRecyclerItemDispatchedListener objListener,
                              int nuIdDrawable, int nuIdColor) {
        super(0, ItemTouchHelper.LEFT);
        this.objContext = objContext;
        this.objListener = objListener;
        this.nuIdDrawable = nuIdDrawable;
        this.nuIdColor = nuIdColor;
    }

    @Override
    public boolean onMove(RecyclerView objRecyclerView, RecyclerView.ViewHolder objViewHolder,
                          RecyclerView.ViewHolder objViewHolderTarget) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder objViewHolder, int nuDirection) {
        if (nuDirection == ItemTouchHelper.LEFT) {
            int nuPosition = objViewHolder.getAdapterPosition();
            objListener.OnItemDispatched(nuPosition);
        }
    }

    @Override
    public void onChildDraw(Canvas objCanvas, RecyclerView objRecyclerView,
                            RecyclerView.ViewHolder objViewHolder, float flDX, float flDY,
                            int nuActionState, boolean blIsCurrentlyActive) {
        Bitmap objIcon;

        if (nuActionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View objView = objViewHolder.itemView;
            float flHeight = (float) objView.getBottom() - (float) objView.getTop();
            float flWidth = flHeight / 3;

            if (flDX < 0) {
                Paint objPaint = new Paint();
                objPaint.setColor(objContext.getResources().getColor(nuIdColor));
                objIcon = BitmapFactory.decodeResource(objContext.getResources(), nuIdDrawable);

                RectF objBackground = new RectF(objView.getRight() + flDX, (float) objView.getTop(),
                        (float) objView.getRight(), (float) objView.getBottom());
                objCanvas.drawRect(objBackground, objPaint);

                RectF objIconBackground = new RectF((objView.getRight() - (2 * flWidth)),
                        objView.getTop() + flWidth, objView.getRight() - flWidth,
                        objView.getBottom() - flWidth);
                objCanvas.drawBitmap(objIcon, null, objIconBackground, objPaint);
            }
        }

        super.onChildDraw(objCanvas, objRecyclerView, objViewHolder, flDX, flDY, nuActionState,
                blIsCurrentlyActive);
    }

}
