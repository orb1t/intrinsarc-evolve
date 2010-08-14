package com.intrinsarc.carrentalgui.server;

import com.intrinsarc.backbone.runtime.api.*;

/** generated by Evolve */
public class CarRentalServiceFactory implements IHardcodedFactory
{
  private java.util.List<IHardcodedFactory> children;

 // parts
  private com.intrinsarc.carrentalgui.server.CarRental x = new com.intrinsarc.carrentalgui.server.CarRental();
  public com.intrinsarc.carrentalgui.client.IRentalService getService() { return x.getService_IRentalService(null); }

  public CarRentalServiceFactory() {}

  public CarRentalServiceFactory initialize(IHardcodedFactory parent, java.util.Map<String, Object> values)
  {
    return this;
  }
  public void childDestroyed(IHardcodedFactory child) { children.remove(child); }

  public void destroy()
  {
  }

  static void destroyChildren(IHardcodedFactory parent, IHardcodedFactory me, java.util.List<IHardcodedFactory> children)
  {
    parent.childDestroyed(me);
    if (children != null) {
      java.util.List<IHardcodedFactory> copy = new java.util.ArrayList<IHardcodedFactory>(children);
      java.util.Collections.reverse(copy);
      for (IHardcodedFactory f : copy)
        f.destroy();
    }
  }
}