package com.zappycode.coingame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;


// Partie déclaration des variables
public class CoinMan extends ApplicationAdapter {
    SpriteBatch batch;

    Texture background;
    Texture[] man;
    Texture dizzy;
    Texture gameover;
    Texture main;
    Texture copy;
    Texture title;
    Texture studio;

    Rectangle manRectangle;

    BitmapFont font;

    int backgroundPosition = 0;
    int score = 0;
    int gameState = 0;
    int screenWidth;
    int screenHeight;
    int manState = 0;
    int pause = 0;
    int manY = 0;
    int life = 3;
    int highScore = 0;
    Preferences preferences;


    float gravity = 1f;
    float velocity = 0;


    //génerateur auto
    Random random;

    Sound coinSound;
    Sound gameoverSound;
    Sound damageSound;
    Sound diamondSound;
    Sound starSound;


//diamant

    ArrayList<Integer> diamondXs = new ArrayList<Integer>();
    ArrayList<Integer> diamondYs = new ArrayList<Integer>();
    ArrayList<Rectangle> diamondRectangles = new ArrayList<Rectangle>();

    Texture diamond;
    int diamondCount;


//Etoile

    ArrayList<Integer> star1Xs = new ArrayList<Integer>();
    ArrayList<Integer> star1Ys = new ArrayList<Integer>();
    ArrayList<Rectangle> star1Rectangles = new ArrayList<Rectangle>();

    Texture star1;
    int star1Count;


    // coin
    ArrayList<Integer> coinXs = new ArrayList<Integer>();
    ArrayList<Integer> coinYs = new ArrayList<Integer>();
    ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();

    Texture coin;
    int coinCount;

    //bomb
    ArrayList<Integer> bombXs = new ArrayList<Integer>();
    ArrayList<Integer> bombYs = new ArrayList<Integer>();
    ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();

    Texture bomb;
    int bombCount;

//Bullet

    ArrayList<Integer> bulletXs = new ArrayList<Integer>();
    ArrayList<Integer> bulletYs = new ArrayList<Integer>();
    ArrayList<Rectangle> bulletRectangles = new ArrayList<Rectangle>();

    Texture bullet;
    int bulletCount;

    //Partie graphisme
    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4];
        man[0] = new Texture("frame-1.png");
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");

        manY = Gdx.graphics.getHeight() / 2;

        coin = new Texture("coin.png");
        random = new Random();
        bomb = new Texture("bomb.png");
        random = new Random();
        bullet = new Texture("bullet.png");
        random = new Random();
        star1 = new Texture("star1.png");
        random = new Random();
        diamond = new Texture("diamond.png");

        dizzy = new Texture("dizzy-1.png");
        gameover = new Texture("go.png");

        main = new Texture("main.png");
        copy = new Texture("copy.png");
        title = new Texture("title.png");
        studio = new Texture("studio.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3);


        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin_sound.wav"));
        gameoverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("damage_sound.mp3"));
        diamondSound = Gdx.audio.newSound(Gdx.files.internal("diamond.wav"));
        starSound = Gdx.audio.newSound(Gdx.files.internal("star.mp3"));


        preferences = Gdx.app.getPreferences("run_out");
        highScore = preferences.getInteger("highscore",0);


    }


    //fonction diamond
    public void makeDiamond() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        diamondYs.add((int) height);
        diamondXs.add(Gdx.graphics.getWidth());
    }

    //fonction star1
    public void makeStar1() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        star1Ys.add((int) height);
        star1Xs.add(Gdx.graphics.getWidth());
    }

    //fonction Piece
    public void makeCoin() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        coinYs.add((int) height);
        coinXs.add(Gdx.graphics.getWidth());
    }

    //fonction Bomb
    public void makeBomb() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bombYs.add((int) height);
        bombXs.add(Gdx.graphics.getWidth());
    }

    //fonction bullet
    public void makeBullet() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bulletYs.add((int) height);
        bulletXs.add(Gdx.graphics.getWidth());
    }


    //partie ECRAN/DISPOSITION
//decor
    @Override
    public void render() {
        batch.begin();
        batch.draw(background, backgroundPosition, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(background, backgroundPosition + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState == 1) {

            if (life == 0) {
                long id = gameoverSound.play(1.0f);
                gameoverSound.setLooping(id, false);
                gameState = 2;
            }


            if (backgroundPosition < Gdx.graphics.getWidth()) {
                backgroundPosition -= 5;
            }
            if (backgroundPosition < (-Gdx.graphics.getWidth())) {
                backgroundPosition = 0;
            }


            //diamond
            if (diamondCount < 400) {
                diamondCount++;
            } else {
                diamondCount = 0;
                makeDiamond();
            }

            diamondRectangles.clear();
            for (int i = 0; i < diamondXs.size(); i++) {
                batch.draw(diamond, diamondXs.get(i), diamondYs.get(i));
                diamondXs.set(i, diamondXs.get(i) - 8);
                diamondRectangles.add(new Rectangle(diamondXs.get(i), diamondYs.get(i), diamond.getWidth(), diamond.getHeight()));
            }


            //star1
            if (star1Count < 175) {
                star1Count++;
            } else {
                star1Count = 0;
                makeStar1();
            }

            star1Rectangles.clear();
            for (int i = 0; i < star1Xs.size(); i++) {
                batch.draw(star1, star1Xs.get(i), star1Ys.get(i));
                star1Xs.set(i, star1Xs.get(i) - 8);
                star1Rectangles.add(new Rectangle(star1Xs.get(i), star1Ys.get(i), star1.getWidth(), star1.getHeight()));
            }


            //bullet
            if (bulletCount < 175) {
                bulletCount++;
            } else {
                bulletCount = 0;
                makeBullet();
            }

            for (int i = 0; i < bulletXs.size(); i++) {
                batch.draw(bullet, bulletXs.get(i), bulletYs.get(i));
                bulletXs.set(i, bulletXs.get(i) - 12);
            }

            bulletRectangles.clear();
            for (int i = 0; i < bulletXs.size(); i++) {
                batch.draw(bullet, bulletXs.get(i), bulletYs.get(i));
                bulletXs.set(i, bulletXs.get(i) - 8);
                bulletRectangles.add(new Rectangle(bulletXs.get(i), bulletYs.get(i), bullet.getWidth(), bullet.getHeight()));
            }


            //bomb
            if (bombCount < 100) {
                bombCount++;
            } else {
                bombCount = 0;
                makeBomb();
            }

            for (int i = 0; i < bombXs.size(); i++) {
                batch.draw(bomb, bombXs.get(i), bombYs.get(i));
                bombXs.set(i, bombXs.get(i) - 1);
            }

            bombRectangles.clear();
            for (int i = 0; i < bombXs.size(); i++) {
                batch.draw(bomb, bombXs.get(i), bombYs.get(i));
                bombXs.set(i, bombXs.get(i) - 8);
                bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
            }


            //piece
            if (coinCount < 30) {
                coinCount++;
            } else {
                coinCount = 0;
                makeCoin();
            }

            coinRectangles.clear();
            for (int i = 0; i < coinXs.size(); i++) {
                batch.draw(coin, coinXs.get(i), coinYs.get(i));
                coinXs.set(i, coinXs.get(i) - 8);
                coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
            }

            //Saut
            if (Gdx.input.justTouched()) {
                velocity = -20;
            }
            //vitesse de l'animation du personnage
            if (pause < 3) {
                pause++;
            } else {
                pause = 0;
                if (manState < 2) {
                    manState++;
                } else {
                    manState = 0;
                }
            }

            velocity += gravity;
            manY -= velocity;

            if (manY <= 0) {
                manY = 0;

            }

            if (manY > (Gdx.graphics.getHeight() - manRectangle.getHeight())) {
                manY = (int) (Gdx.graphics.getHeight() - manRectangle.getHeight());
            }


        } else if (gameState == 0) {
            batch.draw(main, 600, 100);
            batch.draw(copy, 1500, 125);
            batch.draw(title, 650, 886);
            batch.draw(studio,50,75);

            if (Gdx.input.justTouched()) {
                gameState = 1;
                //
            }
        } else if (gameState == 2) {


            if (Gdx.input.justTouched()) {
                gameState = 1;
                manY = Gdx.graphics.getHeight() / 2;
                score = 0;
                velocity = 0;
                coinXs.clear();
                coinYs.clear();
                coinRectangles.clear();
                coinCount = 0;
                bombXs.clear();
                bombYs.clear();
                bombRectangles.clear();
                bombCount = 0;
                bulletXs.clear();
                bulletYs.clear();
                bulletRectangles.clear();
                bulletCount = 0;
                star1Xs.clear();
                star1Ys.clear();
                star1Rectangles.clear();
                star1Count = 0;
                diamondXs.clear();
                diamondYs.clear();
                diamondRectangles.clear();
                diamondCount = 0;
                life = 3;
                highScore = preferences.getInteger("highscore",highScore);

                //

            }
        }

        //Partie AFFICHAGE

        //gameover


        if (gameState == 2) {

            if(score > highScore)
            {
                preferences.putInteger("highscore",score);
                preferences.flush();
                font.draw(batch, "New High Score: " + String.valueOf(score),screenWidth /2, screenHeight - 100);
            }
            else
            {
                font.draw(batch, "Score: " + String.valueOf(score),screenWidth /2, screenHeight - 100);
            }
            batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
            batch.draw(gameover, Gdx.graphics.getWidth() / 4 - man[manState].getWidth() / 4, manY);

        } else {
            batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
        }
        manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());

        for (int i = 0; i < coinRectangles.size(); i++) {
            if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
                score++;

                coinRectangles.remove(i);
                coinXs.remove(i);
                coinYs.remove(i);
                long id = coinSound.play(1.0f);
                coinSound.setPitch(id, 2);
                coinSound.setLooping(id, false);
                break;
            }
        }

        for (int i = 0; i < bombRectangles.size(); i++) {
            if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
                life--;

                bombRectangles.remove(i);
                bombXs.remove(i);
                bombYs.remove(i);
                long id = damageSound.play(1.0f);
                damageSound.setLooping(id, false);
                break;
            }
        }

        for (int i = 0; i < bulletRectangles.size(); i++) {
            if (Intersector.overlaps(manRectangle, bulletRectangles.get(i))) {
                life--;


                bulletRectangles.remove(i);
                bulletXs.remove(i);
                bulletYs.remove(i);
                long id = damageSound.play(1.0f);
                damageSound.setLooping(id, false);
                break;
            }
        }

        for (int i = 0; i < star1Rectangles.size(); i++) {
            if (Intersector.overlaps(manRectangle, star1Rectangles.get(i))) {
                score += 5;

                star1Rectangles.remove(i);
                star1Xs.remove(i);
                star1Ys.remove(i);
                long id = starSound.play(1.0f);
                starSound.setPitch(id, 2);
                starSound.setLooping(id, false);
                break;
            }
        }

        for (int i = 0; i < diamondRectangles.size(); i++) {
            if (Intersector.overlaps(manRectangle, diamondRectangles.get(i))) {
                score += 10;

                diamondRectangles.remove(i);
                diamondXs.remove(i);
                diamondYs.remove(i);
                long id = diamondSound.play(1.0f);
                damageSound.setPitch(id, 2);
                diamondSound.setLooping(id, false);
                break;
            }
        }

        font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Lives : " + life, 10, Gdx.graphics.getHeight() - 125);
        font.draw(batch, "Record : " + highScore,10, Gdx.graphics.getHeight() - 200);


        batch.end();


    }

    //execution
    @Override
    public void dispose() {
        batch.dispose();

    }
}


