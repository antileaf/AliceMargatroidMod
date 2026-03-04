package me.antileaf.alice.monsters.medicine;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.RunicDome;
import me.antileaf.alice.cards.medicine.MedicineCripplingPoison;
import me.antileaf.alice.cards.medicine.MedicineEnvenom;
import me.antileaf.alice.cards.medicine.MedicineNoxiousFumes;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.medicine.MedicineOutmaneuverPower;
import me.antileaf.alice.powers.medicine.MedicinePoisonPower;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MedicineMelancholy extends AbstractMonster {
	private static final Logger logger = LogManager.getLogger(MedicineMelancholy.class);
	
	public static final String SIMPLE_NAME = MedicineMelancholy.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	
	public MedicineCardList cardList;
	
	int act_count = 0;
	
	public MedicineMelancholy(float x, float y) {
		super(
				monsterStrings.NAME,
				ID,
				100, // TODO HP
				0.0F, // HB offset X
				-20.0F, // HB offset Y
				250.0F, // HB width
				300.0F, // HB height
				AliceHelper.getImgFilePath("monsters", SIMPLE_NAME),
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
		this.cardList.playAll(this);
		
		if (this.hasPower(MedicineOutmaneuverPower.POWER_ID))
			this.addToTop(new RemoveSpecificPowerAction(this, this,
					MedicineOutmaneuverPower.POWER_ID));
		
		this.act_count++;
		if (this.act_count > 3)
			this.act_count = 1;
		this.addToBot(new RollMoveAction(this));
	}
	
	@Override
	protected void getMove(int i) {
		// TODO
		
		this.cardList.clear();
		
		if (this.act_count == 0) {
			AbstractCard envenom = new MedicineEnvenom();
			envenom.upgrade();
			
			this.cardList.add(envenom);
			this.cardList.add(new MedicineNoxiousFumes());
			this.cardList.add(new Outmaneuver());
		}
		else if (this.act_count == 1) {
			AbstractCard cripplingPoison = new MedicineCripplingPoison();
			cripplingPoison.upgrade();
			
			this.cardList.add(new DeadlyPoison());
			this.cardList.add(cripplingPoison);
			this.cardList.add(new CorpseExplosion());
		}
		else if (this.act_count == 2) {
			this.cardList.add(new Bane());
			this.cardList.add(new PoisonedStab());
			this.cardList.add(new PoisonedStab());
		}
		else if (this.act_count == 3) {
			this.cardList.add(new BouncingFlask());
			this.cardList.add(new Outmaneuver());
		}
		
		this.setMove((byte) 0, Intent.NONE);
	}
	
	public int calculateCardDamage(int damage) {
		ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class)
				.invoke(this, damage);
		int tmp = this.getIntentDmg();
		ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", -1);
		return tmp;
	}
	
	public boolean bane(int index) {
		if (AbstractDungeon.player.hasPower(MedicinePoisonPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(MedicinePoisonPower.POWER_ID);
			if (power.amount > 1)
				return true;
		}
		
		AbstractDoll doll = DollManager.get().getDolls().get(index);
		return doll != null && !(doll instanceof EmptyDollSlot) && doll.poison > 1;
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
		PowerTip intentTip = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
		intentTip.body = "测试文本";
		intentTip.header = "测试标题";
		intentTip.img = ImageMaster.INTENT_UNKNOWN;
	}
	
	@Override
	public void update() {
		super.update();
		
		this.cardList.update(this.hb.cX, this.hb.cY + this.hb.height / 2.0F + 100.0F * Settings.scale);
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
		
//		if (!AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID))
//			this.cardList.renderIntent(sb);
	}
}
