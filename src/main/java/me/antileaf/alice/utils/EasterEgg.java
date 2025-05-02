package me.antileaf.alice.utils;

import com.badlogic.gdx.Gdx;

import java.util.Arrays;
import java.util.function.Consumer;

public class EasterEgg {
	private final int[] sequence;

	private final Consumer<Integer> stepCallback;
	private final Consumer<Integer> missCallback;
	private final Runnable callback;

	private int index;

	public EasterEgg(int[] sequence, Runnable callback,
					 Consumer<Integer> stepCallback, Consumer<Integer> missCallback) {
		this.sequence = sequence;
		this.callback = callback;
		this.stepCallback = stepCallback;
		this.missCallback = missCallback;
		this.index = 0;
	}

	public EasterEgg(int[] sequence, Runnable callback) {
		this(sequence, callback, null, null);
	}

	public void updateInput() {
		if (Arrays.stream(this.sequence).anyMatch(key -> Gdx.input.isKeyJustPressed(key))) {
			int cur = this.sequence[this.index];
			if (Gdx.input.isKeyJustPressed(cur) && Arrays.stream(this.sequence)
					.filter(key -> key != cur)
					.noneMatch(key -> Gdx.input.isKeyJustPressed(key))) {
				if (this.stepCallback != null)
					this.stepCallback.accept(this.sequence[this.index]);

				this.index++;
			}
			else {
				if (this.missCallback != null)
					this.stepCallback.accept(this.sequence[this.index]);

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
