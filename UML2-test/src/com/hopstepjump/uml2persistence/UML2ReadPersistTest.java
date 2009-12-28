package com.hopstepjump.uml2persistence;

import java.io.*;
import java.util.*;

import javax.jdo.*;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.emf.ecore.xmi.impl.*;
import org.eclipse.emf.edit.domain.*;
import org.eclipse.emf.edit.provider.*;
import org.eclipse.emf.edit.provider.resource.*;
import org.eclipse.uml2.*;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.impl.*;

import com.hopstepjump.notifications.*;

class MyObserver implements Adapter
{
  public void notifyChanged(Notification arg)
  {
    System.out.println("  $$$$$$$$$$$$$$$$$ got an adaptation... :" + arg);
  }

  public Notifier getTarget()
  {
    return null;
  }

  public void setTarget(Notifier arg0)
  {
  }

  public boolean isAdapterForType(Object arg0)
  {
    return false;
  }
  
}


public class UML2ReadPersistTest
{

  private final static boolean CLIENT_SERVER = true;
  private PersistenceManager pm;

  public static void main(String[] args) throws IOException
  {
    new UML2ReadPersistTest().go();
  }
  
  private void go() throws IOException
  {
    pm = setUpPersistenceManager();
    setUpEditingDomain();
    
    // make a package with a class
    
    // read all packages
    GlobalNotifier.getSingleton().addObserver(new MyObserver());
    Extent e = pm.getExtent(PackageImpl.class, true);
    Iterator i = e.iterator();
    start();
    while (i.hasNext())
    {
      Package p = (Package) i.next();
      
      System.out.println("$$ read package: " + p.getName() + ", is deleted = " + p.getJ_deleted());
      for (Iterator o = p.getOwnedElements().iterator(); o.hasNext();)
      {
        Object obj = o.next();
        if (obj instanceof NamedElement)
        {
          NamedElement element = (NamedElement) obj;
          System.out.println("   $$ has owned element: " + element.getName());
          EClass ecls = element.eClass();
          System.out.println("     $$ eclass = " + ecls.getName() + ", attrs = " + ecls.getFeatureCount());
//          for (TreeIterator iter = ecls.eAllContents(); iter.hasNext();)
//          {
//            System.out.println("      $$ all contents, element = " + iter.next());
//          }
        }
        else
        if (obj instanceof Comment)
        {
          Comment comment = (Comment) obj;
          System.out.println("   $$ has comment: " + comment.getBody());
        }
      }
    }
    
    e.close(i);
    pm.currentTransaction().rollback();

    System.out.println("$$ successfully done");
  }


  private void end()
  {
    pm.currentTransaction().commit();
  }

  private void middle()
  {
    end();
    start();
  }

  private void start()
  {
    pm.currentTransaction().begin();
  }

  /**
   * @return
   */
  private EditingDomain setUpEditingDomain()
  {
    // register the XMI factory
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
    
    // set up the factories and undo/redo infrastructure
    // Create an adapter factory that yields item providers.
    List factories = new ArrayList();
    factories.add(new ResourceItemProviderAdapterFactory());
    factories.add(new ReflectiveItemProviderAdapterFactory());
    ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);
    EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, null, new HashMap());
    return domain;
  }

  /**
   * @return
   */
  private PersistenceManager setUpPersistenceManager()
  {
    // Obtain a database connection:
    Properties properties = new Properties();
    properties.setProperty("javax.jdo.PersistenceManagerFactoryClass", "com.objectdb.jdo.PMF");
    if (CLIENT_SERVER)
    {
      properties.setProperty("javax.jdo.option.ConnectionURL", "objectdb://localhost/uml2.odb");
      properties.setProperty("javax.jdo.option.ConnectionUserName", "admin");
      properties.setProperty("javax.jdo.option.ConnectionPassword", "admin");
    }
    else
    {
      properties.setProperty("javax.jdo.option.ConnectionURL", "uml2.odb");
    }

    PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(properties, JDOHelper.class.getClassLoader());
    PersistenceManager pm = pmf.getPersistenceManager();
    return pm;
  }
  
  /**
   * @return
   */
  private Resource createXMIResource(String fileName)
  {
    // create the resource where Foo will live
    ResourceSet resourceSet = new ResourceSetImpl();
    URI fileURI = URI.createFileURI(new File(fileName).getAbsolutePath());
    Resource resource = resourceSet.createResource(fileURI);
    return resource;
  }  
}

