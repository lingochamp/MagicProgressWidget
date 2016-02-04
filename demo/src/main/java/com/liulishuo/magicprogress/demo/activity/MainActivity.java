package com.liulishuo.magicprogress.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.liulishuo.magicprogress.demo.R;
import com.liulishuo.magicprogress.demo.widget.AnimTextView;
import com.liulishuo.magicprogresswidget.MagicProgressBar;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import java.util.Random;

import cn.dreamtobe.percentsmoothhandler.ISmoothTarget;

/**
 * Created by Jacksgong on 12/10/15.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        anim();
    }

    private boolean isAnimActive;
    private final Random random = new Random();

    private void anim() {
        final int ceil = 26;
        final int progress = random.nextInt(ceil);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(demoMpc, "percent", 0, progress / 100f),
                ObjectAnimator.ofInt(demoTv, "progress", 0, progress),
                ObjectAnimator.ofFloat(demo1Mpb, "percent", 0, random.nextInt(ceil) / 100f),
                ObjectAnimator.ofFloat(demo2Mpb, "percent", 0, random.nextInt(ceil) / 100f),
                ObjectAnimator.ofFloat(demo3Mpb, "percent", 0, random.nextInt(ceil) / 100f),
                ObjectAnimator.ofFloat(demo4Mpb, "percent", 0, random.nextInt(ceil) / 100f)
        );
        set.setDuration(600);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimActive = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimActive = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
    }


    public void onReRandomPercent(final View view) {
        if (isAnimActive) {
            return;
        }
        anim();
    }

    public void onClickIncreaseSmoothly(final View view) {
        if (isAnimActive) {
            return;
        }

        float mpcPercent = getIncreasedPercent(demoMpc);
        demoMpc.setSmoothPercent(mpcPercent);
        demoTv.setSmoothPercent(mpcPercent);
        demo1Mpb.setSmoothPercent(getIncreasedPercent(demo1Mpb));
        demo2Mpb.setSmoothPercent(getIncreasedPercent(demo2Mpb));
        demo3Mpb.setSmoothPercent(getIncreasedPercent(demo3Mpb));
        demo4Mpb.setSmoothPercent(getIncreasedPercent(demo4Mpb));
    }

    private float getIncreasedPercent(ISmoothTarget target) {
        float increasedPercent = target.getPercent() + 0.1f;

        return Math.min(1, increasedPercent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_github:
                openGitHub();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openGitHub() {
        Uri uri = Uri.parse(getString(R.string.app_github_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private MagicProgressCircle demoMpc;
    private AnimTextView demoTv;
    private MagicProgressBar demo1Mpb;
    private MagicProgressBar demo2Mpb;
    private MagicProgressBar demo3Mpb;
    private MagicProgressBar demo4Mpb;

    private void assignViews() {
        demoMpc = (MagicProgressCircle) findViewById(R.id.demo_mpc);
        demoTv = (AnimTextView) findViewById(R.id.demo_tv);
        demo1Mpb = (MagicProgressBar) findViewById(R.id.demo_1_mpb);
        demo2Mpb = (MagicProgressBar) findViewById(R.id.demo_2_mpb);
        demo3Mpb = (MagicProgressBar) findViewById(R.id.demo_3_mpb);
        demo4Mpb = (MagicProgressBar) findViewById(R.id.demo_4_mpb);
    }

}

