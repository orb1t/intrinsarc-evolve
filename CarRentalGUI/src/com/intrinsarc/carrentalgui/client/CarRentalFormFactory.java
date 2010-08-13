package com.intrinsarc.carrentalgui.client;

import com.intrinsarc.backbone.runtime.api.*;

/** generated by Evolve */
public class CarRentalFormFactory implements IHardcodedFactory
{
  private java.util.List<IHardcodedFactory> children;

  // attributes
  private Attribute<Integer> spacing = new Attribute<Integer>(10);
  private Attribute<Integer> spacing1 = new Attribute<Integer>(10);
  private Attribute<Integer> spacing2 = new Attribute<Integer>(10);
  private Attribute<String> width = new Attribute<String>("300px");
  private Attribute<Integer> spacing3 = new Attribute<Integer>(10);
  private Attribute<String> text = new Attribute<String>("Add new car:");

  // connectors
  private com.intrinsarc.backbone.runtime.api.ICreate c;
  private com.google.gwt.user.client.ui.TextBox c1;
  private com.google.gwt.user.client.ui.Label c2;
  private com.google.gwt.user.client.ui.TextBox c3;
  private com.google.gwt.event.dom.client.ChangeHandler c4;
  private com.google.gwt.user.client.ui.HorizontalPanel c5;
  private com.google.gwt.user.client.ui.VerticalPanel c6;
  private com.google.gwt.user.client.ui.VerticalPanel c7;
  private com.google.gwt.user.client.ui.VerticalPanel c8;
  private com.google.gwt.user.client.ui.DecoratorPanel c9;
  private com.google.gwt.user.client.ui.DecoratorPanel c10;

 // parts
  private com.google.gwt.user.client.ui.DecoratorPanel x16 = new com.google.gwt.user.client.ui.DecoratorPanel();
  private com.google.gwt.user.client.ui.VerticalPanel x1 = new com.google.gwt.user.client.ui.VerticalPanel();
  private com.google.gwt.user.client.ui.VerticalPanel x2 = new com.google.gwt.user.client.ui.VerticalPanel();
  private com.google.gwt.user.client.ui.DecoratorPanel x15 = new com.google.gwt.user.client.ui.DecoratorPanel();
  private com.google.gwt.user.client.ui.VerticalPanel x12 = new com.google.gwt.user.client.ui.VerticalPanel();
  private com.intrinsarc.carrentalgui.client.GUILogic x3 = new com.intrinsarc.carrentalgui.client.GUILogic();
  private com.google.gwt.user.client.ui.HorizontalPanel x = new com.google.gwt.user.client.ui.HorizontalPanel();
  private com.google.gwt.user.client.ui.Label x7 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x5 = new com.google.gwt.user.client.ui.TextBox();
  private ICreate factory = new ICreate() {
    public Object create(java.util.Map<String, Object> values) {
      CarWidgetFactory f = new CarWidgetFactory();
      f.initialize(CarRentalFormFactory.this, values);
      if (children == null)
        children = new java.util.ArrayList<IHardcodedFactory>();
      children.add(f);

      return f;
    }
    public void destroy(Object memento) { ((IHardcodedFactory) memento).destroy(); }
  };
  public com.google.gwt.user.client.ui.VerticalPanel getPanel() { return x2; }
  public void setService(com.intrinsarc.carrentalgui.client.IRentalServiceAsync val)  { x3.setService_IRentalServiceAsync(val); };

  public CarRentalFormFactory() {}

  public CarRentalFormFactory initialize(IHardcodedFactory parent, java.util.Map<String, Object> values)
  {
    x1.setSpacing(spacing.get());
    x2.setSpacing(spacing1.get());
    x12.setSpacing(spacing2.get());
    x.setWidth(width.get());
    x.setSpacing(spacing3.get());
    x7.setText(text.get());
    c = factory;
    c1 = x5;
    c2 = x7;
    c3 = x5;
    c4 = x3.getChange_ChangeHandler(com.google.gwt.event.dom.client.ChangeHandler.class);
    c5 = x;
    c6 = x12;
    c7 = x12;
    c8 = x1;
    c9 = x15;
    c10 = x16;
    x.add(c2);
    x1.add(c5);
    x2.add(c10);
    x.add(c1);
    x2.add(c9);
    x3.setCreate_ICreate(c);
    x3.setText_TextBox(c3);
    x5.addChangeHandler(c4);
    x3.setPanel_Panel(c6);
    x15.setWidget(c7);
    x16.setWidget(c8);
    x3.afterInit();
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

// flattened factories
class CarWidgetFactory implements IHardcodedFactory
{
  private IHardcodedFactory parent;
  private java.util.List<IHardcodedFactory> children;

  // attributes
  private Attribute<Integer> number = new Attribute<Integer>(0);
  public void setNumber(int number) { this.number.set(number); }
  public int getNumber() { return number.get(); }
  private Attribute<Boolean> returnEnabled = new Attribute<Boolean>(true);
  public void setReturnEnabled(boolean returnEnabled) { this.returnEnabled.set(returnEnabled); }
  public boolean getReturnEnabled() { return returnEnabled.get(); }
  private Attribute<String> model = new Attribute<String>(null);
  public void setModel(String model) { this.model.set(model); }
  public String getModel() { return model.get(); }
  private Attribute<Boolean> rentEnabled = new Attribute<Boolean>(true);
  public void setRentEnabled(boolean rentEnabled) { this.rentEnabled.set(rentEnabled); }
  public boolean getRentEnabled() { return rentEnabled.get(); }
  private Attribute<String> width1 = new Attribute<String>("300px");
  private Attribute<Integer> spacing4 = new Attribute<Integer>(10);
  private Attribute<String> width2 = new Attribute<String>("100px");
  private Attribute<String> text1 = new Attribute<String>("rent");
  private Attribute<String> text2 = new Attribute<String>("return");

  // connectors
  private com.google.gwt.user.client.ui.ClickListener c11;
  private com.google.gwt.user.client.ui.ClickListener c12;
  private com.google.gwt.user.client.ui.Button c13;
  private com.google.gwt.user.client.ui.Button c14;
  private com.google.gwt.user.client.ui.Label c15;
  private com.google.gwt.user.client.ui.HorizontalPanel c16;

 // parts
  private com.google.gwt.user.client.ui.HorizontalPanel x20 = new com.google.gwt.user.client.ui.HorizontalPanel();
  private com.google.gwt.user.client.ui.Label x27 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.Button x25 = new com.google.gwt.user.client.ui.Button();
  private com.google.gwt.user.client.ui.Button x23 = new com.google.gwt.user.client.ui.Button();

  public CarWidgetFactory() {}

  public CarWidgetFactory initialize(IHardcodedFactory parent, java.util.Map<String, Object> values)
  {
    this.parent = parent;
    if (values != null && values.containsKey("number")) number = new Attribute<Integer>((Integer) values.get("number"));
    if (values != null && values.containsKey("returnEnabled")) returnEnabled = new Attribute<Boolean>((Boolean) values.get("returnEnabled"));
    if (values != null && values.containsKey("model")) model = new Attribute<String>((String) values.get("model"));
    if (values != null && values.containsKey("rentEnabled")) rentEnabled = new Attribute<Boolean>((Boolean) values.get("rentEnabled"));
    x20.setWidth(width1.get());
    x20.setSpacing(spacing4.get());
    x27.setWidth(width2.get());
    x27.setText(model.get());
    x25.setEnabled(rentEnabled.get());
    x25.setText(text1.get());
    x23.setEnabled(returnEnabled.get());
    x23.setText(text2.get());
    c11 = x3.getReturn_ClickListener(com.google.gwt.user.client.ui.ClickListener.class, -1);
    c12 = x3.getRent_ClickListener(com.google.gwt.user.client.ui.ClickListener.class, -1);
    c13 = x23;
    c14 = x25;
    c15 = x27;
    c16 = x20;
    x12.add(c16);
    x20.add(c15);
    x20.add(c14);
    x20.add(c13);
    x23.addClickListener(c11);
    x25.addClickListener(c12);
    return this;
  }
  public void childDestroyed(IHardcodedFactory child) { children.remove(child); }

  public void destroy()
  {
    destroyChildren(parent, this, children);
    x12.remove(c16);
    x20.remove(c15);
    x20.remove(c14);
    x20.remove(c13);
    x23.removeClickListener(c11);
    x25.removeClickListener(c12);
  }

}
}
