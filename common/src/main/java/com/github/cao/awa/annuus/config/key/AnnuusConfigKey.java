package com.github.cao.awa.annuus.config.key;

import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public record AnnuusConfigKey<T>(String name, Consumer<T> callback, Class<T> type, T defaultValue, List<T> limits) {
    public static <X> AnnuusConfigKey<X> create(String name, X defaultValue) {
        return new AnnuusConfigKey<>(name, (x) -> {}, Manipulate.cast(defaultValue.getClass()), defaultValue, new ObjectArrayList<>());
    }

    public static <X> AnnuusConfigKey<X> create(String name, Consumer<X> callback, X defaultValue) {
        return new AnnuusConfigKey<>(name, callback, Manipulate.cast(defaultValue.getClass()), defaultValue, new ObjectArrayList<>());
    }

    @SafeVarargs
    public static <X> AnnuusConfigKey<X> create(String name, Consumer<X> callback, X defaultValue, X... limits) {
        return new AnnuusConfigKey<>(name, callback, Manipulate.cast(defaultValue.getClass()), defaultValue, new ObjectArrayList<>(limits));
    }

    public static <X> AnnuusConfigKey<X> create(String name, Consumer<X> callback, X defaultValue, Collection<X> limits) {
        return new AnnuusConfigKey<>(name, callback, Manipulate.cast(defaultValue.getClass()), defaultValue, new ObjectArrayList<>(limits));
    }

    @SafeVarargs
    public final AnnuusConfigKey<T> withLimits(T... limits) {
        this.limits.clear();
        this.limits.addAll(new ObjectArrayList<>(limits));
        return this;
    }

    public T onChangeCheck(T value) {
        if (this.limits.isEmpty() || this.limits.contains(value)) {
            this.callback.accept(value);
            return value;
        }
        throw new IllegalStateException("Unexpected config value '" + value + "', the config '" + this.name + "' only allow these values: " + this.limits);
    }
}
