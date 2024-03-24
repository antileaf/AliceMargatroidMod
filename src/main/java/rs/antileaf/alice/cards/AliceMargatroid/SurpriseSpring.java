package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class SurpriseSpring extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SurpriseSpring.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int HEAL = 3;
	private static final int UPGRADE_PLUS_HEAL = 2;
	
	public SurpriseSpring() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = HEAL;
		this.exhaust = true;
		this.tags.add(CardTags.HEALING);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new HealAction(p, p, this.magicNumber));
		this.addToBot(new DrawCardAction(
				1,
				new AnonymousAction(() -> {
					if (!DrawCardAction.drawnCards.isEmpty()) {
						int drawn_count = 0;
						for (AbstractCard card : DrawCardAction.drawnCards) {
							if (AbstractDungeon.player.hand.contains(card)) {
								drawn_count++;
								this.addToTop(new UpgradeSpecificCardAction(card));
							}
						}
						
						if (drawn_count > 1)
							AliceMargatroidMod.logger.warn("SpringHasCome drawn >1 cards. How could this be possible???");
					}
				})
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SurpriseSpring();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
			this.initializeDescription();
		}
	}
}
