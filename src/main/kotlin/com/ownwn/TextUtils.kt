package com.ownwn

import com.ownwn.config.Config
import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.awt.Color
import kotlin.math.min
import kotlin.math.roundToLong

class TextUtils {
    companion object {
        private val config = Config.instance()

        /** Overload of [replaceOrderedText] */
        fun replaceOrderedText(original: OrderedText, target: String, replacement: Text): OrderedText {
            val originalTextArray = openOrderedText(original)
            return replaceOrderedText(original, originalTextArray, target, replacement)
        }

        /** Replaces the first occurrence of `target` with `replacement`
         * @return an `OrderedText` with the replacement applied*/
        fun replaceOrderedText(
            originalText: OrderedText,
            originalTextArray: MutableList<Text>, // list of single characters
            target: String,
            replacement: Text
        ): OrderedText {
        // include originalText in case of early return, or if no changes are made

            val targetIndex = originalTextArray.joinToString("") { it.string }.indexOf(target)
            if (targetIndex == -1) return originalText // text does not contain target


            val replacementString = replacement.string
            val targetLength = target.length

            val replacementStyleHasSiblings = replacement.siblings.size >= replacementString.length


            var replacementStrIndex = 0 // index in the replacement string
            var appendedExtraReplacement = false
            val newText = Text.empty()
            var shortOffset = targetLength - replacementString.length


            for ((i, currentCharacter) in originalTextArray.withIndex()) {
                var currentStyle = currentCharacter.style
                var currentStr = currentCharacter.string

                // target.len > replacement.len so we need to remove some characters
                if (replacementStrIndex >= replacementString.length && shortOffset > 0) {
                    shortOffset--
                    currentStr = ""
                }

                // whether to replace character
                if (i > (targetIndex - 1) && replacementStrIndex < min(targetLength, replacementString.length)) {
                    currentStr = replacementString[replacementStrIndex].toString()

                    currentStyle = if (replacementStyleHasSiblings) replacement.siblings[replacementStrIndex].style
                    else replacement.style
                    replacementStrIndex++

                }

                newText.append(Text.literal(currentStr).setStyle(currentStyle))

                // replacement.len > target.len so we need to add extra chars to the text
                if (!appendedExtraReplacement && replacementString.length > targetLength && replacementStrIndex == targetLength) {

                    if (!replacementStyleHasSiblings) {
                        newText.append(Text.literal(replacementString.substring(targetLength)).setStyle(currentStyle))
                    } else {
                        // account for styles of individual chars in replacement
                        for ((replacementIterator, theChar) in replacementString.substring(targetLength).toCharArray()
                            .withIndex()) {
                            newText.append(
                                Text.literal(theChar.toString())
                                    .setStyle(replacement.siblings[replacementIterator + replacementStrIndex].style)
                            )
                        }
                    }

                    appendedExtraReplacement = true
                }
            }

            return if (newText.string.isEmpty()) originalText else newText.asOrderedText()
        }

        /** @return A list of every character inside `text`*/
        fun openOrderedText(text: OrderedText): MutableList<Text> {
            val textArray: MutableList<Text> = mutableListOf()

            text.accept { _: Int, style: Style?, codePoint: Int ->
                val originalStr = Character.toString(codePoint)
                textArray.add(Text.literal(originalStr).setStyle(style))
                true
            }
            return textArray
        }

        /** @return `text` with a chroma effect applied to it*/
        fun dynamicSpectrum(text: Text): Text {
            val originalTextArray = text.string.toCharArray()
            val newText = Text.empty()

            val nanoHue = if (config.reverseChromaDirection) 1 - getChromaHue() else getChromaHue()


            for ((i, char) in originalTextArray.withIndex()) {
                // the difference in colour between each character
                val colorOffset = (i * (config.chromaScale / 100.0) + nanoHue) % 1.0
                val colour = getChromaColourRGB(colorOffset.toFloat())

                newText.append(Text.literal(char.toString()).withColor(colour))
            }
            return newText
        }

        /** @return A Colour hue, based on the current time*/
        private fun getChromaHue(): Float {
            // the time in nanoseconds to cycle the full colour spectrum
            val cycleTime = (1 / (config.chromaSpeed) * 5000000000.0).roundToLong()

            return ((System.nanoTime() % cycleTime).toFloat() / cycleTime)
        }

        /** @param hue The hue of the returned colour
         * @return A Colour in decimal form, based on the values from `config`*/
        private fun getChromaColourRGB(hue: Float): Int {
            return Color.getHSBColor(hue, config.chromaSaturation, config.chromaBrightness).rgb
        }

        /** @return A cycling colour, based on the current time*/
        fun colourSpectrum(): Int {
            return getChromaColourRGB(getChromaHue())
        }
    }
}