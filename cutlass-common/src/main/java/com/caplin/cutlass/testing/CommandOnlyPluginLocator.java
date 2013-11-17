package com.caplin.cutlass.testing;

import java.util.ArrayList;
import java.util.List;

import org.bladerunnerjs.core.plugin.BRJSPluginLocator;
import org.bladerunnerjs.core.plugin.ModelObserverPlugin;
import org.bladerunnerjs.core.plugin.PluginLocator;
import org.bladerunnerjs.core.plugin.bundler.BundlerPlugin;
import org.bladerunnerjs.core.plugin.command.CommandPlugin;
import org.bladerunnerjs.model.BRJS;

public class CommandOnlyPluginLocator implements PluginLocator {
	private final PluginLocator brjsPluginLocator = new BRJSPluginLocator();
	
	@Override
	public void createPlugins(BRJS brjs) {
		brjsPluginLocator.createPlugins(brjs);
	}
	
	@Override
	public List<CommandPlugin> getCommandPlugins() {
		return brjsPluginLocator.getCommandPlugins();
	}
	
	@Override
	public List<ModelObserverPlugin> getModelObservers() {
		return new ArrayList<>();
	}
	
	@Override
	public List<BundlerPlugin> getBundlerPlugins() {
		return new ArrayList<>();
	}
}
