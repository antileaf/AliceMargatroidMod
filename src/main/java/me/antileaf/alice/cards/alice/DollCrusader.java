package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.RecycleDollAction;
import me.antileaf.alice.action.doll.SpawnDollAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class DollCrusader extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollCrusader.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
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
		this.exhaust = true;
		
//		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
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

//			ArrayList<AbstractDoll> spawn = new ArrayList<>();
			
			for (int i : indices) {
				AbstractDoll doll = AbstractDoll.getRandomDoll();
				AliceHelper.addActionToBuffer(new SpawnDollAction(doll, i));
//				spawn.add(doll);
			}

//			AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
//				for (AbstractDoll d : DollManager.get().getDolls())
//					if (!(d instanceof EmptyDollSlot))
//						AliceHelper.addActionToBuffer(new DollActAction(d));
//				AliceHelper.commitBuffer();
//			}));
			
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
			this.exhaust = false;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
