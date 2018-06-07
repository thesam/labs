package se.timberline.rpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private final Texture characterSheet;
    private final TextureRegion characterSprite;
    private final TextureRegion characterSprite2;
    private final Animation<TextureRegion> walkAnimation;
    private TextureRegion currentFrame;
    private float x;
    private float y;

    public Player() {
        characterSheet = new Texture("gfx/character.png");
        characterSprite = new TextureRegion(characterSheet, 1, 6, 15, 22);
        characterSprite2 = new TextureRegion(characterSheet, 17, 7, 15, 22);
        TextureRegion[] walkFrames = new TextureRegion[2];
        walkFrames[0] = characterSprite;
        walkFrames[1] = characterSprite2;
        walkAnimation = new Animation<TextureRegion>(1f, walkFrames);
        currentFrame = walkFrames[0];
    }

    public void animate(float stateTime) {
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

    }

    public TextureRegion currentFrame() {
        return currentFrame;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
