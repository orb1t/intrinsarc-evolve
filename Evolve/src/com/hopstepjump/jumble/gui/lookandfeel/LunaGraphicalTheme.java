package com.hopstepjump.jumble.gui.lookandfeel;

import java.util.*;

import javax.swing.*;

import com.hopstepjump.swing.*;
import com.jtattoo.plaf.*;
import com.jtattoo.plaf.luna.*;

public class LunaGraphicalTheme implements GraphicalTheme
{
  public static final String THEME_NAME = "Luna";

	public void change(String subtheme) throws Exception
  {
    // setup the look and feel properties    
    Properties props = SmartGraphicalTheme.setOptions(subtheme);
    changeWindowIcons();

    // set the theme properties
    LunaLookAndFeel.setTheme(subtheme);
    BaseTheme.setProperties(props);

    // select the Look and Feel
    UIManager.setLookAndFeel(LunaLookAndFeel.class.getName());
  }

  public static void changeWindowIcons()
  {
    UIManager.put("InternalFrame.iconifyIcon", loadIcon("jt-minimize.png"));
    UIManager.put("InternalFrame.maximizeIcon", loadIcon("jt-maximize.png"));
    UIManager.put("InternalFrame.minimizeIcon", loadIcon("jt-restore.png"));
    UIManager.put("InternalFrame.closeIcon", loadIcon("jt-close.png"));
  }


  private static ImageIcon loadIcon(String iconName)
  {
    return IconLoader.loadIcon(iconName);
  }

  public String getName()
  {
    return THEME_NAME;
  }

  public List<String> getSubthemes()
  {
    return LunaLookAndFeel.getThemes();
  }
  
  public void setProgressBarUI(JProgressBar progress)
  {
  	SmartGraphicalTheme.fixProgressBarUI(progress);
  }
}