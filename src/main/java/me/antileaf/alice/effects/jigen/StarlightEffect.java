package me.antileaf.alice.effects.jigen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StarlightEffect extends AbstractGameEffect {
    public float x;
    public float y;
    public float angle;
    private float width;
    private boolean firstFrame = true;

    public StarlightEffect(Color color, float angle, AbstractCreature target, float durModifier) {
        this.color = color;
        this.angle = angle;
        this.x = target.hb.cX;
        this.y = target.hb.cY;
        this.width = 0.3F;
        this.startingDuration = this.duration = 1F + durModifier;
    }

    public void update() {
        if (this.firstFrame) {
            this.firstFrame = false;

//            CardCrawlGame.sound.play("ATTACK_FAST", 0.2F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (!(this.duration < 0.0F)) {
            if (this.duration < 0.2F) {
                if (this.duration > 0.1F) {
                    this.width = Interpolation.exp10Out.apply(0.3F, 2.0F, MathUtils.clamp((0.2F - this.duration) / 0.1F, 0.0F, 1.0F));
                } else {
                    this.width = Interpolation.exp10In.apply(2.0F, 0.0F, MathUtils.clamp(this.duration / 0.1F, 0.0F, 1.0F));
                }

                this.width = Math.max(0.0F, this.width);
            }

            if (this.duration > this.startingDuration * 0.9F) {
                this.color.a = Interpolation.linear.apply(0.0F, 1.0F, (this.startingDuration - this.duration) / (this.startingDuration * 0.1F));
            } else if (this.duration < this.startingDuration * 0.1F) {
                this.color.a = Interpolation.linear.apply(0.0F, 1.0F, this.duration / (this.startingDuration * 0.1F));
            }

        } else {
            this.isDone = true;

            for(int i = 0; i < 7; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new LatticeEffect(this.x, this.y, this.color));
            }

        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.x, this.y, 16.0F * Settings.scale, 16.0F * Settings.scale, 32.0F * Settings.scale, 32.0F * Settings.scale, this.width, 100.0F, this.angle, 0, 0, 32, 32, false, false);
    }

    public void dispose() {
    }
}
