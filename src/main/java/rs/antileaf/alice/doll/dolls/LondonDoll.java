package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class LondonDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = LondonDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 1;
	public static final int ACT_AMOUNT = 8;
	
	public LondonDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("purple"),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.OTHERS;
		this.actAmountType = DollAmountType.DAMAGE;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		return MAX_HP;
	}
	
	@Override
	public void onAct() {
		int pos = -1;
		for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
			AbstractDoll doll = DollManager.get().getDolls().get(i);
			
			if (doll == this)
				break;
			
			if (!(doll instanceof EmptyDollSlot)) {
				pos = i;
				break;
			}
		}
		
		if (pos != -1) {
			int nowpos = DollManager.get().getIndex(this), finalPos = pos;
			AliceSpireKit.addToTop(new AnonymousAction(() -> {
				AbstractDoll other = DollManager.get().getDolls().get(finalPos);
				DollManager.get().getDolls().set(finalPos, this);
				DollManager.get().getDolls().set(nowpos, other);
			}));
		}
		else {
			AbstractMonster m = AbstractDungeon.getRandomMonster();
			if (m != null) {
				this.addActionsToTop(
						new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.0F),
						new DamageAction(
								m,
								new DollDamageInfo(
										this.actAmount,
										this,
										this.actAmountType,
										DollAmountTime.ACT
								)
						)
				);
				
				this.highlightActValue();
			}
		}
	}
	
	@Override
	public void postSpawn() {
		this.addToBot(new AnonymousAction(() -> {
			int index = -1;
			for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--) {
				AbstractDoll doll = DollManager.get().getDolls().get(i);
				
				if (doll instanceof EmptyDollSlot) {
					index = i;
					break;
				}
			}
			
			if (index == -1)
				AliceSpireKit.log(this.getClass(), "No empty doll slot found");
			
			this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDollExcept(LondonDoll.ID), index));
		}));
	}
	
	private void onRecycleOrDestroyed() {
		this.addToBot(new AnonymousAction(() -> {
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
			
			this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDollExcept(LondonDoll.ID), index));
		}));
	}
	
	@Override
	public void onRecycle() {
		this.onRecycleOrDestroyed();
	}
	
	@Override
	public void onDestroyed() {
		this.onRecycleOrDestroyed();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.DESCRIPTION[0];
		this.actDescription = String.format(dollStrings.DESCRIPTION[1], this.coloredActAmount());
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
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new LondonDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
