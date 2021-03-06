package org.bladerunnerjs.plugin.plugins.require;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bladerunnerjs.api.Asset;
import org.bladerunnerjs.api.memoization.MemoizedFile;
import org.bladerunnerjs.api.model.exception.ModelOperationException;
import org.bladerunnerjs.model.AssetContainer;
import org.bladerunnerjs.api.BundlableNode;
import org.bladerunnerjs.plugin.bundlers.commonjs.CommonJsSourceModule;

public class ServiceDataSourceModule implements CommonJsSourceModule {
	
	private final BundlableNode bundlableNode;
	public static final String PRIMARY_REQUIRE_PATH = "service!$data"; 

	public ServiceDataSourceModule(BundlableNode bundlableNode) {
		this.bundlableNode = bundlableNode;
	}

	@Override
	public void addImplicitDependencies(List<Asset> implicitDependencies) {
	}
	
	@Override
	public List<Asset> getDependentAssets(BundlableNode bundlableNode) throws ModelOperationException {
		return Collections.emptyList();
	}

	@Override
	public Reader getReader() throws IOException {
		try
		{
			return new StringReader(
					String.format(COMMONJS_DEFINE_BLOCK_HEADER, getPrimaryRequirePath()) +
					"module.exports = " + ServiceDataSerializer.createJson(bundlableNode.getBundleSet()) + ";" +
					COMMONJS_DEFINE_BLOCK_FOOTER
				);
		}
		catch (ModelOperationException e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public MemoizedFile file() {
		return bundlableNode.dir();
	}

	@Override
	public String getAssetName() {
		return getAssetPath();
	}

	@Override
	public String getAssetPath() {
		return getPrimaryRequirePath();
	}

	@Override
	public List<String> getRequirePaths() {
		return Arrays.asList(PRIMARY_REQUIRE_PATH);
	}

	@Override
	public String getPrimaryRequirePath() {
		return PRIMARY_REQUIRE_PATH;
	}

	@Override
	public boolean isEncapsulatedModule() {
		return true;
	}

	@Override
	public boolean isGlobalisedModule() {
		return false;
	}

	@Override
	public List<Asset> getPreExportDefineTimeDependentAssets(BundlableNode bundlableNode) throws ModelOperationException {
		return Collections.emptyList();
	}

	@Override
	public List<Asset> getPostExportDefineTimeDependentAssets(BundlableNode bundlableNode) throws ModelOperationException {
		return Collections.emptyList();
	}

	@Override
	public List<Asset> getUseTimeDependentAssets(BundlableNode bundlableNode) throws ModelOperationException {
		return Collections.emptyList();
	}

	@Override
	public AssetContainer assetContainer()
	{
		return bundlableNode;
	}
	
	@Override
	public boolean isScopeEnforced() {
		return false;
	}
	
	@Override
	public boolean isRequirable()
	{
		return true;
	}
}
