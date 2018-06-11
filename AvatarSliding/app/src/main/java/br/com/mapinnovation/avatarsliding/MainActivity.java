package br.com.mapinnovation.avatarsliding;

import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

public class MainActivity extends AppCompatActivity {

    static public UnityPlayer mUnityPlayer;
    private FrameLayout layoutUnity;

    private ViewPager mSlideViewPger;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SlideAdapter slideAdapter;

    private ImageView mNextBtn;
    private ImageView mBackBtn;

    private TextView slideDescription;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Chama o avatar na tela
        layoutUnity = (FrameLayout) findViewById(R.id.avatar);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        mUnityPlayer.init(glesMode, false);
        mUnityPlayer.getSettings().getBoolean("orientation", false);
        mUnityPlayer.getSettings().getBoolean("hide_status_bar", false);

        layoutUnity.addView(mUnityPlayer.getView(), 0, lp);

        mUnityPlayer.UnitySendMessage("PlayerManager", "setOrientationLandScape", "");

        mSlideViewPger = (ViewPager) findViewById(R.id.slideViewPager);

        mNextBtn = (ImageView) findViewById(R.id.nextBtn);
        mBackBtn = (ImageView) findViewById(R.id.prevBtn);


        slideAdapter = new SlideAdapter(this);

        mSlideViewPger.setAdapter(slideAdapter);

        mSlideViewPger.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPger.setCurrentItem(mCurrentPage + 1);
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPger.setCurrentItem(mCurrentPage - 1);
            }
        });

        slideDescription = (TextView) findViewById(R.id.slide_desc);

    }

    public String[] slide_descriiption = {
            "Lorem ipsum dolor sit amet. ",
            "Consectetur adipiscing elit. ",
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
    };
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //addDotsIdicator(position);
            mCurrentPage = position;

            if (position == 0) {//Slide INÍCIO
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setEnabled(true);
            } else if (position == 3 - 1) {
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.INVISIBLE);
            } else {//Slide OUTROS
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
            }
            for (int i = position; i <= slide_descriiption.length; i++) {
                slideDescription.setText(slide_descriiption[position]);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void avatarSinaliza(View view) {
        sinalizarAvatar("Bom dia");
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
        mUnityPlayer.getSettings();
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    public static void sinalizarAvatar(String texto) {
        if (texto != null && texto.isEmpty() == false) {
            mUnityPlayer.UnitySendMessage("PlayerManager", "start_inputfield_web_play", texto);
//            mUnityPlayer.UnitySendMessage("PlayerManager", "setOrientationLandScape", texto);

            Log.e("sinalizarAvatar", "AvatarSinalizando");
            //setOrientationLandScape()
        }
    }

    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
         mUnityPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
           mUnityPlayer.quit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
