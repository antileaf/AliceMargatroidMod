package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class DollCrusader extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollCrusader.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;
//	private static final int MAGIC = 1;
	
	public DollCrusader() {
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
		
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			ArrayList<Integer> indices = new ArrayList<>();
			
			for (int i = 0; i < DollManager.get().getDolls().size(); i++)
				if (!(DollManager.get().getDolls().get(i) instanceof EmptyDollSlot)) {
					AbstractDoll doll = DollManager.get().getDolls().get(i);
					indices.add(i);

					AliceHelper.addActionToBuffer(new RecycleDollAction(doll));
				}
			
			for (int i : indices) {
				AbstractDoll doll = AbstractDoll.getRandomDoll();
				AliceHelper.addActionToBuffer(new SpawnDollAction(doll, i));
//				AliceSpireKit.addActionToBuffer(new DollActAction(doll));
			}
			
			AliceHelper.commitBuffer();
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollCrusader();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
//			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
