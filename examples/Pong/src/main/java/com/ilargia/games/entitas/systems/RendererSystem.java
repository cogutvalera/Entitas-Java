package com.ilargia.games.entitas.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.core.CoreContext;
import com.ilargia.games.entitas.core.CoreEntity;
import com.ilargia.games.entitas.core.CoreMatcher;
import com.ilargia.games.logicbrick.component.Score;
import com.ilargia.games.logicbrick.component.TextureView;
import com.ilargia.games.logicbrick.component.View;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.api.system.ITearDownSystem;
import com.ilargia.games.entitas.group.Group;


public class RendererSystem implements IRenderSystem, ITearDownSystem {
    private BitmapFont font;
    private Group<CoreEntity> _group;
    private ShapeRenderer sr;
    private Camera cam;
    private Group<CoreEntity> _groupScore;
    private Batch batch;
    private Group<CoreEntity> _groupTextureView;

    public RendererSystem(CoreContext context, ShapeRenderer sr, Camera cam, Batch batch, BitmapFont font) {
        this.sr = sr;
        this.cam = cam;
        this.batch = batch;
        this.font = font;
        _group = context.getGroup(CoreMatcher.View());
        _groupScore = context.getGroup(CoreMatcher.Score());
        _groupTextureView = context.getGroup(CoreMatcher.TextureView());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();

        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);

        for (CoreEntity e : _group.getEntities()) {
            View view = e.getView();
            System.out.printf("..views");
            if (view.shape instanceof Rectangle) {
                Rectangle ret = (Rectangle) view.shape;
                sr.rect(ret.x, ret.y, ret.width, ret.height);
            } else {
                Circle circle = (Circle) view.shape;
                sr.circle(circle.x, circle.y, circle.radius);
            }

        }

        sr.end();

        batch.begin();
        for (CoreEntity e : _groupScore.getEntities()) {
            Score score = e.getScore();
            font.draw(batch, score.text + " " + score.points, score.x, score.y);
        }
        for (CoreEntity e : _groupTextureView.getEntities()) {
            TextureView textureView = e.getTextureView();

            batch.draw(textureView.texture, textureView.position.x, textureView.position.y,
                    0, 0, textureView.width, textureView.height, 1, 1, textureView.rotation);


        }
        batch.end();
    }

    @Override
    public void tearDown() {
        //batch.end();
        batch = null;
//        //cam = null;
//        font = null;
//        _group = null;
//        sr = null;
//        cam = null;
//        _groupTextureView = null;
    }
}
