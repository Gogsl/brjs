package org.bladerunnerjs.appserver.filter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;

@SuppressWarnings("deprecation")
public class ServletFilterTest {
	protected int serverPort;
	protected HttpClient httpclient;
	protected File contextDir;
	
	@Before
	public void setUp() throws IOException {
		serverPort = generatePortNumber();
		httpclient = new DefaultHttpClient();
		contextDir = Files.createTempDirectory("ServletFilterTest").toFile();
	}

	@After
	public void tearDown() {
		httpclient.getConnectionManager().shutdown();
		contextDir.deleteOnExit();
	}
	
	protected Map<String, String> makeRequest(String url) throws ClientProtocolException, IOException
	{
		Map<String, String> responseMap = new HashMap<String, String>();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		populateResponseDetails(response, responseMap);
		responseMap.put("responseText", EntityUtils.toString(response.getEntity()));
		return responseMap;
	}
	
	protected Map<String, String> makeBinaryRequest(String url, OutputStream outputStream) throws ClientProtocolException, IOException
	{
		Map<String, String> responseMap = new HashMap<String, String>();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		populateResponseDetails(response, responseMap);
		IOUtils.copy(response.getEntity().getContent(), outputStream);
		outputStream.flush();
		return responseMap;
	}
	
	private void populateResponseDetails(HttpResponse response, Map<String, String> responseMap) {
		responseMap.put("responseCode", Integer.toString(response.getStatusLine().getStatusCode()));
		String contentType = (ContentType.get(response.getEntity()) != null) ? ContentType.get(response.getEntity()).getMimeType().toString() : "";
		responseMap.put("responseContentType", contentType);
	}
	
	protected Server createAndStartAppServer(Servlet servlet, Filter filter) throws Exception {
		Map<String,String> emptyMap = Collections.emptyMap();
		return createAndStartAppServer(servlet, filter, emptyMap);
	}
	
	protected Server createAndStartAppServer(Servlet servlet, Filter filter, Map<String,String> filterInitParams) throws Exception {
		Server appServer;
		int attempts = 0;
		while (true) {
			attempts++;
			try {
				serverPort = generatePortNumber();
				appServer = createAppServer(servlet, filter, filterInitParams);
				appServer.start();
				return appServer;
			} catch (SocketException ex) {
				if (attempts > 10) {
					throw ex;
				}
			}
		}
		
	}
	
	protected Server createAppServer(Servlet servlet, Filter filter, Map<String,String> filterInitParams) throws Exception
	{
		Server appServer = new Server(serverPort);
		WebAppContext webappContext = new WebAppContext();
		webappContext.setConfigurationClasses(new String[] {"org.eclipse.jetty.webapp.WebInfConfiguration",
			"org.eclipse.jetty.webapp.WebXmlConfiguration", "org.eclipse.jetty.webapp.MetaInfConfiguration",
			"org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration",
			"org.eclipse.jetty.plus.webapp.PlusConfiguration", "org.eclipse.jetty.webapp.JettyWebXmlConfiguration"});
		webappContext.setResourceBase(contextDir.getPath());
		webappContext.setContextPath("/");
		webappContext.addServlet(new ServletHolder(servlet), "/*");
		FilterHolder filterHolder = new FilterHolder(filter);
		webappContext.addFilter(filterHolder, "/*", null);
		filterHolder.setInitParameters(filterInitParams);
		appServer.setHandler(webappContext);
		
		return appServer;
	}
	
	protected int generatePortNumber()
	{
		return new Random().nextInt(2000)+1000;
	}
}
