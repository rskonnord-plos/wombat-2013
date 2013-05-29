package org.ambraproject.wombat.controller;

import org.ambraproject.wombat.FrontEndBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class ArticleController {

  @Autowired
  private FrontEndBundle frontEndBundle;

  @RequestMapping(value = "/article", method = RequestMethod.GET)
  public ResponseEntity<String> fetch() throws IOException {
//    return new ResponseEntity<String>(frontEndBundle.saveBundles(), HttpStatus.OK);
    return null;
  }

}
