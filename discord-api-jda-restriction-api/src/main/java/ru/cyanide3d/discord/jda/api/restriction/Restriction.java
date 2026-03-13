package ru.cyanide3d.discord.jda.api.restriction;


import ru.cyanide3d.discord.jda.api.contexts.EventContext;

public interface Restriction<C extends EventContext<?>> {

    RestrictionResult check(C ctx);

    default Restriction<C> and(Restriction<? super C> other) {
        return ctx -> {
            var r1 = this.check(ctx);
            if (!r1.isAllowed())  {
                return r1;
            }
            return other.check(ctx);
        };
    }

    default Restriction<C> or(Restriction<? super C> other) {
        return ctx -> {
            var r1 = this.check(ctx);
            if (r1.isAllowed()) {
                return r1;
            }
            var r2 = other.check(ctx);
            if (r2.isAllowed()) {
                return r2;
            }
            return r1;
        };
    }

    default Restriction<C> not(String reasonIfDenied) {
        return ctx -> {
            var r = this.check(ctx);
            return r.isAllowed() ? RestrictionResult.deny(reasonIfDenied) : RestrictionResult.allow();
        };
    }

    static <C extends EventContext<?>> Restriction<C> allow() {
        return ctx -> RestrictionResult.allow();
    }

}
