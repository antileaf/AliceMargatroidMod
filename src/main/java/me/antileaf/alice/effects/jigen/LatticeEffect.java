//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.antileaf.alice.effects.jigen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class LatticeEffect extends AbstractGameEffect {
    private final TextureAtlas.AtlasRegion img;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private final float acc;
    private boolean lock;
    private static final TextureAtlas.AtlasRegion LATTICE_2 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("AliceMargatroidMod/img/vfx/lattice_2.png"), 0, 0, 156, 142);

    public LatticeEffect(float x, float y, Color color) {
        this.img = LATTICE_2;

        this.duration = MathUtils.random(0.5F, 1.0F);
        this.x = x - (float)(this.img.packedWidth / 2);
        this.y = y - (float)(this.img.packedHeight / 2);
        this.color = color.cpy();
        this.color.a = 0.0F;
        this.rotation = MathUtils.random(0.0F, 360.0F);
        this.scale = MathUtils.random(0.3F, 0.5F) * Settings.scale;
        float a = MathUtils.random(0.0F, 360.0F);
        this.vX = 1500.0F * (float)Math.cos(a) * Settings.scale;
        this.vY = 1500.0F * (float)Math.sin(a) * Settings.scale;
        this.acc = MathUtils.random(1500.0F, 2200.0F) * Settings.scale;
    }

    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.rotation += Gdx.graphics.getDeltaTime() * this.acc * 0.03F;
        if (this.vX > 0.0F) {
            this.vX = Math.max(0.0F, this.vX - this.acc * Gdx.graphics.getDeltaTime());
        } else {
            this.vX = Math.min(0.0F, this.vX + this.acc * Gdx.graphics.getDeltaTime());
        }

        if (this.vY > 0.0F) {
            this.vY = Math.max(0.0F, this.vY - this.acc * Gdx.graphics.getDeltaTime());
        } else {
            this.vY = Math.min(0.0F, this.vY + this.acc * Gdx.graphics.getDeltaTime());
        }

        if ((double)Math.abs(this.vX) < 1.0E-5 && (double)Math.abs(this.vY) < 1.0E-5 && !this.lock) {
            this.duration = 0.1F;
            this.lock = true;
        }

        if (1.0F - this.duration < 0.1F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, (1.0F - this.duration) * 10.0F);
        } else {
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.8F, 1.2F), this.scale * MathUtils.random(0.8F, 1.2F), this.rotation);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.8F, 1.2F), this.scale * MathUtils.random(0.8F, 1.2F), this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
