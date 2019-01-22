package se.timberline.galaxy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Random;

public class MyGalaxyGame extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    private Pixmap pixmap;
    private int xoffset;
    private int yoffset;

    @Override
    public void create() {
        batch = new SpriteBatch();
        pixmap = new Pixmap(600, 400, Pixmap.Format.RGB888);
        reRender();
        Gdx.input.setInputProcessor(this);
    }

    private void reRender() {
        for (int x = 0; x < 600; x++) {
            for (int y = 0; y < 400; y++) {
                pixmap.setColor(getColor(x + xoffset, y + yoffset));
                pixmap.drawPixel(x, y);
            }
        }
    }

    private Color getColor(int x, int y) {
        MersenneTwister random = new MersenneTwister(new int[]{x, y});
        int rand = random.nextInt();
        int r = random.nextInt();
        int g = random.nextInt();
        int b = random.nextInt();
        return rand > (Integer.MAX_VALUE - 10000000) ? new Color(r, g, b, 1) : Color.BLACK;
    }

    @Override
    public void render() {
        img = new Texture(pixmap);
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W) {
            yoffset -= 10;
            reRender();
        }
        if (keycode == Input.Keys.S) {
            yoffset += 10;
            reRender();
        }
        if (keycode == Input.Keys.A) {
            xoffset -= 10;
            reRender();
        }
        if (keycode == Input.Keys.D) {
            xoffset += 10;
            reRender();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
