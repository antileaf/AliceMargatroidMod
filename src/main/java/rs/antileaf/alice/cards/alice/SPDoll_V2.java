package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class SPDoll_V2 extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SPDoll_V2.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 3;
	
	public SPDoll_V2() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
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
		this.addToBot(new AnonymousAction(() -> {
			ArrayList<Integer> indices = new ArrayList<>();
			for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
				if (DollManager.get().getDolls().get(i) instanceof EmptyDollSlot)
					indices.add(i);
			}
					
			int count = Math.min(indices.size(), this.magicNumber);
			
			ArrayList<AbstractDoll> dolls = new ArrayList<>();
			
			for (int i = 0; i < count; i++) {
				AbstractDoll doll = new ShanghaiDoll();
				dolls.add(doll);
				
				int k = AbstractDungeon.cardRandomRng.random(indices.size() - 1);
				int index = indices.get(k);
				indices.remove(k);
				
				AliceSpireKit.addActionToBuffer(new SpawnDollAction(doll, index));
			}
			
			if (this.upgraded)
				for (AbstractDoll d : dolls)
					AliceSpireKit.addActionToBuffer(new DollActAction(d));
			
			AliceSpireKit.commitBuffer();
			
//			for (int i = 0; i < count; i++)
//				this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDoll(), -1));
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SPDoll_V2();
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
