package me.antileaf.alice.cards.alice;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.effects.jigen.StarlightEffect;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.strings.AliceCardNoteStrings;
import me.antileaf.alice.strings.AliceLanguageStrings;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.Comparator;

public class SevenColoredPuppeteer extends AbstractAliceCard implements OnObtainCard {
	public static final String SIMPLE_NAME = SevenColoredPuppeteer.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final AliceCardNoteStrings cardNoteStrings = AliceCardNoteStrings.get(ID);

	private static final Color[] colors = {
			new Color(0xFF7C80FF),
			new Color(0xED7D31FF),
			new Color(0xFFC000FF),
			new Color(0x70AD47FF),
			new Color(0x5B9BD5FF),
			new Color(0xB953FFFF),
			Color.WHITE
	};
	
	private static final int COST = 7;
	private static final int DAMAGE = 7;
	private static final int MAGIC = 7;
	
	int reduced = 0;
	
	public SevenColoredPuppeteer() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
		if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null)
			this.configureCostsOnNewCard();
	}
	
	public void configureCostsOnNewCard() {
		DollManager.Stats stats = DollManager.get().getStats();
		for (String placed : stats.placedThisCombat) {
			this.updateCost(-1);
			this.reduced++;
		}
	}

	@Override
	public void onObtainCard() {
		this.cost = this.costForTurn = COST;
		this.isCostModified = false;
		this.reduced = 0;
	}
	
	public void updateStats(DollManager.Stats stats) {
		for (int i = this.reduced; i < stats.placedThisCombat.size(); i++) {
			this.updateCost(-1);
			this.reduced++;
		}
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		this.updateStats(DollManager.get().getStats());
	}
	
	@Override
	public TooltipInfo getNote() {
		if (!AliceHelper.isInBattle())
			return null;
//			return new TooltipInfo(cardNoteStrings.TITLE, cardNoteStrings.DESCRIPTION);
		
		if (cardNoteStrings == null) {
			AliceHelper.logger.info("SevenColoredPuppeteer.getNote(): No note found for ID: {}!", ID);
			return null;
		}
		
		DollManager.Stats stats = DollManager.get().getStats();
		
		StringBuilder sb = new StringBuilder(
				String.format(cardNoteStrings.EXTENDED_DESCRIPTION[0], stats.placedThisCombat.size()));
		if (stats.placedThisCombat.isEmpty())
			sb.append(AliceLanguageStrings.PERIOD);
		else {
			sb.append(cardNoteStrings.EXTENDED_DESCRIPTION[1]).append(" NL ");
			
			String[] ids = stats.placedThisCombat.stream()
					.sorted(Comparator.comparingInt(s -> -stats.actCountThisCombat.getOrDefault(s, 0)))
//					.filter(s -> stats.actCountThisCombat.getOrDefault(s, 0) > 0)
					.toArray(String[]::new);
			
			for (String id : ids) {
				int value = stats.actCountThisCombat.getOrDefault(id, 0);
				sb.append(AbstractDoll.getName(id))
						.append(AliceLanguageStrings.COLON_WITH_SPACE)
						.append(value)
						.append(value <= 1 ?
								cardNoteStrings.EXTENDED_DESCRIPTION[2] :
								cardNoteStrings.EXTENDED_DESCRIPTION[3])
						.append(id.equals(ids[ids.length - 1]) ?
								AliceLanguageStrings.PERIOD :
								AliceLanguageStrings.SEMICOLON + " NL ");
			}
		}
		
//		AliceSpireKit.logger.info("SevenColoredPuppeteer.getNote(): {}", sb);
		
		return new TooltipInfo(cardNoteStrings.TITLE, sb.toString());
	}
	
	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (!super.canUse(p, m))
			return false;
		
		DollManager.Stats stats = DollManager.get().getStats();
		if (stats.actCountThisCombat.values().stream()
				.max(Integer::compareTo)
				.orElse(0) < 7) {
			this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
			return false;
		}
		
		return true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);

		ArrayList<Integer> indexes = new ArrayList<>();
		for (int i = 0; i < this.magicNumber; i++) {
			int index = MathUtils.random(0, colors.length - 1);
			while (indexes.contains(index))
				index = MathUtils.random(0, colors.length - 1);
			indexes.add(index);
		}

		this.addToBot(new WaitAction(0.2F));

		for(int i = 0; i < this.magicNumber; i++) {
			for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
				if (!mo.isDeadOrEscaped()) {
					Color color = colors[indexes.get(i)].cpy();
//					color.r *= MathUtils.random(0.8F, 1.2F);
//					if (color.r > 1.0F)
//						color.r = 1.0F;
//					color.g *= MathUtils.random(0.8F, 1.2F);
//					if (color.g > 1.0F)
//						color.g = 1.0F;
//					color.b *= MathUtils.random(0.8F, 1.2F);
//					if (color.b > 1.0F)
//						color.b = 1.0F;
					color.a = 0.7F;

					AliceHelper.addActionToBuffer(new VFXAction(new StarlightEffect(
							colors[indexes.get(i)].cpy(),
							MathUtils.random(360.0F), mo,
							i * -0.01F + 0.12F)));
				}

			AliceHelper.addActionToBuffer(new WaitAction(0.08F));

			AliceHelper.commitBuffer();

			this.addToBot(new DamageAllEnemiesAction(
					p,
					this.multiDamage,
					this.damageTypeForTurn,
					AbstractGameAction.AttackEffect.FIRE,
					true
			) {{ this.duration = 0.0F; }});

			this.addToBot(new WaitAction(0.09F));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SevenColoredPuppeteer();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.retain = this.selfRetain = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
	
	public static void updateAll() {
		for (CardGroup group : new CardGroup[]{
				AbstractDungeon.player.drawPile,
				AbstractDungeon.player.discardPile,
				AbstractDungeon.player.hand,
				AbstractDungeon.player.limbo
		})
			for (AbstractCard card : group.group) {
				if (card instanceof SevenColoredPuppeteer)
					((SevenColoredPuppeteer) card).updateStats(DollManager.get().getStats());
			}
	}
}
