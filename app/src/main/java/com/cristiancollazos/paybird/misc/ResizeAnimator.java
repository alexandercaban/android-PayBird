package com.cristiancollazos.paybird.misc;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimator extends Animation {

    private int nuTargetHeight;
    private View objView;
    private int nuStartHeight;

    public ResizeAnimator(View objView, int nuTargetHeight, int nuStartHeight) {
        this.objView = objView;
        this.nuTargetHeight = nuTargetHeight;
        this.nuStartHeight = nuStartHeight;
    }

    @Override
    protected void applyTransformation(float flInterpolatedTime, Transformation objTransformation) {
        int nuNewHeight = (int) (nuStartHeight + nuTargetHeight * flInterpolatedTime);
        objView.getLayoutParams().height = nuNewHeight;
        objView.requestLayout();
    }

    @Override
    public void initialize(int nuWidth, int nuHeight, int nuParentWidth, int nuParentHeight) {
        super.initialize(nuWidth, nuHeight, nuParentWidth, nuParentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

}
