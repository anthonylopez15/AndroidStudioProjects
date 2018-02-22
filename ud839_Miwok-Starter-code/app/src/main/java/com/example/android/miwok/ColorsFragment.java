package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {


    //Controla a reprodução de todos os arquivos de som
    private MediaPlayer mMediaPlayer;

    // Controla o foco de áudio ao reproduzir um arquivo de som
    private AudioManager mAudioManager;

    /**
     *        Este listener é ativado quando o {@link MediaPlayer} foi concluído
     *        tocando o arquivo de áudio.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Agora que o arquivo de som finalizou a reprodução, liberte os recursos do media player.
            releaseMediaPlayer();
        }
    };

    /**
     * Este listener é ativado sempre que o foco de áudio muda
     * (ou seja, ganhamos ou perdemos foco de áudio por causa de outro aplicativo ou dispositivo).     
     */

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                /* O caso AUDIOFOCUS_LOSS_TRANSIENT significa que perdemos foco de áudio por um
                   curto período de tempo. O caso AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK significa que
                   nosso aplicativo pode continuar a tocar som, mas em um volume menor. Trataremos
                   ambos os casos da mesma maneira porque nosso aplicativo está reproduzindo pequenos arquivos de som.

                   Pausar a reprodução e reiniciar o player no início do arquivo. Dessa forma, podemos
                   toque a palavra desde o início quando retomamos a reprodução.*/
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // O caso AUDIOFOCUS_GAIN significa que recuperamos o foco e podemos retomar a reprodução.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                /*/ O caso AUDIOFOCUS_LOSS significa que perdemos foco de áudio e
                 // Parar reprodução e limpeza de recursos*/
                releaseMediaPlayer();
            }
        }
    };

    private void releaseMediaPlayer() {
        // Se o reprodutor de mídia não for nulo, então ele poderá tocar um som.
        if (mMediaPlayer != null) {
            // Independentemente do estado atual do media player, divulgue seus recursos
            // porque já não precisamos disso.
            mMediaPlayer.release();

            // Defina o player de mídia de volta para nulo. Para o nosso código, decidimos que
            // definir o player de mídia como nulo é uma maneira fácil de dizer que o player de mídia
            // não está configurado para reproduzir um arquivo de áudio no momento.
            mMediaPlayer = null;

            // Independentemente de ter ou não sido concedido foco de áudio, abandone-o. Isso também
            // anula o AudioFocusChangeListener para que não obtenhamos mais chamadas de retorno.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Crie e configure o {@link AudioManager} para solicitar o foco de áudio
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Crie uma lista de palavras
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow,
                R.raw.color_mustard_yellow));
        words.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow,
                R.raw.color_dusty_yellow));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));

        /* Crie um {@link WordAdapter}, cuja fonte de dados é uma lista de {@link Word} s. o
         // adaptador sabe como criar itens de lista para cada item na lista.*/
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        /* Encontre o objeto {@link ListView} na hierarquia de exibição da {@link Activity}.
           Deve haver um {@link ListView} com a lista de ID da vista, que é declarada no
           arquivo de layout word_list.xml.*/
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        /* Faça o {@link ListView} usar o {@link WordAdapter} que criamos acima, para que o
         // {@link ListView} exibirá itens de lista para cada {@link Word} na lista.*/
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*/ Libere o media player se ele existe atualmente porque estamos prestes a
                  reproduzir um arquivo de som diferente*/
                releaseMediaPlayer();

                // Obter o objeto {@link Word} na posição determinada, o usuário clicou em
                Word word = words.get(position);

                /* Solicite foco de áudio para que possa reproduzir o arquivo de áudio. O aplicativo precisa jogar um
                   arquivo de áudio curto, então pediremos foco de áudio com um curto período de tempo
                  com AUDIOFOCUS_GAIN_TRANSIENT.*/
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    /* Nós temos foco de áudio agora

                     Crie e configure o {@link MediaPlayer} para o recurso de áudio associado
                     Com a palavra atual*/
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());

                    // Inicie o arquivo de áudio
                    mMediaPlayer.start();

                    /* Configurar um listener no media player, para que possamos parar e liberar o
                     media player uma vez que o som termine de tocar.*/
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

}
