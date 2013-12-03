package org.bladerunnerjs.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InvalidNameException;

import org.bladerunnerjs.core.console.ConsoleWriter;
import org.bladerunnerjs.core.console.PrintStreamConsoleWriter;
import org.bladerunnerjs.core.log.LogConfiguration;
import org.bladerunnerjs.core.log.Logger;
import org.bladerunnerjs.core.log.LoggerFactory;
import org.bladerunnerjs.core.log.LoggerType;
import org.bladerunnerjs.core.log.SLF4JLoggerFactory;
import org.bladerunnerjs.core.plugin.BRJSPluginLocator;
import org.bladerunnerjs.core.plugin.PluginAccessor;
import org.bladerunnerjs.core.plugin.PluginLocator;
import org.bladerunnerjs.core.plugin.command.CommandList;
import org.bladerunnerjs.model.appserver.ApplicationServer;
import org.bladerunnerjs.model.appserver.BRJSApplicationServer;
import org.bladerunnerjs.model.engine.Node;
import org.bladerunnerjs.model.engine.NodeItem;
import org.bladerunnerjs.model.engine.NodeMap;
import org.bladerunnerjs.model.exception.ConfigException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.exception.command.CommandOperationException;
import org.bladerunnerjs.model.exception.command.NoSuchCommandException;
import org.bladerunnerjs.model.exception.modelupdate.ModelUpdateException;
import org.bladerunnerjs.model.utility.CommandRunner;
import org.bladerunnerjs.model.utility.PluginLocatorLogger;
import org.bladerunnerjs.model.utility.UserCommandRunner;
import org.bladerunnerjs.model.utility.VersionInfo;


public class BRJS extends AbstractBRJSRootNode
{
	public static final String PRODUCT_NAME = "BladeRunnerJS";
	
	public class Messages {
		public static final String PERFORMING_NODE_DISCOVERY_LOG_MSG = "performing node discovery";
		public static final String CREATING_PLUGINS_LOG_MSG = "creating plugins";
		public static final String MAKING_PLUGINS_AVAILABLE_VIA_MODEL_LOG_MSG = "making plugins available via model";
		public static final String PLUGIN_FOUND_MSG = "found plugin %s";
	}
	
	private final NodeMap<App> apps = App.createAppNodeSet();
	private final NodeMap<App> systemApps = App.createSystemAppNodeSet();
	private final NodeItem<JsLib> sdkLib = JsLib.createSdkNodeItem();
	private final NodeMap<JsLib> sdkNonBladeRunnerLibs = JsLib.createSdkNonBladeRunnerLibNodeSet();
	private final NodeItem<DirNode> jsPatches = new NodeItem<>(DirNode.class, "js-patches");
	private final NodeMap<NamedDirNode> templates = new NodeMap<>(NamedDirNode.class, "sdk/templates", "-template$");
	private final NodeItem<DirNode> appJars = new NodeItem<>(DirNode.class, "sdk/libs/java/application");
	private final NodeItem<DirNode> systemJars = new NodeItem<>(DirNode.class, "sdk/libs/java/system");
	private final NodeItem<DirNode> testJars = new NodeItem<>(DirNode.class, "sdk/libs/java/testRunner");
	private final NodeItem<DirNode> userJars = new NodeItem<>(DirNode.class, "conf/java");
	private final NodeItem<DirNode> logs = new NodeItem<>(DirNode.class, "sdk/log");
	private final NodeItem<DirNode> apiDocs = new NodeItem<>(DirNode.class, "sdk/docs/jsdoc");
	private final NodeItem<DirNode> testResults = new NodeItem<>(DirNode.class, "sdk/test-results");
	
	private AssetLocationUtility assetLocator = new AssetLocationUtility();
	
	private final Logger logger;
	private final CommandList commandList;
	private BladerunnerConf bladerunnerConf;
	private TestRunnerConf testRunnerConf;
	private final Map<Integer, ApplicationServer> appServers = new HashMap<Integer, ApplicationServer>();
	private final PluginAccessor pluginAccessor;
	
	public BRJS(File brjsDir, PluginLocator pluginLocator, LoggerFactory loggerFactory, ConsoleWriter consoleWriter)
	{
		super(brjsDir, loggerFactory, consoleWriter);
		
		logger = loggerFactory.getLogger(LoggerType.CORE, BRJS.class);
		
		logger.info(Messages.CREATING_PLUGINS_LOG_MSG);
		pluginLocator.createPlugins(this);
		PluginLocatorLogger.logPlugins(logger, pluginLocator);
		
		logger.info(Messages.PERFORMING_NODE_DISCOVERY_LOG_MSG);
		discoverAllChildren();
		
		logger.info(Messages.MAKING_PLUGINS_AVAILABLE_VIA_MODEL_LOG_MSG);
		
		pluginAccessor = new PluginAccessor(this, pluginLocator);
		commandList = new CommandList(this, pluginLocator.getCommandPlugins());
	}

	public BRJS(File brjsDir, LogConfiguration logConfiguration)
	{
		this(brjsDir, new BRJSPluginLocator(), new SLF4JLoggerFactory(), new PrintStreamConsoleWriter(System.out));
	}

	@Override
	public boolean isRootDir(File dir)
	{
		File sdkDir = new File(dir, "sdk");
		
		return (sdkDir.exists() && sdkDir.isDirectory());
	}
	
	@Override
	public void addTemplateTransformations(Map<String, String> transformations) throws ModelUpdateException {
	}
	
	@Override
	public void create() throws InvalidNameException, ModelUpdateException {
		// do nothing
	}
	
	@Override
	public void populate() throws InvalidNameException, ModelUpdateException {
		try {
			super.populate();
			bladerunnerConf().write();
		}
		catch (ConfigException e) {
			if(e.getCause() instanceof InvalidNameException) {
				throw (InvalidNameException) e.getCause();
			}
			else {
				throw new ModelUpdateException(e);
			}
		}
	}
	
	// TODO: this needs unit testing
	public BundlableNode locateFirstBundlableAncestorNode(File file)
	{
		Node node = locateFirstAncestorNode(file);
		BundlableNode bundlableNode = null;
		
		while((node != null) && (bundlableNode == null))
		{
			if(node instanceof BundlableNode)
			{
				bundlableNode = (BundlableNode) node;
			}
			
			node = node.parentNode();
		}
		
		return bundlableNode;
	}
	
	public List<App> apps()
	{
		return children(apps);
	}
	
	public App app(String appName)
	{
		return child(apps, appName);
	}
	
	public List<App> systemApps()
	{
		return children(systemApps);
	}
	
	public App systemApp(String appName)
	{
		return child(systemApps, appName);
	}
	
	public JsLib sdkLib()
	{
		return item(sdkLib);
	}
	
	public List<JsLib> sdkNonBladeRunnerLibs()
	{
		List<JsLib> typeCastLibs = new ArrayList<JsLib>();
		for (JsLib jsLib : children(sdkNonBladeRunnerLibs))
		{
			typeCastLibs.add(jsLib);
		}
		return typeCastLibs;
	}
	
	public JsLib sdkNonBladeRunnerLib(String libName)
	{
		return child(sdkNonBladeRunnerLibs, libName);
	}
	
	public DirNode jsPatches()
	{
		return item(jsPatches);
	}
	
	public List<NamedDirNode> templates()
	{
		return children(templates);
	}
	
	public NamedDirNode template(String templateName)
	{
		return child(templates, templateName);
	}
	
	// TODO: delete this method -- the test results should live within a generated directory
	public DirNode testResults()
	{
		return item(testResults);
	}
	
	public DirNode appJars()
	{
		return item(appJars);
	}
	
	public DirNode systemJars()
	{
		return item(systemJars);
	}
	
	public DirNode testJars()
	{
		return item(testJars);
	}
	
	public DirNode userJars()
	{
		return item(userJars);
	}
	
	public DirNode logs()
	{
		return item(logs);
	}
	
	public DirNode apiDocs()
	{
		return item(apiDocs);
	}
	
	public VersionInfo versionInfo()
	{
		return new VersionInfo(this);
	}
	
	public File loginRealmConf()
	{
		return new File(dir(), "sdk/loginRealm.conf");
	}
	
	public File usersPropertiesConf()
	{
		return new File(dir(), "conf/users.properties");
	}
	
	public BladerunnerConf bladerunnerConf() throws ConfigException {
		if(bladerunnerConf == null) {
			bladerunnerConf = new BladerunnerConf(this);
		}
		
		return bladerunnerConf;
	}
	
	public TestRunnerConf testRunnerConf() throws ConfigException {
		if(testRunnerConf == null) {
			testRunnerConf = new TestRunnerConf(this);
		}
		
		return testRunnerConf;
	}
	
	public PluginAccessor plugins() {
		return pluginAccessor;
	}
	
	public void runCommand(String... args) throws NoSuchCommandException, CommandArgumentsException, CommandOperationException
	{
		CommandRunner.run(commandList, args);
	}
	
	public void runUserCommand(LogLevelAccessor logLevelAccessor, String... args) throws CommandOperationException
	{
		UserCommandRunner.run(this, commandList, logLevelAccessor, args);
	}
	
	public ApplicationServer applicationServer() throws ConfigException
	{
		return applicationServer( bladerunnerConf().getJettyPort() );
	}
	
	public ApplicationServer applicationServer(int port)
	{
		ApplicationServer appServer = appServers.get(port);
		if (appServer == null)
		{
			appServer = new BRJSApplicationServer(this, port);
			appServers.put(port, appServer);
		}
		return appServer;
	}
	
	public <AF extends AssetFile> List<AF> getAssetFilesNamed(AssetLocation assetLocation, Class<? extends AssetFile> assetFileType, String... fileNames)
	{
		return assetLocator.getAssetFilesNamed(assetLocation, assetFileType, fileNames);
	}
	
	public <AF extends AssetFile> List<AF> getAssetFilesWithExtension(AssetLocation assetLocation, Class<? extends AssetFile> assetFileType, String... extensions)
	{
		return assetLocator.getAssetFilesWithExtension(assetLocation, assetFileType, extensions);
	}
	
	public <AF extends AssetFile> AssetFile getAssetFile(Class<? extends AssetFile> assetFileType, AssetLocation assetLocation, File file) throws UnableToInstantiateAssetFileException
	{
		return assetLocator.getAssetFile(assetFileType, assetLocation, file);
	}
}
