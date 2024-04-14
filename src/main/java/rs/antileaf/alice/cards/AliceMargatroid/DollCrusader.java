package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AliceDiscoverAction;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.Derivations.dolls.AbstractCreateDoll;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.targeting.DollOrEmptySlotTargeting;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class DollCrusader extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollCrusader.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int BLOCK = 8;
	private static final int MAGIC = 4;
	
	public DollCrusader() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);
		
		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void applyPowers() {
		int tmp = this.block, tmpBase = this.baseBlock;
		
		this.baseBlock = this.baseMagicNumber;
		this.block = this.magicNumber;
		super.applyPowers();
		this.baseMagicNumber = this.baseBlock;
		this.magicNumber = this.block;
		
		this.baseBlock = tmpBase;
		this.block = tmp;
		tmpBase = this.baseMagicNumber;
		tmp = this.magicNumber;
		super.applyPowers();
		this.baseMagicNumber = tmpBase;
		this.magicNumber = tmp;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll target = DollOrEmptySlotTargeting.getTarget(this);
		int index = DollManager.get().getDolls().indexOf(target);
		
		this.addToBot(new GainBlockAction(p, this.block));
		
		if (!this.upgraded) {
			AbstractDoll doll = AbstractDoll.getRandomDoll();
			this.addToBot(new AnonymousAction(() -> {
				AliceSpireKit.addActionsToTop(
						new SpawnDollAction(doll, index),
						new DollGainBlockAction(doll, this.magicNumber)
				);
			}));
		}
		else {
			ArrayList<String> dollIds = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				String id = AbstractDoll.getRandomDollIdExcept(dollIds.toArray(new String[0]));
				dollIds.add(id);
			}
			
			ArrayList<AbstractCard> choices = dollIds.stream()
					.map(AbstractCreateDoll::get)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			
			this.addToBot(new AliceDiscoverAction(
					choices,
					(card) -> {
						if (card instanceof AbstractCreateDoll) {
							AbstractDoll doll = ((AbstractCreateDoll) card).getDoll();
							AliceSpireKit.addActionsToTop(
									new SpawnDollAction(doll, index),
									new DollGainBlockAction(doll, this.magicNumber)
							);
						}
						else
							AliceSpireKit.log(this.getClass(), "Invalid choice: " + card);
					},
					cardStrings.EXTENDED_DESCRIPTION[0],
					false
			));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollCrusader();
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
