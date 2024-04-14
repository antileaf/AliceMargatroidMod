package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.HashSet;

public class SPDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SPDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public SPDoll() {
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
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			int count = (int) DollManager.get().getDolls().stream()
					.filter(doll -> doll instanceof EmptyDollSlot)
					.count();
			count = Math.min(count, this.magicNumber);
			
			HashSet<String> existing = new HashSet<>();
			
			for (int i = 0; i < count; i++) {
				AbstractDoll newDoll = AbstractDoll.getRandomDollExcept(existing.toArray(new String[0]));
				if (newDoll == null) {
					AliceSpireKit.log("SPDoll: No dolls left to spawn.");
					break;
				}
				
				existing.add(newDoll.getID());
				AliceSpireKit.addActionToBuffer(new SpawnDollAction(newDoll, -1));
			}
			
			AliceSpireKit.commitBuffer();
			
//			for (int i = 0; i < count; i++)
//				this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDoll(), -1));
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SPDoll();
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
