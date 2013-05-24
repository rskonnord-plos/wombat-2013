package org.ambraproject.wombat.controller;

import com.google.common.io.Closer;
import org.ambraproject.wombat.service.SoaService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ArticleController {

  @Autowired
  ServletContext servletContext;

  @Autowired
  private SoaService soaService;


  @RequestMapping(value = "/article", method = RequestMethod.GET)
  public void showArticle(HttpServletResponse response) throws IOException {
    Closer closer = Closer.create();
    try {
//      InputStream articleStream = closer.register(soaService.requestStream("assetfiles/10.1371/journal.pone.0000000.xml"));
//      String articleText = IOUtils.toString(articleStream);
      Document articleXml = null;//DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(articleStream);

      InputStream transformStream = closer.register(servletContext.getResourceAsStream("/WEB-INF/views/articleTransform-v3.xsl"));
//      String xformText = IOUtils.toString(transformStream);
      Transformer transformer = TransformerFactory.newInstance().newTemplates(new StreamSource(transformStream)).newTransformer();

      ServletOutputStream responseStream = closer.register(response.getOutputStream());
      transformer.transform(new DOMSource(articleXml), new StreamResult(responseStream));
    } catch (Throwable t) {
      throw closer.rethrow(t);
    } finally {
      closer.close();
    }
  }

}
