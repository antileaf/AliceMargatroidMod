package me.antileaf.alice.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EasterEgg {
	private final int[] sequence;

	private final Consumer<Integer> stepCallback;
	private final Consumer<Integer> missCallback;
	private final Runnable callback;
	
	private final boolean controllerSupport;

	private int index;

	public EasterEgg(int[] sequence, Runnable callback,
					 Consumer<Integer> stepCallback, Consumer<Integer> missCallback, boolean controllerSupport) {
		this.sequence = sequence;
		this.callback = callback;
		this.stepCallback = stepCallback;
		this.missCallback = missCallback;
		this.controllerSupport = controllerSupport;
		this.index = 0;
	}

	public EasterEgg(int[] sequence, Runnable callback) {
		this(sequence, callback, null, null, false);
	}

	public void updateInput() {
		boolean RT = AliceControllerHelper.isRightTriggerPressed();
		
		Map<Integer, Boolean> pressed = new HashMap<>();
		
		for (int key : this.sequence) {
			if (pressed.containsKey(key))
				continue;
			
			boolean flag = Gdx.input.isKeyJustPressed(key);
			
			if (this.controllerSupport && RT) {
				if (key == Input.Keys.UP)
					flag |= CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed();
				else if (key == Input.Keys.DOWN)
					flag |= CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed();
				else if (key == Input.Keys.LEFT)
					flag |= CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed();
				else if (key == Input.Keys.RIGHT)
					flag |= CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed();
				else if (key == Input.Keys.A)
					flag |= CInputActionSet.select.isJustPressed();
				else if (key == Input.Keys.B)
					flag |= CInputActionSet.cancel.isJustPressed();
			}
			
			if (flag)
				System.out.println("EasterEgg: Detected key " + Input.Keys.toString(key));
			
			pressed.put(key, flag);
		}
		
		if (this.controllerSupport && RT) {
			for (int button : new int[] { Input.Keys.BUTTON_X, Input.Keys.BUTTON_Y,
					Input.Keys.BUTTON_L1, Input.Keys.BUTTON_R1 }) {
				pressed.put(button, Gdx.input.isKeyJustPressed(button));
			}
		}
		
		if (pressed.values().stream().anyMatch(b -> b)) {
			int cur = this.sequence[this.index];
			if (pressed.get(cur) && pressed.entrySet().stream()
					.filter(e -> e.getKey() != cur)
					.noneMatch(Map.Entry::getValue)) {
				if (this.stepCallback != null)
					this.stepCallback.accept(this.sequence[this.index]);

				this.index++;
			}
			else {
				if (this.missCallback != null)
					this.missCallback.accept(this.sequence[this.index]);

				this.index = 0;
			}

			if (this.index >= this.sequence.length) {
				this.callback.run();
				this.index = 0;
			}
		}
	}

	public void clear() {
		this.index = 0;
	}
}
