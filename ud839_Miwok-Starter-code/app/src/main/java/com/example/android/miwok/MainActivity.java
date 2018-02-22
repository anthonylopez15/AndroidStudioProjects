/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Ache o view pager que habilitará o usuário a deslizar entre fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Ache o view pager que habilitará o usuário a deslizar entre fragments
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Sete o adapter no view pager
        viewPager.setAdapter(adapter);

        //Ache o layout da aba que mostra as abas
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Conecte o layout da aba com o view pager. Isto irá
        //   1. Atualizar o layout da aba quando o view pager for deslizado
        //   2. Atualizar o view pager quando uma aba for selecionada
        //   3. Definir os nomes da aba do layout da aba com os títulos do adapter do view pager
        //      chamando onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }


}
