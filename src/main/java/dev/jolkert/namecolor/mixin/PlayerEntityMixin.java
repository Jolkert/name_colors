package dev.jolkert.namecolor.mixin;

import dev.jolkert.namecolor.NameColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
  protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
  public void getName(CallbackInfoReturnable<Text> cir) {
    Text original = cir.getReturnValue();
    int color = NameColor.getNameColor(this.getUuid());

    cir.setReturnValue(color != -1 ? original.copy().setStyle(Style.EMPTY.withColor(color)) : original);
  }

  @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
  public void getDisplayName(CallbackInfoReturnable<Text> cir) {
    Text original = cir.getReturnValue();
    int color = NameColor.getNameColor(this.getUuid());

    cir.setReturnValue(color != -1 ? original.copy().setStyle(Style.EMPTY.withColor(color)) : original);
  }
}
