package se.timberline.galaxy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class MyGalaxyGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    private ShapeRenderer shapeRenderer;
    private Pixmap pixmap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        pixmap = new Pixmap(600, 400, Pixmap.Format.RGB888);
        for (int x = 0; x < 600; x++) {
            for (int y = 0; y < 400; y++) {
                pixmap.setColor(getColor(x,y));
                pixmap.drawPixel(x, y);
            }
        }
    }

    private Color getColor(int x, int y) {
        int seed = ((x << 32) | y) ^ 12345679;
        Random random = new Random(seed);
        int rand = random.nextInt();
        return rand % 2 == 0 ? Color.RED : Color.BLACK;
    }

    @Override
    public void render() {
        img = new Texture(pixmap);
        Gdx.gl.glClearColor(1, 0, 0, 1);
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
}
