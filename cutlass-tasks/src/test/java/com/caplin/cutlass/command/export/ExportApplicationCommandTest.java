package com.caplin.cutlass.command.export;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.model.exception.command.NodeDoesNotExistException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.sinbin.CutlassConfig;
import org.bladerunnerjs.model.utility.FileUtility;

import com.caplin.cutlass.BRJSAccessor;
import com.caplin.cutlass.testing.BRJSTestFactory;

public class ExportApplicationCommandTest 
{

	private File testFolder = new File("src/test/resources/ExportApplicationCommandTest");
	private File sdkBaseDir, tempDirRoot = null;
	ExportApplicationCommand exportCommand;
	
	@Before
	public void setUp() throws IOException
	{
		tempDirRoot = FileUtility.createTemporaryDirectory(this.getClass().getSimpleName());
		FileUtils.copyDirectory(testFolder, tempDirRoot);
		
		sdkBaseDir = new File(tempDirRoot, "structure1" + File.separator + CutlassConfig.SDK_DIR);
		BRJS brjs = BRJSAccessor.initialize(BRJSTestFactory.createBRJS(sdkBaseDir.getParentFile()));
		
		exportCommand = new ExportApplicationCommand(brjs);
	}
	
	@Test
	public void exportApplicationAndVerifyTheZipContents() throws Exception 
	{
		
		File appDir = new File(sdkBaseDir.getParentFile(), CutlassConfig.APPLICATIONS_DIR + File.separator + "example");
		File bundlesDir = new File(appDir, "fx-bladeset/blades/grid/tests/test-acceptance/js-test-driver/bundles");
		
		// Zip
		File zipFileLocation = new File(sdkBaseDir, "example.zip");
		
		File extractLocation = FileUtility.createTemporaryDirectory("extracted");
		
		assertTrue(bundlesDir.exists());
		assertTrue(zipFileLocation.exists() == false);
		
		exportCommand.doCommand(new String[] {"example", "aDisclaimer"});		
		
		assertTrue(zipFileLocation.exists());
		
		ZipFile createdZip = new ZipFile(zipFileLocation);
		FileUtility.unzip(createdZip, extractLocation);
		
		// calplin jars are removed and the 'bundles' folder is excluded
		assertTrue(new File(extractLocation, "example/WEB-INF/lib/app-1.jar").exists());
		assertFalse(new File(extractLocation, "example/WEB-INF/lib/brjs-1.jar").exists());
		assertFalse(new File(extractLocation, "example/WEB-INF/lib/brjs-2.jar").exists());
		assertTrue(new File(extractLocation, "example/fx-bladeset/blades/grid/tests/test-acceptance/js-test-driver").exists());
		assertFalse(new File(extractLocation, "example/fx-bladeset/blades/grid/tests/test-acceptance/js-test-driver/bundles").exists());
		assertFalse(new File(extractLocation, "example/fx-bladeset/blades/grid/tests/test-acceptance/js-test-driver/bundles/js").exists());
		assertTrue(new File(extractLocation, "example/libs/myLib/src").exists());
		assertFalse(new File(extractLocation, "example/libs/myLib/test").exists());
		
		assertEquals(FileUtils.readFileToString(new File(extractLocation, "example/libs/myLib/src/aSrcFile.js")), "/*\n* aDisclaimer\n*/\n\n");
	}
	
	@Test
	public void defaultDisclaimerIsUsedIfnoDisclaimerIsPassedAsParameter() throws Exception
	{
		// Zip
		File zipFileLocation = new File(sdkBaseDir, "example.zip");
		
		File extractLocation = FileUtility.createTemporaryDirectory("extracted");
		
		exportCommand.doCommand(new String[] {"example"});	
		ZipFile createdZip = new ZipFile(zipFileLocation);
		FileUtility.unzip(createdZip, extractLocation);
		
		assertEquals(FileUtils.readFileToString(new File(extractLocation, "example/libs/myLib/src/aSrcFile.js")), "/*\n* Do not edit this file; edits will be lost after upgrades.\n*/\n\n");
	}
	
	@Test (expected=CommandArgumentsException.class)
	public void commandThrowsErrorIfUsingAdditionalArguments() throws Exception
	{
		exportCommand.doCommand(new String[] { "example", "disclaimer", "extra" });
	}
	
	@Test (expected=CommandArgumentsException.class)
	public void commandThrowsErrorIfAppNameIsNotProvided() throws Exception
	{
		exportCommand.doCommand(new String[] {});
	}
	
	@Test (expected=NodeDoesNotExistException.class)
	public void commandThrowsErrorIfSpecifiedAppDoesNotExist() throws Exception
	{
		exportCommand.doCommand(new String[] { "doesnotexist" });
	}

}