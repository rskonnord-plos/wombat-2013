package org.ambraproject.wombat.config;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Configuration for the webapp's runtime behavior. Because most configuration of application behavior should be behind
 * the service layer, this class ought to be concerned only with the minimal set of values that concern how this Spring
 * app interacts with the service API.
 *
 * @see SpringConfiguration#runtimeConfiguration
 */
public class RuntimeConfiguration {

  private static final Logger log = LoggerFactory.getLogger(RuntimeConfiguration.class);

  /**
   * @deprecated should only be called reflectively by Gson
   */
  @Deprecated
  public RuntimeConfiguration() {
  }

  // Fields are immutable by convention. They should be modified only during deserialization.
  private String server;
  private Boolean trustUnsignedServer;
  private String localFrontEndPath;

  /**
   * Validate values after deserializing.
   *
   * @throws RuntimeConfigurationException if a value is invalid
   */
  public void validate() {
    if (Strings.isNullOrEmpty(server)) {
      throw new RuntimeConfigurationException("Server address required");
    }
    try {
      new URL(server);
    } catch (MalformedURLException e) {
      throw new RuntimeConfigurationException("Provided server address is not a valid URL", e);
    }
  }

  /**
   * Check whether this webapp is configured to naively trust servers without SSL certificates. This should be {@code
   * true} only when debugging or connecting to a private testing server. Defaults to {@code false}.
   *
   * @return {@code false} if default SSL authentication should be preserved
   */
  public boolean trustUnsignedServer() {
    return (trustUnsignedServer == null) ? false : trustUnsignedServer;
  }

  /**
   * Get the URL of the SOA server.
   *
   * @return the URL
   */
  public URL getServer() {
    try {
      return new URL(server);
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Invalid URL should have been caught at validation", e);
    }
  }

  /**
   * Get the local file system path at which this webapp may cache front-end data downloaded from the service API. If
   * none is provided in the config file, defaults to a system-provided temporary directory.
   *
   * @return the path
   */
  public File getLocalFrontEndPath() {
    if (localFrontEndPath == null) {
      File tempDir = Files.createTempDir();
      log.warn("Using temporary directory as default for localFrontEndPath: {}", tempDir.getAbsolutePath());
      return tempDir;
    }

    File path = new File(localFrontEndPath);
    if (!path.exists()) {
      throw new RuntimeConfigurationException("Path does not exist: " + localFrontEndPath);
    }
    if (!path.isDirectory()) {
      throw new RuntimeConfigurationException("Path is not a directory: " + localFrontEndPath);
    }
    return path;
  }

  /*
   * For debugger-friendliness only. If there is a need to serialize back to JSON in production, it would be more
   * efficient to use the Gson bean.
   */
  @Override
  public String toString() {
    return new GsonBuilder().create().toJson(this);
  }

}
