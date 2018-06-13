package br.com.mapinnovation.avatarsliding;

import android.content.pm.ActivityInfo;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

import java.text.Normalizer;

public class MainActivity extends AppCompatActivity {

    public UnityPlayer mUnityPlayer;
    private FrameLayout layoutUnity;

    private ViewPager mSlideViewPger;

    private SlideAdapter slideAdapter;

    private ImageView mNextBtn;
    private ImageView mBackBtn;

    private TextView slideDescription;
    private TextView pager;

    private int mCurrentPage;
    Toolbar toolbar;

    private int contagem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        toolbar = (Toolbar) findViewById(R.id.tooblarId);
        toolbar.setTitle("Curso de Desenvolvimento Web");

        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));

        //Chama o avatar na tela
        layoutUnity = (FrameLayout) findViewById(R.id.avatar);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        mUnityPlayer.init(glesMode, false);
        mUnityPlayer.UnitySendMessage("PlayerManager", "setOrientationLandScape", "");
        mUnityPlayer.getSettings().getBoolean("orientation", false);
        mUnityPlayer.getSettings().getBoolean("hide_status_bar", false);

        layoutUnity.addView(mUnityPlayer.getView(), 0, lp);

        mNextBtn = (ImageView) findViewById(R.id.nextBtn);
        mBackBtn = (ImageView) findViewById(R.id.prevBtn);

        mSlideViewPger = (ViewPager) findViewById(R.id.slideViewPager);
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
        pager = (TextView) findViewById(R.id.pager);
        pager.setText(mCurrentPage + 1 + "/" + (slide_description.length));

        if (mSlideViewPger.getCurrentItem() == 0) {
            slideDescription.setText(slide_description[0]);
        }

    }

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
                mNextBtn.setVisibility(View.VISIBLE);
            } else if (position == slide_description.length - 1) {
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setEnabled(false);
                mNextBtn.setVisibility(View.INVISIBLE);
            } else {//Slide OUTROS
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
            }
            for (int i = position; i <= slide_description.length; i++) {
                slideDescription.setText(slide_description[position]);
                pager.setText(position + 1 + "/" + (slide_description.length));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                finish();
                break;
            case R.id.about_menu:
                Toast.makeText(this, "Curso de Desenvolvimento Web.", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void avatarSinaliza(View view) {
        int index = mSlideViewPger.getCurrentItem();
        sinalizarAvatar(removerAcentos(slide_description[index]));
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
        mUnityPlayer.getSettings();
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    public void sinalizarAvatar(String texto) {
        if (texto != null && texto.isEmpty() == false) {
            mUnityPlayer.UnitySendMessage("PlayerManager", "start_inputfield_web_play", texto);
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

    public String[] slide_description = {
            /*1*/"Programação Web básico uso HTML CSS Javascript. ",
            /*2*/"Programação o que? é programar significar informação enviar computador entender linguagem própria linguagem programação. ",
            /*3*/"Programar aprender bom por quê? Lógica raciocínio desenvolver. Inglês melhor. Se problema ter capaz problema sumir acabar. Vaga trabalho oportunidade. Área programação desenvolver sempre. ",
            /*4*/"Pessoa todos programar precisar aprender porque programação ensinar mim pensar.",
            /*5*/"Linguagem programação o que? é língua própria computador programa desenvolver. Linguagem programação criar programa cada um.",
            /*6*/"Linguagem programação usar mais ano 2017 exemplo.",
            /*7*/"Aprender estudar HTML, JS, CSS.",
            /*8*/"Começar como? Primeiro linguagem escolher depois editor escolher depois treinar.",
            /*9*/"HTML exemplo.",
            /*10*/"Primeiro programa meu.",
            /*11*/"Código exemplo.",
            /*12*/"Formulário exemplo.",
            /*13*/"Código fazer tabela exemplo.",
            /*14*/"Consectetur adipiscing elit. ",
            /*15*/"Código fazer tabela exemplo.",
            /*16*/"Tabela exemplo.",
            /*17*/"Botão código exemplo.",
            /*18*/"Alerta exemplo.",
            /*19*/"Alerta exemplo.",
            /*20*/"Ação botão exemplo.",
            /*21*/"Tela estilo não ter exemplo.",
            /*22*/"Ação tabela exemplo.",
            /*23*/"Confirmação alerta exemplo.",
            /*24*/"Tabela exemplo.",
            /*25*/"Estilo exemplo.",
            /*26*/"Estilo exemplo.",
            /*27*/"Estilo exemplo.",
            /*28*/"Estilo exemplo.",
            /*29*/"Tela final exemplo.",
            /*30*/"Sucesso conseguir precisar paciência ter.",
            /*31*/"Obrigado. Email."
    };
}