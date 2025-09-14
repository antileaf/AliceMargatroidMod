package me.antileaf.alice.powers.unique;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;
import me.antileaf.alice.cardmodifier.PhantomCardModifier;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class PrincessFormPower extends TwoAmountPower {
	public static final String SIMPLE_NAME = PrincessFormPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private AbstractCard last = null;
	private AbstractCard copy = null;
	
	private float pulseTimer = 0.0F;
	private boolean sfx = false;

	public PrincessFormPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.amount2 = 0;
		this.priority = 5;
		
		this.type = PowerType.BUFF;
		this.updateDescription();

		String img84 = AliceHelper.getPowerImgFilePath(SIMPLE_NAME + "84");
		String img32 = AliceHelper.getPowerImgFilePath(SIMPLE_NAME + "32");
		this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img84), 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img32), 0, 0, 32, 32);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster monster) {
		this.last = null;
		this.copy = null;

		if (card.costForTurn == 1 || (card.cost == -1 && card.energyOnUse == 1)) {
			this.amount2++;

			if (this.amount2 == 2) {
				this.pulseTimer = 0.0F;
				this.sfx = true;
			}
			else {
				this.pulseTimer = 1.0F;
				this.sfx = false;
			}

			if (this.amount2 >= 3) {
				this.last = card;
				this.copy = card.makeStatEquivalentCopy();

				this.amount2 -= 3;
			}
		}
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card == this.last && this.copy != null) {
			this.flash();
			this.copy.setCostForTurn(0);
			CardModifierManager.addModifier(this.copy, new PhantomCardModifier());
			this.addToBot(new MakeTempCardInHandAction(this.copy, this.amount));
		}

		this.last = null;
		this.copy = null;
	}
	
	@Override
	public void update(int slot) {
		super.update(slot);
		
		if (this.amount2 == 2) {
			this.pulseTimer -= Gdx.graphics.getDeltaTime();
			
			if (this.pulseTimer < 0.0F) {
				ArrayList<AbstractGameEffect> effects = ReflectionHacks.getPrivate(this,
						AbstractPower.class, "effect");
				
				if (this.sfx) {
					effects.add(new GainPowerEffect(this));
					this.sfx = false;
				}
				else
					effects.add(new SilentGainPowerEffect(this));
				
				this.pulseTimer = 1.0F;
			}
		}
	}
}
