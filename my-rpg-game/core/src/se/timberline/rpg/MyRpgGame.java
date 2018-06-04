package se.timberline.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyRpgGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	private Texture characterSheet;
	private TextureRegion characterSprite;
    private Texture overworldSheet;
    private TextureRegion greenGrass;
    private OrthographicCamera cam;
    private float x = 0;
    private float y = 0;
	private Integer keycode;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		characterSheet = new Texture("gfx/character.png");
        characterSprite = new TextureRegion(characterSheet, 1, 6, 15, 22);
        overworldSheet = new Texture("gfx/Overworld.png");
        greenGrass = new TextureRegion(overworldSheet, 16, 16);
        cam = new OrthographicCamera(320, 240);

        Gdx.input.setInputProcessor(this);
    }

	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		//batch.draw(img, 0, 0);
        batch.draw(greenGrass,0,0);
		batch.draw(characterSprite,x,y,15,22);
		batch.end();
	}

	private void update() {
		if (keycode != null) {
			switch (keycode) {
				case Input.Keys
						.DOWN:
					y--;
					break;
				case Input.Keys
						.UP:
					y++;
					break;
				case Input.Keys
						.LEFT:
					x--;
					break;
				case Input.Keys
						.RIGHT:
					x++;
					break;
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
    	this.keycode = keycode;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		this.keycode = null;
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
