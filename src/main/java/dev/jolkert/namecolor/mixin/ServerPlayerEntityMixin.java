package dev.jolkert.namecolor.mixin;

import com.mojang.authlib.GameProfile;
import dev.jolkert.namecolor.NameColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
  public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
    super(world, pos, yaw, gameProfile);
  }

  @Inject(method = "getPlayerListName", at = @At("RETURN"), cancellable = true)
  public void getPlayerListName(CallbackInfoReturnable<Text> cir) {
    Text original = cir.getReturnValue();
    int color = NameColor.getNameColor(this.getUuid());

    cir.setReturnValue(color != -1 ? this.getDisplayName().copy().setStyle(Style.EMPTY.withColor(color)) : original);
  }
}
