package rs.antileaf.alice.loli;

import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.antileaf.alice.cards.AbstractLoliCard;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.function.Function;

public class AliceLoliManager {
	private static final HashMap<Class<? extends AbstractLoliCard<?>>, Function<AbstractCard, AbstractLoliCard<?>>>
			registry = new HashMap<>();

	// I love tricks.
	public static Class<? extends AbstractCard> getParamType(Class<? extends AbstractLoliCard<?>> clazz) {
		Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
		if (type instanceof Class<?>) {
			Class<?> c = (Class<?>) type;
			if (AbstractCard.class.isAssignableFrom(c)) {
				@SuppressWarnings("unchecked")
				Class<? extends AbstractCard> res = (Class<? extends AbstractCard>) c;
				return res;
			}
		}

		throw new IllegalArgumentException("Cannot get parameter type of " + clazz);
	}

	public static void register(Class<? extends AbstractLoliCard<?>> clazz,
								Function<AbstractCard, AbstractLoliCard<?>> fromMethod) {
		registry.put(clazz, fromMethod);

		AliceSpireKit.logger.info("Registered {} -> {}",
				getParamType(clazz).getSimpleName(), clazz.getSimpleName());
	}

	// 如果实现了一个叫 from 的静态方法，就可以直接注册，它会自动尝试调用这个方法
	public static void register(Class<? extends AbstractLoliCard<?>> clazz) {
		final Method method;
		try {
			method = clazz.getMethod("from", AbstractCard.class);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Cannot find method named \"from\" in " + clazz);
		}

		if (method.getReturnType() != clazz)
			throw new IllegalArgumentException("Method from in " + clazz + " does not return " + clazz);

		register(clazz, (card) -> {
			try {
				return (AbstractLoliCard<?>) method.invoke(null, card);
			}
			catch (ReflectiveOperationException e) {
				throw new RuntimeException("Failed to invoke method", e);
			}
		});
	}

	public static boolean isLoliCard(AbstractCard card) {
		return card instanceof AbstractLoliCard;
	}

	public static boolean hasLoliVersion(AbstractCard card) {
		return registry.containsKey(card.getClass());
	}

	public static AbstractLoliCard<?> getLoliVersion(AbstractCard card) {
		return registry.get(card.getClass()).apply(card);
	}
}
