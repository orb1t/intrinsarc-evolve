package org.freehep.graphicsio.pdf;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import org.freehep.graphics2d.*;
import org.freehep.graphicsio.*;
import org.freehep.graphicsio.exportchooser.*;
import org.freehep.swing.layout.*;
import org.freehep.util.*;

/**
 * 
 * @author Simon Fischer
 * @version $Id: PDFExportFileType.java,v 1.1 2009-03-04 22:46:57 andrew Exp $
 */
public class PDFExportFileType extends AbstractExportFileType
{

  final private static String[] versionList = {PDFGraphics2D.VERSION4, PDFGraphics2D.VERSION5};

  public String getDescription()
  {
    return "Portable Document Format";
  }

  public String[] getExtensions()
  {
    return new String[]{"pdf"};
  }

  public String[] getMIMETypes()
  {
    return new String[]{"application/pdf"};
  }

  public boolean isMultipageCapable()
  {
    return true;
  }

  public boolean hasOptionPanel()
  {
    return true;
  }

  public JPanel createOptionPanel(Properties user)
  {
    UserProperties options = new UserProperties(user, PDFGraphics2D.getDefaultProperties());
    OptionPanel format = new OptionPanel("Format");

    OptionComboBox version = new OptionComboBox(options, PDFGraphics2D.VERSION, versionList);
    format.add(TableLayout.LEFT, new JLabel("PDF Version"));
    format.add(TableLayout.RIGHT, version);

    format.add(TableLayout.FULL, new OptionCheckBox(options, PDFGraphics2D.COMPRESS, "Compress"));

    JPanel preview = new OptionPanel("Preview");

    JCheckBox thumbnails = new OptionCheckBox(options, PDFGraphics2D.THUMBNAILS, "Include Thumbnail");
    thumbnails.setToolTipText("Thumbnails are automatically generated by " + "Acrobat Reader 5");
    preview.add(TableLayout.FULL, thumbnails);
    version.selects(PDFGraphics2D.VERSION4, thumbnails);

    String rootKey = PDFGraphics2D.class.getName();

    JPanel infoPanel = new InfoPanel(options, rootKey, new String[]{InfoConstants.AUTHOR, InfoConstants.TITLE,
        InfoConstants.SUBJECT, InfoConstants.KEYWORDS});

    // TableLayout.LEFT Panel
    JPanel leftPanel = new OptionPanel();
    leftPanel.add(TableLayout.COLUMN, new PageLayoutPanel(options, rootKey));
    leftPanel.add(TableLayout.COLUMN, new PageMarginPanel(options, rootKey));
    leftPanel.add(TableLayout.COLUMN_FILL, new JLabel());

    // TableLayout.RIGHT Panel
    JPanel rightPanel = new OptionPanel();
    rightPanel.add(TableLayout.COLUMN, format);
    rightPanel.add(TableLayout.COLUMN, preview);
    rightPanel.add(TableLayout.COLUMN, new BackgroundPanel(options, rootKey, true));
    rightPanel.add(TableLayout.COLUMN, new ImageTypePanel(options, rootKey, new String[]{ImageConstants.SMALLEST,
        ImageConstants.ZLIB, ImageConstants.JPG}));
    rightPanel.add(TableLayout.COLUMN, new FontPanel(options, rootKey));
    rightPanel.add(TableLayout.COLUMN_FILL, new JLabel());

    // Make the full panel.
    OptionPanel optionsPanel = new OptionPanel();
    optionsPanel.add("0 0 [5 5 5 5] wt", leftPanel);
    optionsPanel.add("1 0 [5 5 5 5] wt", rightPanel);
    optionsPanel.add("0 1 2 1 [5 5 5 5] wt", infoPanel);
    optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

    return optionsPanel;
  }

  public VectorGraphics getGraphics(OutputStream os, Component target) throws IOException
  {
    return new PDFGraphics2D(os, target.getSize());
  }
}
