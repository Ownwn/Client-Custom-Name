package com.ownwn.mixin;

import com.ownwn.CustomNames;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @ModifyVariable(at = @At("HEAD"), method = "prepare(Lnet/minecraft/text/OrderedText;FFIZI)Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;", ordinal = 0, argsOnly = true)
    public OrderedText prepareOrderedText(OrderedText value) {
        return CustomNames.Companion.replaceName(value);
    }

    @ModifyVariable(at = @At("HEAD"), method = "prepare(Ljava/lang/String;FFIZI)Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;", ordinal = 0, argsOnly = true)
    public String prepareString(String value) {
        return CustomNames.Companion.replaceName(value);
    }

    @ModifyVariable(at = @At("HEAD"), method = "getWidth(Lnet/minecraft/text/OrderedText;)I", ordinal = 0, argsOnly = true)
    public OrderedText getWidth(OrderedText value) {
        return CustomNames.Companion.replaceName(value);
    }

    @ModifyVariable(at = @At("HEAD"), method = "getWidth(Ljava/lang/String;)I", ordinal = 0, argsOnly = true)
    public String getWidth(String value) {
        return CustomNames.Companion.replaceName(value);
    }
}
