package rs.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

public class MysteriousChallenger extends AbstractAliceCard {
	public static final String SIMPLE_NAME = MysteriousChallenger.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
//	private static final int MAGIC = 1;
	
	public MysteriousChallenger() {
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
		
//		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	public boolean shouldTriggerEffect() {
		int count = (int)DollManager.get().getDolls().stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.count();
		
		if ((!this.upgraded && count != 5) || (this.upgraded && (count < 3 || count > 5)))
			return false;
		
		for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
			AbstractDoll doll = DollManager.get().getDolls().get(i);
			if (!(doll instanceof EmptyDollSlot)) {
				for (int j = 1; j < count && i + j < DollManager.get().getDolls().size(); j++) {
					AbstractDoll other = DollManager.get().getDolls().get(i + j);
					if (other instanceof EmptyDollSlot)
						return false;
				}
				
				return true;
			}
		}
		
		AliceHelper.log("MysteriousChallenger: There was a bug in the code. Please report this to the mod author.");
		return false;
	}
	
	@Override
	public void triggerOnGlowCheck() {
		if (this.shouldTriggerEffect())
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.shouldTriggerEffect()) {
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot))
					this.addToBot(new DollActAction(doll));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new MysteriousChallenger();
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
