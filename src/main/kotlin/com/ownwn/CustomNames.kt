package com.ownwn

import com.ownwn.config.Config
import net.minecraft.client.MinecraftClient
import net.minecraft.text.MutableText
import net.minecraft.text.OrderedText
import net.minecraft.text.Text


class CustomNames {
    companion object {
        private val config = Config.instance()


        private fun OrderedText.contains(string: String): Boolean {
            return TextUtils.openOrderedText(this).joinToString("") { it.string }.contains(string)
        }

        /** @return An edited `OrderedText` with the player's custom name and rank*/
        fun replaceName(text: OrderedText): OrderedText {
            val username = MinecraftClient.getInstance().player?.name?.string ?: return text

            val customRankEnabled = config.customRankToggle && config.customRank.isNotEmpty()
            val customNameEnabled = config.customNameToggle && config.customName.isNotEmpty()


            if (!customNameEnabled) return text

            val originalTextArray = TextUtils.openOrderedText(text)
            if (!originalTextArray.joinToString("") { it.string }.contains(username)) return text


            var currentText = text

            // loop in case username appears in multiple places
            // todo could get stuck if config.customName == player.username?
            while (currentText.contains(username)) {

                // replace rank
                if (customRankEnabled && currentText.contains(config.manualRankSelector + " " + username)) {
                    currentText = TextUtils.replaceOrderedText(currentText,
                        config.manualRankSelector,
                        getCustomText(config.customRank, config.customRankType, false)
                    )
                }

                // replace name
                currentText = TextUtils.replaceOrderedText(currentText, username,
                    getCustomText(config.customName, config.customNameType, true)
                )
            }

            return currentText
        }

        /** @param oldText The text to be edited
         * @param chromaType Which type of chroma the user has selected from config
         * @param isCustomName Whether to use the custom name static colour, or the custom rank static colour*/
        private fun getCustomText(oldText: MutableText, chromaType: Config.ChromaType, isCustomName: Boolean): Text {
            return when (chromaType) {
                Config.ChromaType.FANCY_CHROMA -> TextUtils.dynamicSpectrum(oldText)
                // get the correct static colour for rank/name
                Config.ChromaType.STATIC_COLOUR -> oldText.withColor(
                    if (isCustomName) config.staticNameColour.rgb else config.staticRankColour.rgb
                )
                Config.ChromaType.PLAIN_CHROMA -> oldText.withColor(TextUtils.colourSpectrum())
            }
        }
        /** overload of [getCustomText]*/
        private fun getCustomText(oldText: String, chromaType: Config.ChromaType, isCustomName: Boolean): Text {
            return getCustomText(Text.literal(oldText), chromaType, isCustomName)
        }
    }


}


