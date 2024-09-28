package rs.antileaf.alice.cards.loli;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractLoliCard;
import rs.antileaf.alice.cards.alice.RedHairedNetherlandsDoll;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.NetherlandsDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

public class RedHairedNetherlandsDoll_Loli extends AbstractLoliCard<RedHairedNetherlandsDoll> {
	public static final String SIMPLE_NAME = RedHairedNetherlandsDoll_Loli.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 2;
	private static final int MAGIC = 2;

	public RedHairedNetherlandsDoll_Loli() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(RedHairedNetherlandsDoll.SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_LOLI_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.targetIcons.add(AliceTargetIcon.SLOT);
		this.targetIcons.add(AliceTargetIcon.NONE);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		AliceHoveredTargets o = new AliceHoveredTargets();
		o.dolls = DollManager.get().getDolls().stream()
				.filter(doll -> doll != slot)
				.filter(doll -> doll instanceof NetherlandsDoll)
				.toArray(AbstractDoll[]::new);

		if (this.upgraded)
			o.player = true;

		return o;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getIndex(slot);
		
		AbstractDoll doll = new NetherlandsDoll();
		this.addToBot(new SpawnDollAction(doll, index));

		this.addToBot(new AnonymousAction(() -> {
			this.applyPowers();

			if (this.upgraded)
				for (int i = 0; i < this.magicNumber; i++)
					AliceSpireKit.addActionToBuffer(new GainBlockAction(p, p, this.block));

			for (AbstractDoll n : DollManager.get().getDolls()) {
				if (n instanceof NetherlandsDoll)
					for (int i = 0; i < this.magicNumber; i++)
						AliceSpireKit.addActionToBuffer(new DollGainBlockAction(n, this.block));
			}

			AliceSpireKit.commitBuffer();
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new RedHairedNetherlandsDoll_Loli();
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
