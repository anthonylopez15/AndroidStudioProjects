package br.com.mapinnovation.cadastrosamu;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CadastroAdapter sliderAdapter;
    private LinearLayout dotsLayout;

    private TextView[] dots;

    private Button btnVoltar;
    private Button btnProx;

    private int pagingaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        viewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotsLayout = findViewById(R.id.dotsLayout);

        btnProx = (Button) findViewById(R.id.proximoBtn);
        btnVoltar = (Button) findViewById(R.id.voltarBtn);

        sliderAdapter = new CadastroAdapter(CadastroActivity.this, getSupportFragmentManager());

        viewPager.setAdapter(sliderAdapter);

        addDotsIndicatior(0);

        viewPager.addOnPageChangeListener(viewListener);

        //Desativa o sliding
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });



        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem( pagingaAtual + 1 );

            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem( pagingaAtual - 1 );

            }
        });


    }

    public void addDotsIndicatior(int position){

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicatior(i);
            pagingaAtual = i;

            if(i == 0){
                btnProx.setEnabled(true);
                btnVoltar.setEnabled(false);
                btnVoltar.setVisibility(View.INVISIBLE);

                btnProx.setText("Proximo");
                btnVoltar.setText("");

            }else if(i == dots.length - 1){

                btnProx.setEnabled(true);
                btnVoltar.setEnabled(true);
                btnVoltar.setVisibility(View.VISIBLE);

                btnProx.setText("Salvar");
                btnVoltar.setText("Voltar");

            }else{

                btnProx.setEnabled(true);
                btnVoltar.setEnabled(true);
                btnVoltar.setVisibility(View.VISIBLE);

                btnProx.setText("Proximo");
                btnVoltar.setText("Voltar");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}