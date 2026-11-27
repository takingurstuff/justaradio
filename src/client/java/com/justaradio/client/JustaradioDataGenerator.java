package com.justaradio.client;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import com.justaradio.client.datagenproviders.JustaradioModelProvider;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;

public class JustaradioDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(JustaradioModelProvider::new);
		
	}
}
