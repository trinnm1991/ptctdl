/*

[The "BSD licence"]
Copyright (c) 2005 Jean Bovet
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package edu.usfca.xj.appkit.menu;

import edu.usfca.xj.appkit.app.XJApplication;
import edu.usfca.xj.appkit.document.XJDocument;
import edu.usfca.xj.appkit.frame.XJWindow;
import edu.usfca.xj.appkit.utils.XJLocalizable;
import edu.usfca.xj.foundation.XJSystem;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XJMainMenuBar implements XJMenuItemDelegate {

    public static final int MI_NEW = 10000;
    public static final int MI_OPEN = 10001;
    public static final int MI_LOAD = 10002;
    public static final int MI_SAVE = 10003;
    public static final int MI_SAVEAS = 10004;
    public static final int MI_CLOSE = 10005;
    public static final int MI_QUIT = 10006;
    public static final int MI_HELP = 10020;
    public static final int MI_ABOUT = 10021;
    public static final int MI_PREFS = 10022;

    public static final int MI_CLEAR_RECENT_FILES = 20000;
    public static final int MI_RECENT_FILES = 20001; // + any number of recent files!

    public static final int MI_WINDOW = 21000; // + any number of windows!

    protected JMenuBar menuBar = null;
    protected XJMenu menuFile = null;
    protected XJMenu menuRecentFiles = null;
    protected XJMenu menuWindow = null;
    protected XJMenu menuHelp = null;

    protected XJMenuBarCustomizer customizer = null;
    protected XJMenuBarDelegate delegate = null;

    protected List customMenus = new ArrayList();

    protected static List mmbs = new ArrayList();

    public static XJMainMenuBar createInstance() {
        XJMainMenuBar mmb = new XJMainMenuBar();
        mmbs.add(mmb);
        return mmb;
    }

    public static void removeInstance(XJMainMenuBar mmb) {
        mmbs.remove(mmb);
    }

    public static void refreshAllRecentFilesMenu() {
        Iterator iterator = mmbs.iterator();
        while(iterator.hasNext()) {
            XJMainMenuBar mmb = (XJMainMenuBar)iterator.next();
            mmb.rebuildRecentFilesMenu();
        }
    }

    public XJMainMenuBar() {
    }

    public void setCustomizer(XJMenuBarCustomizer customizer) {
        this.customizer = customizer;
    }

    public void setDelegate(XJMenuBarDelegate delegate) {
        this.delegate = delegate;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    public XJMenu getFileMenu() {
        return menuFile;
    }
    
    public void refresh() {
        refreshContent();
        refreshState();
    }

    public void refreshState() {
        refreshState(menuFile);
        refreshState(menuHelp);

        Iterator iterator = customMenus.iterator();
        while(iterator.hasNext())
            refreshState((XJMenu)iterator.next());
    }

    public void refreshState(XJMenu menu) {
        Iterator iterator = menu.itemIterator();
        while(iterator.hasNext()) {
            XJMenuItem item = (XJMenuItem)iterator.next();
            if(delegate != null) {
                if(item.getTag() == MI_SAVE)
                    item.setEnabled(delegate.dirty());

                delegate.menuItemState(item);
            }
        }
    }

    public void refreshContent() {
        rebuildRecentFilesMenu();
        rebuildWindowMenu();
    }

    public void setActiveWindow(XJWindow window) {
        if(menuWindow == null)
            return;

        int index = XJApplication.shared().getWindows().indexOf(window);
        if(index>=0) {
            XJMenuItem item = menuWindow.getItemAtIndex(index);
            item.setSelected(true);
        }
    }

    public void setupMenuItem(XJMenuItem item, String name, char mnemonic, int keystroke, int tag) {
        item.setTitle(name);
        item.setTag(tag);

        if(mnemonic > 0)
            item.setMnemonic(mnemonic);

        if(keystroke >0) {
            item.setAccelerator(keystroke);
        }

        item.setDelegate(this);
    }

    public XJMenuItem buildMenuItem(String name, char mnemonic, int keystroke, int tag) {
        XJMenuItem item = new XJMenuItem();
        setupMenuItem(item, name, mnemonic, keystroke, tag);
        return item;
    }

    public XJMenuItemCheck buildMenuItemCheck(String name, char mnemonic, int keystroke, int tag) {
        XJMenuItemCheck item = new XJMenuItemCheck();
        setupMenuItem(item, name, mnemonic, keystroke, tag);
        return item;
    }

    public XJMenuItem buildMenuItem(String name, int tag) {
        return buildMenuItem(name, (char)0, -1, tag);
    }

    public void createMenuBar() {
        customMenus.clear();

        menuBar = new XJMenuBar();

        XJMenu menu = buildFileMenu();
        if(customizer != null)
            customizer.customizeFileMenu(menu);
        addMenu(menu);

        if(customizer != null)
            customizer.customizeMenuBar(this);

        menu = buildWindowMenu();
        if(customizer != null)
            customizer.customizeWindowMenu(menu);
        addMenu(menu);

        menu = buildHelpMenu();
        if(customizer != null)
            customizer.customizeHelpMenu(menu);
        addMenu(menu);
    }

    public void addCustomMenu(XJMenu menu) {
        if(menu == null)
            return;

        customMenus.add(menu);
        addMenu(menu);
    }

    private void addMenu(XJMenu menu) {
        menuBar.add(menu.getSwingComponent());
        menu.setMainMenuBar(this);
    }

    private XJMenu buildFileMenu() {
        boolean persistence = XJApplication.shared().supportsPersistence();

        menuFile = new XJMenu();
        menuFile.setTitle(XJLocalizable.getXJString("File"));
        menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("New")+((XJApplication.shared().getDocumentExtensions().size()>1)?"...":""), 'N', KeyEvent.VK_N, MI_NEW));
        if(persistence) {
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Open"), 'O', KeyEvent.VK_O, MI_OPEN));
            menuFile.addItem(createRecentFilesMenu());
        }
        menuFile.addSeparator();
        menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Close"), 'W', KeyEvent.VK_W, MI_CLOSE));
        if(persistence) {
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Save"), 'S', KeyEvent.VK_S, MI_SAVE));
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("SaveAs"), MI_SAVEAS));
        }

        if(!XJSystem.isMacOS()) {
            menuFile.addSeparator();
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Preferences"), ',', KeyEvent.VK_COMMA, MI_PREFS));
            menuFile.addSeparator();
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Quit"), 'Q', KeyEvent.VK_Q, MI_QUIT));
        }
        return menuFile;
    }

    public XJMenu createRecentFilesMenu() {
        menuRecentFiles = new XJMenu();
        rebuildRecentFilesMenu();
        return menuRecentFiles;
    }

    public void rebuildRecentFilesMenu() {
        if(menuRecentFiles == null)
            return;

        menuRecentFiles.clear();
        menuRecentFiles.setTitle(XJLocalizable.getXJString("OpenRecent"));

        int f = 0;
        Iterator iterator = XJApplication.shared().recentFiles().iterator();
        while(iterator.hasNext()) {
            menuRecentFiles.addItem(buildMenuItem((String)iterator.next(), MI_RECENT_FILES+f++));
        }
        menuRecentFiles.addSeparator();
        menuRecentFiles.addItem(buildMenuItem(XJLocalizable.getXJString("ClearMenu"), MI_CLEAR_RECENT_FILES));
    }

    private void buildWindowMenu_() {
        Iterator iterator = XJApplication.shared().getWindows().iterator();
        int count = 0;
        while(iterator.hasNext()) {
            XJWindow window = (XJWindow)iterator.next();
            if(window.shouldAppearsInWindowMenu()) {
                XJMenuItemCheck item = buildMenuItemCheck(window.getTitle(), (char)0, count<10?KeyEvent.VK_0+count:-1, MI_WINDOW+count);
                item.setSelected(window.isActive());
                menuWindow.addItem(item);
                count++;
            }
        }

        if(count == 0) {
            XJMenuItem item = new XJMenuItem(XJLocalizable.getXJString("NoWindows"), 0, null);
            item.setEnabled(false);
            menuWindow.addItem(item);
        }
    }

    private XJMenu buildWindowMenu() {
        menuWindow = new XJMenu();
        menuWindow.setTitle(XJLocalizable.getXJString("Window"));
        buildWindowMenu_();
        return menuWindow;
    }

    private void rebuildWindowMenu() {
        if(menuWindow == null)
            return;

        for(int index = menuWindow.getItemCount()-1; index>=0; index--) {
            XJMenuItem item = menuWindow.getItemAtIndex(index);
            if(item.getTag() >= MI_WINDOW)
                menuWindow.removeItem(index);
        }
        buildWindowMenu_();
    }

    private XJMenu buildHelpMenu() {
        menuHelp = new XJMenu();
        menuHelp.setTitle(XJLocalizable.getXJString("Help"));
        menuHelp.addItem(buildMenuItem(XJLocalizable.getXJString("Help"), MI_HELP));
        if(!XJSystem.isMacOS()) {
            menuHelp.addSeparator();
            menuHelp.addItem(buildMenuItem(XJLocalizable.getXJString("About"), MI_ABOUT));
        }
        return menuHelp;
    }

    public void handleMenuEvent(XJMenu menu, XJMenuItem item) {
        XJDocument document = XJApplication.shared().getActiveDocument();
        switch(item.tag) {
            case MI_NEW:
                XJApplication.shared().newDocument();
                break;

            case MI_OPEN:
                XJApplication.shared().openDocument();
                break;

            case MI_LOAD:
                if(document != null && document.performLoad())
                    document.changeReset();
                break;

            case MI_SAVE:
                if(document != null && document.performSave(false))
                    document.changeReset();
                break;

            case MI_SAVEAS:
                if(document != null && document.performSave(true))
                    document.changeReset();
                break;

            case MI_CLEAR_RECENT_FILES:
                XJApplication.shared().clearRecentFiles();
                break;

            case MI_QUIT:
                XJApplication.shared().performQuit();
                break;

            case MI_PREFS:
                XJApplication.shared().displayPrefs();
                break;

            case MI_ABOUT:
                XJApplication.shared().displayAbout();
                break;

            case MI_HELP:
                XJApplication.shared().displayHelp();
                break;

            default:
                if(item.tag>=MI_WINDOW && item.tag<=MI_WINDOW+10) {
                    XJWindow window = (XJWindow)XJApplication.shared().getWindows().get(item.tag-MI_WINDOW);
                    window.bringToFront();
                } else if(item.tag>=MI_RECENT_FILES && item.tag<=MI_RECENT_FILES+XJApplication.MAX_RECENT_FILES) {
                    if(!XJApplication.shared().openDocument(item.getTitle())) {
                        XJApplication.shared().removeRecentFile(item.getTitle());
                    }
                } else if(delegate != null)
                    delegate.handleMenuEvent(menu, item);
                break;
        }
    }

    public class XJMenuBar extends JMenuBar {

        // Need to override this method in order to refresh
        // the state of all menu items. Otherwise, the key binding
        // may not perform any action if the item is already disabled
        // (because by default the opening of a menu causes its items
        // to be refreshed).

        protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                        int condition, boolean pressed) {
            refreshState();
            return super.processKeyBinding(ks, e, condition, pressed);
        }
    }
}
