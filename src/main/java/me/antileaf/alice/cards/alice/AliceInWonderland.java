package me.antileaf.alice.cards.alice;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceAudioMaster;
import me.antileaf.alice.utils.AliceHelper;

import java.util.stream.IntStream;

public class AliceInWonderland extends AbstractAliceCard {
	public static final String SIMPLE_NAME = AliceInWonderland.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;

	public AliceInWonderland() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);

		this.isEthereal = true;
	}
	
	private AbstractCard getCard() {
		AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
		if (this.upgraded)
			card.upgrade();
		
		if (card.cost != -1 && card.cost != -2)
			card.updateCost(-1);
		return card;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ExhaustAction(BaseMod.MAX_HAND_SIZE, true, false));

		AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.GREEN.cpy()));
		CardCrawlGame.sound.play(AliceAudioMaster.ALICE_IN_WONDERLAND);

		int count = (int) IntStream.range(0, p.hand.size()).filter(i -> p.hand.group.get(i) != this).count();
		for (int i = 0; i < count; i++)
			this.addToBot(new MakeTempCardInHandAction(this.getCard()));

//		this.addToBot(new AnonymousAction(() -> {
//
//			p.hand.refreshHandLayout();
//			p.hand.applyPowers();
//		}));

//		this.addToBot(new AnonymousAction(() -> {
//			AlicePokerCardsDroppingEffect effect = new AlicePokerCardsDroppingEffect(0.6F, 2.5F,
//					false, 750.0F);
//
//			for (int i = 0; i < 32; i++) {
//				float vx = MathUtils.random(-300.0F, 300.0F);
//				float vy = MathUtils.random(-50.0F, 300.0F);
//
//				float x = p.drawX + vx * 0.5F + MathUtils.random(-40.0F, 40.0F);
//				float y = p.drawY + vy * 0.2F + MathUtils.random(-40.0F, 40.0F);
//
//				float angle = vx / 300.0F * 30.0F;
//				float angularV = vx / 300.0F * 180.0F + MathUtils.random(-45.0F, 45.0F);
//
//				effect.add(
//						AlicePokerHelper.getRandomSuit(),
//						x * Settings.scale, y * Settings.scale,
//						vx * Settings.scale, vy * Settings.scale,
//						MathUtils.random(0.0F, 360.0F), // angle
//						MathUtils.random(-360.0F, 360.0F)); // angularV
//			}
//
//			AbstractDungeon.effectList.add(effect);

//			AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.GREEN.cpy()));
//			CardCrawlGame.sound.play(AliceAudioMaster.ALICE_IN_WONDERLAND);
//
//			for (int i = 0; i < p.hand.size(); i++)
//				if (p.hand.group.get(i) != this)
//					p.hand.group.set(i, this.getCard());
//
//			p.hand.refreshHandLayout();
//			p.hand.applyPowers();
//		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceInWonderland();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
