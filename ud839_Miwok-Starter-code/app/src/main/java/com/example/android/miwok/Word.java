package com.example.android.miwok;

/**
 * Created by Anthony on 19/10/2017.
 */

public class Word {

    /**
     * Default translation for the word
     */
    private String mDefaultTranslation;

    /**
     * Miwok translation for the word
     */
    private String mMiwokTranslation;

    /**
     * Image resource ID for the word
     */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /**
     * Constant value that represents no image was provided for this word
     */
    private static final int NO_IMAGE_PROVIDED = -1;

    private int mAudioResourceId;

    /**
     *       * Crie um novo objeto do Word.
     *       *
     *       * @param defaultTranslation é a palavra em um idioma com o qual o usuário já está familiarizado
     *       * (como inglês)
     *       * @param miwokTranslation é a palavra na linguagem Miwok
     */
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    /**
     *       * Crie um novo objeto do Word.
     *       *
     *       * @param defaultTranslation é a palavra em um idioma com o qual o usuário já está familiarizado
     *       * (como inglês)
     *       * @param miwokTranslation é a palavra na linguagem Miwok
     *       * @param imageResourceId é o ID de recurso desenhável para a imagem associada à palavra
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * Return the image resource ID of the word.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public int getmAudioResourceId() {
        return mAudioResourceId;
    }
}