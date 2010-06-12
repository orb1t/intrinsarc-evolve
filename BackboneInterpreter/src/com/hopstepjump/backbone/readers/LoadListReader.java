package com.hopstepjump.backbone.readers;

import java.io.*;
import java.util.*;

import com.hopstepjump.backbone.exceptions.*;
import com.hopstepjump.backbone.nodes.*;
import com.hopstepjump.backbone.nodes.converters.*;
import com.hopstepjump.backbone.nodes.insides.*;

public class LoadListReader
{
  private File loadList;
  
  public LoadListReader(File loadList)
  {
    this.loadList = loadList;
  }
  
  public List<BBStratum> readSystem() throws StratumLoadingException, BBNodeNotFoundException, BBVariableNotFoundException 
  {
    if (!loadList.exists())
      throw new StratumLoadingException("Cannot read load list from " + loadList);
    
    BufferedReader buffered;
    try
    {
      buffered = new BufferedReader(new FileReader(loadList));
    }
    catch (FileNotFoundException ex)
    {
      throw new StratumLoadingException("Cannot find file to read load list " + loadList);
    }
    
    try
    {
      Object obj = null; //x.fromXML(buffered);
      if (!(obj instanceof BBLoadList))
        throw new StratumLoadingException("Did not find a load list inside " + loadList);

      BBLoadList node = (BBLoadList) obj;
      List<BBStratumDirectory> directories = new ArrayList<BBStratumDirectory>(node.getTranslatedStrataDirectories());
      // need to load the last first
      Collections.reverse(directories);
      
      // look through each directory and recursively load any stratum files
      List<BBStratum> strata = new ArrayList<BBStratum>();
      for (BBStratumDirectory directory : directories)
      {
        StratumReader reader = new StratumReader(new File(directory.getPath()), true);
        BBStratum stratum = reader.readStratum();        
        strata.add(stratum);
      }			
      
      return strata;
    }
    finally
    {
      try
      {
        buffered.close();
      }
      catch (IOException e)
      {
      }
    }
  }
}
