package org.bladerunnerjs.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bladerunnerjs.model.aliasing.AliasDefinition;
import org.bladerunnerjs.model.aliasing.AmbiguousAliasException;
import org.bladerunnerjs.model.aliasing.UnresolvableAliasException;
import org.bladerunnerjs.model.aliasing.aliasdefinitions.AliasDefinitionsFile;
import org.bladerunnerjs.model.aliasing.aliases.AliasesFile;
import org.bladerunnerjs.model.engine.RootNode;
import org.bladerunnerjs.model.exception.AmbiguousRequirePathException;
import org.bladerunnerjs.model.exception.ModelOperationException;
import org.bladerunnerjs.model.exception.RequirePathException;
import org.bladerunnerjs.model.exception.UnresolvableRequirePathException;
import org.bladerunnerjs.model.exception.request.BundlerFileProcessingException;

public abstract class AbstractBundlableNode extends AbstractAssetContainer implements BundlableNode {
	private AliasesFile aliasesFile;
	
	public AbstractBundlableNode(RootNode rootNode, File dir) {
		super(rootNode, dir);
	}
	
	public abstract List<LinkedAsset> getSeedFiles();
	
	@Override
	public List<LinkedAsset> seedFiles() {
		List<LinkedAsset> seedFiles = new ArrayList<>();
		
		seedFiles.addAll(getSeedFiles());
		seedFiles.addAll(resources().seedResources());
		
		return seedFiles;
	}
	
	@Override
	public AliasesFile aliasesFile() {
		if(aliasesFile == null) {
			aliasesFile = new AliasesFile(dir(), "resources/aliases.xml", this);
		}
		
		return aliasesFile;
	}
	
	@Override
	public SourceModule getSourceFile(String requirePath) throws RequirePathException {
		SourceModule sourceFile = null;
		
		for(AssetContainer assetContainer : getAssetContainers()) {
			SourceModule locationSourceFile = assetContainer.sourceFile(requirePath);
			
			if(locationSourceFile != null) {
				if(sourceFile == null) {
					sourceFile = locationSourceFile;
				}
				else {
					throw new AmbiguousRequirePathException("'" + sourceFile.getAssetPath() + "' and '" +
						locationSourceFile.getAssetPath() + "' source files both available via require path '" +
						sourceFile.getRequirePath() + "'.");
				}
			}
		}
		
		if(sourceFile == null) {
			throw new UnresolvableRequirePathException(requirePath);
		}
		
		return sourceFile;
	}
	
	@Override
	public AliasDefinition getAlias(String aliasName) throws UnresolvableAliasException, AmbiguousAliasException, BundlerFileProcessingException {
		return aliasesFile.getAlias(aliasName);
	}
	
	@Override
	public BundleSet getBundleSet() throws ModelOperationException {
		return BundleSetCreator.createBundleSet(this);
	}
	
	@Override
	public List<AliasDefinitionsFile> getAliasDefinitionFiles() {
		List<AliasDefinitionsFile> aliasDefinitionFiles = new ArrayList<>();
		
		for(AssetContainer assetContainer : getAssetContainers()) {
			for(AssetLocation assetLocation : assetContainer.getAllAssetLocations()) {
				aliasDefinitionFiles.add(assetLocation.aliasDefinitionsFile());
			}
		}
		
		return aliasDefinitionFiles;
	}
}
