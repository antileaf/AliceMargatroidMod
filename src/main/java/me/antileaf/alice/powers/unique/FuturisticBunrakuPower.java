package me.antileaf.alice.powers.unique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.RecycleDollAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.interfaces.OnDollOperateHook;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

import java.util.Arrays;

public class FuturisticBunrakuPower extends AbstractAlicePower implements NonStackablePower, OnDollOperateHook {
	public static final String SIMPLE_NAME = FuturisticBunrakuPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public final AbstractDoll doll;
	private float vfxTimer = 0.0F;

	public FuturisticBunrakuPower(AbstractDoll doll) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.doll = doll;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void updateDescription() {
		String yellow = Arrays.stream(this.doll.name.split(" "))
				.map(s -> "#y" + s)
				.reduce((s1, s2) -> s1 + " " + s2)
				.orElse("");

		this.description = String.format(powerStrings.DESCRIPTIONS[0], yellow, yellow);
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		this.addToBot(new AnonymousAction(() -> {
			if (DollManager.get().contains(this.doll))
				this.addToTop(new DollActAction(this.doll));
		}));
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		this.addToBot(new AnonymousAction(() -> {
			if (DollManager.get().contains(this.doll))
				this.addToTop(new RecycleDollAction(this.doll));
		}));

		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}

	@Override
	public void postRecycleOrDestroyDoll(AbstractDoll doll) {
		if (this.doll == doll)
			this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
	
	@Override
	public void update(int slot) {
		super.update(slot);

		this.vfxTimer -= Gdx.graphics.getDeltaTime();
		if (this.vfxTimer < 0.0F) {
			AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(this.doll.cX, this.doll.cY));
			this.vfxTimer = MathUtils.random(0.1F, 0.4F);
		}
	}
}
