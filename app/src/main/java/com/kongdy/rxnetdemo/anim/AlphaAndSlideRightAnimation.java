package com.kongdy.rxnetdemo.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author kongdy
 * @date 2017-05-05 09:17
 * @TIME 9:17
 **/

public class AlphaAndSlideRightAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view,"alpha",0F,1F),
        ObjectAnimator.ofFloat(view,"translationX",-view.getMeasuredWidth()*0.5F,0.0F)};
    }
}
