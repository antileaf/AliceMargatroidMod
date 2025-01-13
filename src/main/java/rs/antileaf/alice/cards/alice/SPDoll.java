package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.KyotoDoll;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class SPDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SPDoll.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public SPDoll() {
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
		this.addToBot(new AnonymousAction(() -> {
			ArrayList<Integer> indices = new ArrayList<>();
			for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
				if (DollManager.get().getDolls().get(i) instanceof EmptyDollSlot)
					indices.add(i);
			}
					
			int count = Math.min(indices.size(), this.magicNumber);
			
			for (int i = 0; i < count; i++) {
				AbstractDoll doll = null;
				switch (AbstractDungeon.cardRandomRng.random(0, 1)) {
					case 0:
						doll = new ShanghaiDoll();
						break;
					case 1:
						doll = new KyotoDoll();
						break;
				}
				
				int k = AbstractDungeon.cardRandomRng.random(indices.size() - 1);
				int index = indices.get(k);
				indices.remove(k);
				
				AliceHelper.addActionToBuffer(new SpawnDollAction(doll, index));
			}
			
			AliceHelper.commitBuffer();
			
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
