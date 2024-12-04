package zh.qiushui.mod.qca.command.commandTpPos;

import carpet.utils.CommandHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import zh.qiushui.mod.qca.QcaSettings;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public class TpPosCommand {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tppos")
                        .requires(source -> CommandHelper.canUseCommand(source, QcaSettings.commandTpPos))
                        .then(CommandManager.argument("location", Vec3ArgumentType.vec3())
                                      .executes(ctx -> execute(
                                                  ctx.getSource(), ctx.getSource().getEntityOrThrow(), ctx.getSource().getWorld(),
                                                  Vec3ArgumentType.getPosArgument(ctx, "location")

                                      )))
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                                      .then(CommandManager.argument("location", Vec3ArgumentType.vec3())
                                                      .executes(
                                                              commandContext -> execute(
                                                                      commandContext.getSource(),
                                                                      EntityArgumentType.getEntity(commandContext, "target"),
                                                                      commandContext.getSource().getWorld(),
                                                                      Vec3ArgumentType.getPosArgument(commandContext, "location")
                                                              )
                                                      )))
        );
    }

    private static int execute(ServerCommandSource source, Entity entity, ServerWorld world, PosArgument posArgument) throws CommandSyntaxException {
        Vec3d vec3d = posArgument.getPos(source);
        TeleportCommand.teleport(
                source, entity, world,
                vec3d.x, vec3d.y, vec3d.z,
                getFlags(posArgument, entity.getWorld().getRegistryKey() == world.getRegistryKey()),
                entity.getYaw(), entity.getPitch(),
                null
        );

        source.sendFeedback(
                () -> Text.translatable(
                        "commands.teleport.success.location.single",
                        entity.getDisplayName(),
                        formatFloat(vec3d.x),
                        formatFloat(vec3d.y),
                        formatFloat(vec3d.z)
                ),
                true
        );

        return 1;
    }

    private static Set<PositionFlag> getFlags(PosArgument posArgument, boolean bl) {
        Set<PositionFlag> set = EnumSet.noneOf(PositionFlag.class);
        if (posArgument.isXRelative()) {
            set.add(PositionFlag.DELTA_X);
            if (bl) {
                set.add(PositionFlag.X);
            }
        }

        if (posArgument.isYRelative()) {
            set.add(PositionFlag.DELTA_Y);
            if (bl) {
                set.add(PositionFlag.Y);
            }
        }

        if (posArgument.isZRelative()) {
            set.add(PositionFlag.DELTA_Z);
            if (bl) {
                set.add(PositionFlag.Z);
            }
        }

        return set;
    }

    private static String formatFloat(double d) {
        return String.format(Locale.ROOT, "%f", d);
    }
}
