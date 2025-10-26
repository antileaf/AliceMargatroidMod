package me.antileaf.alice.monsters.medicine;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.RunicDome;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.medicine.MedicinePoisonPower;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MedicineMelancholy extends AbstractMonster {
	private static final Logger logger = LogManager.getLogger(MedicineMelancholy.class);
	
	public static final String SIMPLE_NAME = MedicineMelancholy.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	
	private MedicineCardList cardList;
	
	public MedicineMelancholy(float x, float y) {
		super(
				monsterStrings.NAME,
				ID,
				100, // TODO HP
				0.0F, // HB offset X
				-20.0F, // HB offset Y
				200.0F, // HB width
				300.0F, // HB height
				null, // TODO: AliceHelper.getMonsterImgFilePath(SIMPLE_NAME),
				x,
				y
		);
		
		this.type = EnemyType.ELITE;
		
		this.cardList = new MedicineCardList(this.hb.cX, this.hb.cY);
	}
	
	public MedicineMelancholy() {
		this(0.0F, 0.0F);
	}
	
	@Override
	public void takeTurn() {
		// TODO
	}
	
	@Override
	protected void getMove(int i) {
		// TODO
	}
	
	public int calculateCardDamage(int damage) {
		ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class)
				.invoke(this, damage);
		int tmp = this.getIntentDmg();
		ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", -1);
		return tmp;
	}
	
	public boolean bane() {
		if (AbstractDungeon.player.hasPower(MedicinePoisonPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(MedicinePoisonPower.POWER_ID);
			if (power.amount > 1)
				return true;
		}
		
		int index = DollManager.get().damageTarget.getOrDefault(this, -1);
		if (index != -1) {
			AbstractDoll doll = DollManager.get().getDolls().get(index);
			if (doll != null && !(doll instanceof EmptyDollSlot) && doll.poison > 1)
				return true;
		}
		
		return false;
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		
		this.cardList.applyPowers(this);
	}
	
	@SpireOverride
	public void updateIntentTip() {
		SpireSuper.call();
		
		// TODO: this.intentTip = ...
	}
	
	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		
		if (!AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID))
			this.cardList.render(sb);
	}
	
	@SpireOverride
	public void renderIntent(SpriteBatch sb) {
		SpireSuper.call(sb);
		
		if (!AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID))
			this.cardList.renderIntent(sb);
	}
}
