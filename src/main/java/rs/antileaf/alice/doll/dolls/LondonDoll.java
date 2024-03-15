package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.patches.enums.DamageTypeEnum;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;
import java.util.Arrays;

public class LondonDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = LondonDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 1;
	public static final int PASSIVE_AMOUNT = 3;
	public static final int ACT_AMOUNT = 2;
	
	public LondonDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("purple"),
				RenderTextMode.BOTH
		);
		
		this.passiveAmountType = DollAmountType.DAMAGE;
		this.actAmountType = DollAmountType.BLOCK;
	}
	
	@Override
	public void onAct() {
		this.addToBot(new DollGainBlockAction(this, this.actAmount));
		this.addToBot(new GainBlockAction(AbstractDungeon.player, this.actAmount));
	}
	
	@Override
	public void postSpawn() {
		this.onStartOfTurn();
	}
	
	@Override
	public void onStartOfTurn() {
		this.addToBot(new AnonymousAction(() -> {
			int[] damages = DamageInfo.createDamageMatrix(this.passiveAmount, true);
			AliceSpireKit.log(this.getClass(), "LondonDoll passive damage: " +
					Arrays.stream(damages).mapToObj(Integer::toString).reduce("", (a, b) -> a + ", " + b));
			
			ArrayList<AbstractGameAction> actions = new ArrayList<>();
			
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
				if (!m.isDeadOrEscaped())
					actions.add(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.0F));
			
			actions.add(new DamageAllEnemiesAction(
					null,
					DamageInfo.createDamageMatrix(this.passiveAmount, true),
					DamageTypeEnum.DOLL,
					AbstractGameAction.AttackEffect.NONE,
					true
			));
			
			AliceSpireKit.addActionsToTop(actions.toArray(new AbstractGameAction[0]));
		}));
	}
	
	@Override
	public void onRemoved() {
		int index = -1;
		for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--) {
			AbstractDoll doll = DollManager.get().getDolls().get(i);
			
			if ((doll instanceof EmptyDollSlot) || doll == this) {
				index = i;
				break;
			}
		}
		
		if (index == -1)
			AliceSpireKit.log(this.getClass(), "No empty doll slot found");
		else
			this.addToBot(new SpawnDollAction(AbstractDoll.getRandomDollExcept(LondonDoll.class), index));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[0],
				this.coloredPassiveAmount(),
				dollStrings.DESCRIPTION[1]
		);
		
		this.actDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[2],
				this.coloredActAmount(),
				dollStrings.DESCRIPTION[3]
		);
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new LondonDoll();
	}
	
	@Override
	public void playChannelSFX() {}
}
