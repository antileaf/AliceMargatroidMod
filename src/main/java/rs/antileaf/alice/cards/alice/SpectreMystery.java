package rs.antileaf.alice.cards.alice;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.WindyParticleEffect;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class SpectreMystery extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SpectreMystery.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_MAGIC = 2;
	
	public SpectreMystery() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> cards = new ArrayList<>();
		for (int i = 0; i < this.magicNumber && i < p.drawPile.size(); i++) {
			AbstractCard card = p.drawPile.getNCardFromTop(i);
			cards.add(card);
		}
		
		this.addToBot(new AnonymousAction(() -> {
			ArrayList<AbstractCard> upgradedCards = new ArrayList<>();

			for (AbstractCard card : cards)
				if (card.canUpgrade() && card.type != CardType.STATUS && card.type != CardType.CURSE) {
					card.upgrade();
//					card.superFlash();
					card.applyPowers();

					upgradedCards.add(card);
				}

			for (AbstractCard card : upgradedCards) {
				float rank = upgradedCards.size() > 1 ?
						upgradedCards.indexOf(card) / (float) (upgradedCards.size() - 1) :
						0.5F;
				float x = (MathUtils.lerp(0.3F, 0.7F, rank) +
						MathUtils.random(-0.1F, 0.1F)) * Settings.WIDTH;
				float y = MathUtils.random(0.3F, 0.7F) * Settings.HEIGHT;
				AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(), x, y));
//				AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
//				CardCrawlGame.sound.playV("STANCE_ENTER_CALM", 0.65F);
			}
		}));
		
		for (int i = 0; i < this.magicNumber; i++)
			AbstractDungeon.effectList.add(new WindyParticleEffect());
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SpectreMystery();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
