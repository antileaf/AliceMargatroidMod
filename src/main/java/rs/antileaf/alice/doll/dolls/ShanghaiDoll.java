package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ShanghaiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = ShanghaiDoll.class.getSimpleName();
	public static final String ID = ShanghaiDoll.SIMPLE_NAME;
	public static final String NAME = ""; // TODO
	
	public static final int MAX_HP = 2;
	public static final int PASSIVE_AMOUNT = 5;
	public static final int ACT_AMOUNT = 4;
	
	public ShanghaiDoll() {
		super(
				ShanghaiDoll.ID,
				ShanghaiDoll.NAME,
				ShanghaiDoll.MAX_HP,
				ShanghaiDoll.PASSIVE_AMOUNT,
				ShanghaiDoll.ACT_AMOUNT,
				"", "", "");
	}
	
	@Override
	public void onAct() {
		AbstractMonster m = AliceSpireKit.getMonsterWithLeastHP();
		
		if (m != null)
			this.addToTop(new DamageAction(m,
					new DollDamageInfo(this.actAmount, ShanghaiDoll.class)));
	}
	
	public void postSpawn() {
		AbstractMonster m = AbstractDungeon.getRandomMonster();
		
		if (m != null)
			this.addToTop(new DamageAction(m,
					new DollDamageInfo(this.passiveAmount, ShanghaiDoll.class)));
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new ShanghaiDoll();
	}
	
	@Override
	public void playChannelSFX() {}
}
