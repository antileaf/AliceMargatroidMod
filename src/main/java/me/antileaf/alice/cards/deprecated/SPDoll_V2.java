package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.SpawnDollAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.dolls.ShanghaiDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

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
			
			ArrayList<AbstractDoll> dolls = new ArrayList<>();
			
			for (int i = 0; i < count; i++) {
				AbstractDoll doll = new ShanghaiDoll();
				dolls.add(doll);
				
				int k = AbstractDungeon.cardRandomRng.random(indices.size() - 1);
				int index = indices.get(k);
				indices.remove(k);
				
				AliceHelper.addActionToBuffer(new SpawnDollAction(doll, index));
			}
			
			if (this.upgraded)
				for (AbstractDoll d : dolls)
					AliceHelper.addActionToBuffer(new DollActAction(d));
			
			AliceHelper.commitBuffer();
			
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
