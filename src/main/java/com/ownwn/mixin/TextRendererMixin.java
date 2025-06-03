package com.ownwn.mixin;

import com.ownwn.CustomNames;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @ModifyVariable(at = @At("HEAD"), method = "drawInternal(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;IIZ)I", ordinal = 0, argsOnly = true)
    public OrderedText drawInternal(OrderedText value) {
        return CustomNames.Companion.replaceName(value);
    }

    @ModifyVariable(at = @At("HEAD"), method = "getWidth(Lnet/minecraft/text/OrderedText;)I", ordinal = 0, argsOnly = true)
    public OrderedText getWidth(OrderedText value) {
        return CustomNames.Companion.replaceName(value);
    }
}
