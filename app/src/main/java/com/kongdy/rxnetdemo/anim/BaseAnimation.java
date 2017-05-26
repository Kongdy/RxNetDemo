package com.kongdy.rxnetdemo.anim;

import android.animation.Animator;
import android.view.View;

/**
 * @author kongdy
 * @date 2017-05-05 09:14
 * @TIME 9:14
 **/

public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
