package org.bladerunnerjs.plugin.bundlers.namespacedjs;

import java.io.IOException;
import java.io.Reader;

import org.bladerunnerjs.utility.reader.AssetReaderFactory;
import org.bladerunnerjs.utility.reader.JsCodeBlockStrippingDependenciesReader;
import org.bladerunnerjs.utility.reader.JsCommentStrippingReader;
import org.bladerunnerjs.utility.reader.JsModuleExportsStrippingReader;

public class NamespacedJsPostExportDefineTimeDependenciesReader extends Reader {
	private Reader namespacedJsPostExportDefineTimeDependenciesReader;
	
	public NamespacedJsPostExportDefineTimeDependenciesReader(NamespacedJsSourceModule sourceModule) throws IOException
	{
		Reader commentStrippingReader = new JsCommentStrippingReader(sourceModule.assetContainer().root(), sourceModule.getUnalteredContentReader(), false);
		Reader codeBlockStrippingReader = new JsCodeBlockStrippingDependenciesReader(sourceModule.assetContainer().root(), commentStrippingReader);
		namespacedJsPostExportDefineTimeDependenciesReader = new JsModuleExportsStrippingReader(sourceModule.assetContainer().root(), codeBlockStrippingReader, false);
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException
	{
		return namespacedJsPostExportDefineTimeDependenciesReader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException
	{
		namespacedJsPostExportDefineTimeDependenciesReader.close();
	}
	
	static class Factory implements AssetReaderFactory {
		
		private NamespacedJsSourceModule sourceModule;

		public Factory(NamespacedJsSourceModule sourceModule)
		{
			this.sourceModule = sourceModule;
		}
		
		@Override
		public Reader createReader() throws IOException {
			return new NamespacedJsPostExportDefineTimeDependenciesReader(sourceModule);
		}
	}
	
}
