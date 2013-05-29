package org.ambraproject.wombat;

import com.google.common.io.Closer;
import org.ambraproject.wombat.config.RuntimeConfiguration;
import org.ambraproject.wombat.service.SoaService;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class FrontEndBundle {

  @Autowired
  private RuntimeConfiguration runtimeConfiguration;
  @Autowired
  private SoaService soaService;

  public void saveBundles() throws IOException {
    Set<String> journalKeys = soaService.requestObject("journals", Map.class).keySet();
    for (String journalKey : journalKeys) {
      saveBundle(journalKey);
    }
  }

  private void saveBundle(String journalKey) throws IOException {
    String bundleAddress = String.format("journals/%s?fend", journalKey);
    soaService.requestStream(bundleAddress);
  }

  private static void archiveToy() throws IOException {
    Closer closer = Closer.create();
    try {
      InputStream bundleStream = closer.register(someStream());
      GzipCompressorInputStream gzipStream = closer.register(new GzipCompressorInputStream(bundleStream));
      TarArchiveInputStream tarStream = closer.register(new TarArchiveInputStream(gzipStream));
      TarArchiveEntry tarEntry;
      while ((tarEntry = tarStream.getNextTarEntry()) != null) {
        writeToLocalDisk(tarEntry);
      }
    } catch (Throwable t) {
      throw closer.rethrow(t);
    } finally {
      closer.close();
    }
  }

  private static void writeToLocalDisk(TarArchiveEntry tarEntry) {
    //To change body of created methods use File | Settings | File Templates.
  }

  private static InputStream someStream() throws FileNotFoundException {
    return new FileInputStream(new File("/home/rskonnord/sample.txt.gz"));
  }

  public static void main(String[] args) {
    try {
      archiveToy();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

}
