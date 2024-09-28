package rs.antileaf.alice.cards.loli;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractLoliCard;
import rs.antileaf.alice.cards.alice.CharismaticOrleansDoll;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.OrleansDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

public class CharismaticOrleansDoll_Loli extends AbstractLoliCard<CharismaticOrleansDoll> {
	public static final String SIMPLE_NAME = CharismaticOrleansDoll_Loli.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 0;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public CharismaticOrleansDoll_Loli() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(CharismaticOrleansDoll.SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_LOLI_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
		this.targetIcons.add(AliceTargetIcon.NONE);

		this.configureFreeToPlayOnce();
	}

	public void configureFreeToPlayOnce() {
		if (AliceSpireKit.isInBattle()) {
			if (AbstractDungeon.player.masterDeck.group.stream()
					.map(c -> c.uuid)
					.noneMatch(u -> u.equals(this.uuid)))
				this.freeToPlayOnce = true;
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getDolls().indexOf(slot);
		
		AbstractDoll doll = new OrleansDoll();
		this.addToBot(new SpawnDollAction(doll, index));
		
		for (int i = 0; i < this.magicNumber; i++)
			this.addToBot(new DollActAction(doll));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CharismaticOrleansDoll_Loli();
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

	public static CharismaticOrleansDoll_Loli from(CharismaticOrleansDoll card) {
		CharismaticOrleansDoll_Loli res = AbstractLoliCard.fromAlice(new CharismaticOrleansDoll_Loli(), card);
		res.configureFreeToPlayOnce(); // 因为被重置了所以要重新初始化
		return res;
	}
}
