package com.zero.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.zero.ui.MainActivity;
import com.zero.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class WelcomeActivity extends AppCompatActivity {
    // 首次使用程序的显示的欢迎图片
    private int[] ids = { R.mipmap.wel_1,
            R.mipmap.wel_1, R.mipmap.wel_1,
            R.mipmap.wel_1 };

    SharedPreferences share;
    private List<View> guides = new ArrayList<View>();
    private ViewPager pager;
    private ImageView curDot;
    private int offset;
    private int curPos = 0;

    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = getSharedPreferences("showWelcomm", Context.MODE_PRIVATE);
        editor = share.edit();
        // 判断是否首次登录程序
        if (share.contains("shownum")) {
            setContentView(R.layout.activity_wel_com);
            int num = share.getInt("shownum", 0);
            editor.putInt("shownum", num++);
            editor.commit();
            skipActivity(1);
        } else {
            editor.putInt("shownum", 1);
            editor.commit();
            setContentView(R.layout.activity_wel_com);
            initView();
        }

    }

    private void initView() {
        for (int i = 0; i < ids.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(ids[i]);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            guides.add(iv);
        }
        curDot = (ImageView) findViewById(R.id.cur_dot);
        curDot.getViewTreeObserver().addOnPreDrawListener(
                new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        offset = curDot.getWidth();
                        return true;
                    }
                });

        WecommPagerAdapter adapter = new WecommPagerAdapter(guides);
        pager = (ViewPager) findViewById(R.id.showwelom_page);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                moveCursorTo(arg0);
                //if (arg0 == ids.length - 1) {// 到最后一张了
                   // skipActivity(2);
                //}
                curPos = arg0;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }

        });

    }

    private void moveCursorTo(int position) {
        TranslateAnimation anim = new TranslateAnimation(offset * curPos,
                offset * position, 0, 0);
        anim.setDuration(300);
        anim.setFillAfter(true);
        curDot.startAnimation(anim);
    }


    private void skipActivity(int min) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 1000*min);
    }

}
