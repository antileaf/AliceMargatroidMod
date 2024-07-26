package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.KyotoDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;

import java.util.ArrayList;

public class SpringKyotoDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SpringKyotoDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public SpringKyotoDoll() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<Integer> choices = new ArrayList<>();
		for (int i = 0; i < DollManager.get().getDolls().size(); i++)
			if (DollManager.get().getDolls().get(i) instanceof EmptyDollSlot)
				choices.add(i);
		
		for (int k = 0; k < this.magicNumber; k++) {
			if (choices.isEmpty())
				break;
			
			int pos = choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
			this.addToBot(new SpawnDollAction(new KyotoDoll(), pos));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SpringKyotoDoll();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
