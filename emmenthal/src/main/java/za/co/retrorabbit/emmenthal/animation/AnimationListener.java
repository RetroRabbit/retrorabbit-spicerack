package za.co.retrorabbit.emmenthal.animation;

/**
 * Created by Werner Scheffer on 14/10/16.
 */
public interface AnimationListener {

    /**
     * We need to make MaterialIntroView visible
     * before fade in animation starts
     */
    interface OnAnimationStartListener{
        void onAnimationStart();
    }

    /**
     * We need to make MaterialIntroView invisible
     * after fade out animation ends.
     */
    interface OnAnimationEndListener{
        void onAnimationEnd();
    }

}
