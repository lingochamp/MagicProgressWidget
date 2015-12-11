package com.liulishuo.magicprogress.demo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;

import com.liulishuo.magicprogress.demo.R;
import com.liulishuo.magicprogress.demo.widget.AnimTextView;
import com.liulishuo.magicprogress.library.widget.MagicProgressBar;
import com.liulishuo.magicprogress.library.widget.MagicProgressCircle;

import java.util.Random;

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

    private final Random random = new Random();

    private void anim() {
        final int score = random.nextInt(101);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(demoMpc, "percent", 0, score / 100f),
                ObjectAnimator.ofInt(demoTv, "score", 0, score),
                ObjectAnimator.ofFloat(demo1Mpb, "percent", 0, random.nextInt(101) / 100f),
                ObjectAnimator.ofFloat(demo2Mpb, "percent", 0, random.nextInt(101) / 100f),
                ObjectAnimator.ofFloat(demo3Mpb, "percent", 0, random.nextInt(101) / 100f),
                ObjectAnimator.ofFloat(demo4Mpb, "percent", 0, random.nextInt(101) / 100f)
        );
        set.setDuration(600);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();


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
        // TODO 添加github地址
//        Uri uri = Uri.parse(getString(R.string.app_github_url));
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
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

