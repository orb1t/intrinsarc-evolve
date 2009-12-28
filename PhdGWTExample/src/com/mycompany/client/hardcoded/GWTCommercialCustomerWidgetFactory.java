package com.mycompany.client.hardcoded;

import com.hopstepjump.backbone.runtime.api.*;

/** generated by Evolve */
public class GWTCommercialCustomerWidgetFactory
{
  private Attribute<String> text = new Attribute<String>("<b>Enter full name</b>");
  private Attribute<String> text1 = new Attribute<String>("<b>Enter normal address</b>");

  private Attribute<String> title = new Attribute<String>("Customer details");
  private Attribute<String> text2 = new Attribute<String>("Enter billing address");
  private Attribute<String> text3 = new Attribute<String>("First:");
  private Attribute<String> text4 = new Attribute<String>("Last");
  private Attribute<String> text5 = new Attribute<String>("Street:");
  private Attribute<String> text6 = new Attribute<String>("Town:");

  private com.google.gwt.user.client.ui.HorizontalPanel h = new com.google.gwt.user.client.ui.HorizontalPanel();
  private com.mycompany.client.widgets.SimpleTabPanel x = new com.mycompany.client.widgets.SimpleTabPanel();
  private com.mycompany.client.widgets.ButtonLogic l = new com.mycompany.client.widgets.ButtonLogic();
  private com.google.gwt.user.client.ui.Button b = new com.google.gwt.user.client.ui.Button();
  private com.google.gwt.user.client.ui.VerticalPanel x1 = new com.google.gwt.user.client.ui.VerticalPanel();
  private com.google.gwt.user.client.ui.Label x2 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x3 = new com.google.gwt.user.client.ui.TextBox();
  private com.google.gwt.user.client.ui.Label x4 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x5 = new com.google.gwt.user.client.ui.TextBox();
  private com.google.gwt.user.client.ui.HTML x6 = new com.google.gwt.user.client.ui.HTML();
  private com.google.gwt.user.client.ui.VerticalPanel x7 = new com.google.gwt.user.client.ui.VerticalPanel();
  private com.google.gwt.user.client.ui.Label x8 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x9 = new com.google.gwt.user.client.ui.TextBox();
  private com.google.gwt.user.client.ui.Label x10 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x11 = new com.google.gwt.user.client.ui.TextBox();
  private com.google.gwt.user.client.ui.HTML x12 = new com.google.gwt.user.client.ui.HTML();
  private ICreate factory = new ICreate() {
    public Object create(java.util.Map<String, Object> values) {
      GWTAddressWidgetFactory f = new GWTAddressWidgetFactory();
      f.initialize(values);
      return f;
    }
    public void destroy(Object memento) {}
  };
  public com.google.gwt.user.client.ui.TabPanel getPanel() { return x; }

  public GWTCommercialCustomerWidgetFactory() {}

  public GWTCommercialCustomerWidgetFactory initialize(java.util.Map<String, Object> values)
  {
    h.setTitle(title.get());
    b.setText(text2.get());
    x2.setText(text3.get());
    x4.setText(text4.get());
    x6.setHTML(text.get());
    x8.setText(text5.get());
    x10.setText(text6.get());
    x12.setHTML(text1.get());
    com.hopstepjump.backbone.runtime.api.ICreate c = factory;
    com.google.gwt.user.client.ui.HTML c1 = x12;
    com.google.gwt.user.client.ui.TextBox c2 = x11;
    com.google.gwt.user.client.ui.Label c3 = x10;
    com.google.gwt.user.client.ui.TextBox c4 = x9;
    com.google.gwt.user.client.ui.Label c5 = x8;
    com.google.gwt.user.client.ui.VerticalPanel c6 = x7;
    com.google.gwt.user.client.ui.HTML c7 = x6;
    com.google.gwt.user.client.ui.TextBox c8 = x5;
    com.google.gwt.user.client.ui.Label c9 = x4;
    com.google.gwt.user.client.ui.TextBox c10 = x3;
    com.google.gwt.user.client.ui.Label c11 = x2;
    com.google.gwt.user.client.ui.VerticalPanel c12 = x1;
    com.google.gwt.user.client.ui.ClickListener c13 = l.getListener_ClickListener(com.google.gwt.user.client.ui.ClickListener.class);
    com.google.gwt.user.client.ui.ButtonBase c14 = b;
    com.google.gwt.user.client.ui.HorizontalPanel c15 = h;
    x7.add(c1);
    x1.add(c7);
    h.add(c12);
    x7.add(c5);
    h.add(c6);
    x1.add(c11);
    x7.add(c4);
    x1.add(c10);
    h.add(c14);
    x7.add(c3);
    x1.add(c9);
    x7.add(c2);
    x1.add(c8);
    l.setCreate_ICreate(c);
    b.addClickListener(c13);
    x.addTitleTab(c15);
    x.afterInit();
    return this;
  }
class GWTAddressWidgetFactory
{
  private Attribute<String> text7 = new Attribute<String>("<b>Enter billing address</b>");
  private Attribute<String> title1 = new Attribute<String>("Billing address");
  private Attribute<String> text8 = new Attribute<String>("Street:");
  private Attribute<String> text9 = new Attribute<String>("Town:");

  private com.google.gwt.user.client.ui.HorizontalPanel x27 = new com.google.gwt.user.client.ui.HorizontalPanel();
  private com.google.gwt.user.client.ui.VerticalPanel x28 = new com.google.gwt.user.client.ui.VerticalPanel();
  private com.google.gwt.user.client.ui.Label x29 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x30 = new com.google.gwt.user.client.ui.TextBox();
  private com.google.gwt.user.client.ui.Label x31 = new com.google.gwt.user.client.ui.Label();
  private com.google.gwt.user.client.ui.TextBox x32 = new com.google.gwt.user.client.ui.TextBox();
  private com.google.gwt.user.client.ui.HTML x33 = new com.google.gwt.user.client.ui.HTML();

  public GWTAddressWidgetFactory() {}

  public GWTAddressWidgetFactory initialize(java.util.Map<String, Object> values)
  {
    x27.setTitle(title1.get());
    x29.setText(text8.get());
    x31.setText(text9.get());
    x33.setHTML(text7.get());
    com.google.gwt.user.client.ui.HTML c16 = x33;
    com.google.gwt.user.client.ui.TextBox c17 = x32;
    com.google.gwt.user.client.ui.Label c18 = x31;
    com.google.gwt.user.client.ui.TextBox c19 = x30;
    com.google.gwt.user.client.ui.Label c20 = x29;
    com.google.gwt.user.client.ui.VerticalPanel c21 = x28;
    com.google.gwt.user.client.ui.HorizontalPanel c22 = x27;
    x28.add(c16);
    x27.add(c21);
    x28.add(c20);
    x28.add(c19);
    x28.add(c18);
    x.addTitleTab(c22);
    x28.add(c17);
    return this;
  }
}
}
