package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.targeting.handlers.DollTargeting;
import rs.antileaf.alice.utils.AliceSpireKit;

public class SeekerDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SeekerDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
//	private static final int UPGRADE_PLUS_MAGIC = 1;
	private static final int MAGIC2 = 2;
	private static final int UPGRADE_PLUS_MAGIC2 = 1;
	
	public SeekerDoll() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public void triggerOnGlowCheck() {
		boolean played = false;
		for (int i = 0; i < AbstractDungeon.actionManager.cardsPlayedThisTurn.size(); i++) {
			AbstractCard c = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(i);
			if (c instanceof SeekerDoll) {
				played = true;
				break;
			}
		}
		
		if (!played)
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll != null && !(doll instanceof EmptyDollSlot)) {
			int count = this.secondaryMagicNumber;
			
			boolean played = false;
			for (int i = 0; i < AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1; i++) {
				AbstractCard c = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(i);
				if (c instanceof SeekerDoll) {
					played = true;
					break;
				}
			}
			
			if (!played)
				count += this.magicNumber;
			
			for (int i = 0; i < count; i++)
				this.addToBot(new DollActAction(doll));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SeekerDoll();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.initializeDescription();
		}
	}
}
