package com.ownwn

import com.ownwn.config.Config
import net.fabricmc.api.ClientModInitializer

object ClientCustomName : ClientModInitializer {
	const val MODID = "client-custom-name"

	override fun onInitializeClient() {
		Config.load()
	}
}