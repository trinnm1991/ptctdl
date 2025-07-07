package edu.usfca.ds;

import edu.usfca.ds.panels.*;
import edu.usfca.xj.appkit.frame.XJWindow;
import edu.usfca.xj.appkit.menu.*;
import edu.usfca.xj.appkit.utils.XJAlert;
import edu.usfca.xj.appkit.utils.XJFileChooser;
import edu.usfca.xj.appkit.utils.XJLocalizable;
import edu.usfca.xj.foundation.XJUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DSWindow extends XJWindow implements XJMenuItemDelegate {

    protected JTabbedPane viewTabbedPane;

    protected DSPanelHeap heapPanel;
    protected DSPanelBST BSTPanel;
    protected DSPanelStackArray stackPanel;
    protected DSPanelStackLL stackPanelLL;
    protected DSPanelQueueArray queuePanel;
    protected DSPanelSort sortPanel;
    protected DSPanelQueueLL queuePanelLL;
    protected DSPanelListArray listPanel;
    protected DSPanelCountSort countSortPanel;
    protected DSPanelRadixSort radixSortPanel;
    protected DSPanelListLL listPanel2;
    protected DSPanelHashOpen hashOpenPanel;
    protected DSPanelHashClosed hashClosedPanel;
    protected DSPanelBucketSort bucketPanel;
    protected DSPanelHeapSort heapsortPanel;
    protected DSPanelHuff huffPanel;
    protected DSPanelDijkstra dijkstraPanel;
    protected DSPanelDFS dfsPanel;
    protected DSPanelTopological topoPanel;
    protected DSPanelBFS bfsPanel;
    protected DSPanelCC ccPanel;
    protected DSPanelPrim primPanel;
    protected DSPanelKruskal kruskalPanel;
    protected DSPanelAVLTree AVLPanel;
    protected DSPanelBTree BTreePanel;
    protected DSPanelBTree2 BTreePanel2;
    protected DSPanelDynamicProg DynamicProgPanel;
    protected DSPanelFloyd FloydPanel;
    protected DSPanelBinomialQueue BinomialQueuePanel;
    protected DSPanelDynamicProg2 DynamicProgPanel2;

    public DSWindow() {
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        r.width *= 0.75f;
        r.height *= 0.75f;
        getRootPane().setPreferredSize(r.getSize());

        viewTabbedPane = new JTabbedPane();
        viewTabbedPane.setTabPlacement(JTabbedPane.TOP);

        AVLPanel = new DSPanelAVLTree(this);


        viewTabbedPane.add("Cây tìm kiếm nhị phân cân bằng - AVL", AVLPanel);

        getContentPane().add(viewTabbedPane);
        pack();
    }

    public void setData(Object data) {
    }

    public Object getData() {
        return null;
    }

    public static final int MI_EXPORT_AS_IMAGE = 100;
    public static final int MI_EXPORT_AS_EPS = 101;
    public static final int MI_CHECK_UPDATES = 102;
    public static final int MI_ALGORITHMS_LIST = 200;
    public static final int MI_ALGORITHMS_SORT = 201;
    public static final int MI_ALGORITHMS_GRAPH = 202;
    public static final int MI_ALGORITHMS_TREES = 203;
    public static final int MI_ALGORITHMS_HASHING = 204;
    public static final int MI_ALGORITHMS_DYNAMIC = 205;
    public static final int MI_ALGORITHMS_HEAPS = 206;
    public static final int MI_ALGORITHMS_HUFFMAN = 207;

    private static final String VI_FILE = "Tệp";
    private static final String VI_EDIT = "Biên tập";
    private static final String VI_WINDOW = "Thông tin học viên";
    private static final String VI_HELP = "Trợ giúp";
    private static final String VI_CHECK_UPDATE = "Kiểm tra cập nhật";
    private static final String VI_INFO_1 = "Trần Minh Duy - 911224002";
    private static final int    MI_INFO_1 = 300;
    private static final String VI_INFO_2 = "Đồng Ngọc Trang - 911224007";
    private static final int    MI_INFO_2 = 301;
    private static final String appTitle = "Mô phỏng cây nhị phân cân bằng - AVL";

    public void customizeFileMenu(XJMenu menu) {
        menu.setTitle(VI_FILE);
        Iterator iter = menu.itemIterator();
        while (iter.hasNext()) {
            XJMenuItem item = (XJMenuItem) iter.next();
            if ("New".equals(item.getTitle())) {

                item.setTitle("Tạo mới");
            }
            else if ("Close".equals(item.getTitle())) {
                item.setTitle("Đóng");
            }
            else if ("Quit".equals(item.getTitle())) {
                item.setTitle("Thoát");
            } else {
                item.setTitle("Undefined");
            }
        }

        XJMenu exportMenu = new XJMenu();
        exportMenu.setTitle("Xuất tệp");
        exportMenu.addItem(new XJMenuItem("định dạng EPS...", MI_EXPORT_AS_EPS, this));
        exportMenu.addItem(new XJMenuItem("định dạng Bitmap Image...", MI_EXPORT_AS_IMAGE, this));

        menu.insertItemAfter(exportMenu, XJMainMenuBar.MI_CLOSE);
        menu.insertSeparatorAfter(XJMainMenuBar.MI_CLOSE);
    }

    public void customizeMenuBar(XJMainMenuBar menubar) {
        XJMenu menu = new XJMenu();
        menu.setTitle(VI_WINDOW);
        menu.setTag(303);

        menu.addItem(new XJMenuItem(appTitle, 304, this));
        menu.addSeparator();
        menu.addItem(new XJMenuItemCheck(VI_INFO_1, MI_INFO_1, this, true  ));
        menu.addItem(new XJMenuItemCheck(VI_INFO_2, MI_INFO_2, this, true  ));

        menubar.addCustomMenu(menu);

    }

    public void customizeWindowMenu(XJMenu menu) {

    }

    public void customizeEditMenu(XJMenu menu) {
        menu.setTitle(VI_EDIT);

        Iterator iter = menu.itemIterator();
        while (iter.hasNext()) {
            XJMenuItem item = (XJMenuItem) iter.next();
            if ("Cut".equals(item.getTitle())) {
                item.setTitle("Cắt");
            }
            else if ("Undo".equals(item.getTitle())) {
                item.setTitle("Hoàn tác");
            }
            else if ("Redo".equals(item.getTitle())) {
                item.setTitle("Làm lại");
            } else if ("Copy".equals(item.getTitle())) {
                item.setTitle("Sao chép");
            } else if ("Paste".equals(item.getTitle())) {
                item.setTitle("Dán");
            } else if ("Select All".equals(item.getTitle())) {
                item.setTitle("Chọn tất cả");
            }else {
                item.setTitle("Undefined");
            }
        }
    }

    public void customizeHelpMenu(XJMenu menu) {
        menu.setTitle(VI_HELP);
        menu.insertItemAfter(new XJMenuItem(VI_CHECK_UPDATE, MI_CHECK_UPDATES, this), XJMainMenuBar.MI_HELP);
        menu.insertSeparatorAfter(XJMainMenuBar.MI_HELP);

        Iterator iter = menu.itemIterator();
        while (iter.hasNext()) {
            XJMenuItem item = (XJMenuItem) iter.next();
            if ("Help".equals(item.getTitle())) {
                item.setTitle("Trợ giúp");
            } else if ("About".equals(item.getTitle())) {
                item.setTitle("Giới thiệu");
            }
        }
    }

    public void handleMenuEvent(XJMenu menu, XJMenuItem item) {
        super.handleMenuEvent(menu, item);
        switch(item.getTag()) {
            case MI_EXPORT_AS_EPS:
                exportPanelToEPS((DSPanel)viewTabbedPane.getSelectedComponent());
                break;
            case MI_EXPORT_AS_IMAGE:
                exportPanelToImage((DSPanel)viewTabbedPane.getSelectedComponent());
                break;
            case MI_CHECK_UPDATES:
                DSApplication.checkForUpdates(false);
                break;
            case MI_ALGORITHMS_LIST:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Stack-Array", stackPanel);
                viewTabbedPane.add("Stack-Linked List", stackPanelLL );
                viewTabbedPane.add("Queue-Array", queuePanel);
                viewTabbedPane.add("Queue-Linked List", queuePanelLL );
                viewTabbedPane.add("List-Array", listPanel);
                viewTabbedPane.add("List-Linked List", listPanel2);
                break;
            case MI_ALGORITHMS_SORT:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Comparison Sorts", sortPanel );
                viewTabbedPane.add("Bucket Sort", bucketPanel );
                viewTabbedPane.add("Counting Sort", countSortPanel );
                viewTabbedPane.add("Radix Sort", radixSortPanel);
                viewTabbedPane.add("Heap Sort", heapsortPanel);

                break;
            case MI_ALGORITHMS_TREES:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Binary Search Tree", BSTPanel);
                viewTabbedPane.add("AVL Tree", AVLPanel);
       //         viewTabbedPane.add("Red/Black Tree", RedBlackPanel);
                viewTabbedPane.add("2-3 Tree", BTreePanel2);
                viewTabbedPane.add("B-Tree (Proactive Split/Merge)", BTreePanel);

                break;
            case MI_ALGORITHMS_HEAPS:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Heap", heapPanel);
                viewTabbedPane.add("Binomial Queues", BinomialQueuePanel);

                break;
            case MI_ALGORITHMS_GRAPH:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Topological Sort", topoPanel);
                viewTabbedPane.add("Breadth First Search", bfsPanel);
                viewTabbedPane.add("Depth First Search", dfsPanel);
                viewTabbedPane.add("Dijkstra's Algorithm", dijkstraPanel);
                viewTabbedPane.add("Floyd-Warshall", FloydPanel);
                viewTabbedPane.add("Kruskal's Algorithm", kruskalPanel);
                viewTabbedPane.add("Prim's Algorithm", primPanel);
                viewTabbedPane.add("Connected Components", ccPanel);

                break;
            case MI_ALGORITHMS_HUFFMAN:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Huffman Coding", huffPanel);
                break;
            case MI_ALGORITHMS_DYNAMIC:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Dynamic Programming: Fibonacci", DynamicProgPanel);
                viewTabbedPane.add("Dynamic Programming: Change", DynamicProgPanel2);
                break;
            case MI_ALGORITHMS_HASHING:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("Open Hashing", hashOpenPanel);
                viewTabbedPane.add("Closed Hashing", hashClosedPanel);

                break;


        }
    }

    public void exportPanelToImage(DSPanel panel) {

        // Fetch the list of available image format
        List extensions = new ArrayList();
        for (int i = 0; i < ImageIO.getWriterFormatNames().length; i++) {
            String ext = ImageIO.getWriterFormatNames()[i].toLowerCase();
            if(!extensions.contains(ext))
                extensions.add(ext);
        }

        // Save the panel content to disk
        if(XJFileChooser.shared().displaySaveDialog(this.getJavaContainer(), extensions, extensions, false)) {
            String file = XJFileChooser.shared().getSelectedFilePath();
            try {
                ImageIO.write(panel.getImage(), file.substring(file.lastIndexOf(".")+1), new File(file));
            } catch (IOException e) {
                XJAlert.display(this.getJavaContainer(), "Error", "Image \""+file+"\" cannot be saved because:\n"+e);
            }
        }
    }

    public void exportPanelToEPS(DSPanel panel) {
        if(!XJFileChooser.shared().displaySaveDialog(this.getJavaContainer(), "eps", "EPS file", false))
            return;

        String file = XJFileChooser.shared().getSelectedFilePath();
        if(file == null)
            return;

        try {
            XJUtils.writeStringToFile(panel.getEPS(), file);
        } catch (Exception e) {
            XJAlert.display(this.getJavaContainer(), "Error", "Cannot export to EPS file: "+file+"\nError: "+e);
        }
    }

}
