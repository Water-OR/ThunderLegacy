package net.llvg.thunder.legacy.internal.tweak;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import java.io.File;
import java.util.List;
import net.hypixel.modapi.tweaker.HypixelModAPITweaker;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinTweaker;

import static net.llvg.thunder.legacy.internal.tweak.CallbackTweakKt.*;

@SuppressWarnings ("unused")
public final class Tweak
  implements ITweaker
{
    private static final Object lock = new Object();
    private static volatile boolean isFirst = true;
    
    @Nullable
    private final MixinTweaker mixinTweak;
    
    public Tweak() {
        boolean meIsFirst;
        if (isFirst) synchronized (lock) {
            meIsFirst = isFirst;
            isFirst = false;
        }
        else meIsFirst = false;
        mixinTweak = meIsFirst && !getBlackboardTweaks().contains(MixinTweaker.class.getName()) ? new MixinTweaker() : null;
    }
    
    @Override
    public void acceptOptions(
      List<String> args,
      File gameDir,
      File assetsDir,
      String profile
    ) {
        List<@NotNull String> tweaks = getBlackboardTweaks();
        tweaks.add(MixinTweaker.class.getName());
        tweaks.add(HypixelModAPITweaker.class.getName());
        
        if (mixinTweak == null) return;
        mixinTweak.acceptOptions(args, gameDir, assetsDir, profile);
        MixinExtrasBootstrap.init();
        addSelfMixinContainer();
    }
    
    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        if (mixinTweak != null) mixinTweak.injectIntoClassLoader(classLoader);
    }
    
    @Override
    public String getLaunchTarget() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String[] getLaunchArguments() {
        return mixinTweak == null ? new String[0] : mixinTweak.getLaunchArguments();
    }
}
