package com.ownwn

import com.ownwn.config.Config
import net.fabricmc.api.ClientModInitializer

object HypixelCustomName : ClientModInitializer {
	const val MODID = "hypixel-custom-name"

	override fun onInitializeClient() {
		Config.load()
	}
}