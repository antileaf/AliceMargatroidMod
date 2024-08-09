package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.RemoveDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class AliceGame extends AbstractAliceCard {
	public static final String SIMPLE_NAME = AliceGame.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	
	public AliceGame() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.exhaust = true;
		
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll rightmost = DollManager.get().getDolls().stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.findFirst()
				.orElse(null);
		
		if (rightmost != null) {
			int count = 0;
			
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot) && doll != rightmost) {
					count++;
					
					this.addToBot(new AnonymousAction(() -> {
						if (DollManager.get().contains(doll))
							this.addToTop(new RemoveDollAction(doll));
					}));
				}
			
			for (int i = 0; i < count; i++)
				this.addToBot(new DollActAction(rightmost));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceGame();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.exhaust = false;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
