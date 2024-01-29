package rs.antileaf.alice.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;
import java.util.Arrays;

public class DoNothingAction extends AbstractGameAction {
	public DoNothingAction() {}
	
	public void update() {
		this.isDone = true;
	}
}
