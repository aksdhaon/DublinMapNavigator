package comp;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import com.esri.client.toolkit.bookmarks.JExtentBookmark;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.MapTip;
import com.esri.map.WebMap;

public class Navigator {
	
    private JMap map;
    private WebMap webMap;
    private JExtentBookmark extentBookmarks;

    // public map hosted on ArcGIS.com
    private static final String MAP_ID = "c3e5efd8054346ada4b784b281b37d0d";

    public Navigator() {

    }

    /**
     * Starting point of this application.
     * @param args arguments to this application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // instance of this application
                	Navigator app = new Navigator();

                    // create the UI, including the map, for the application.
                    JFrame appWindow = app.createWindow();
                    appWindow.add(app.createUI());
                    appWindow.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JComponent createUI() {
        // application content
        JComponent contentPane = createContentPane();

        // create the map
        map =  new JMap();

        // add extent bookmarks
        extentBookmarks = new JExtentBookmark(map);
        extentBookmarks.setLocation(10, 10);
        contentPane.add(extentBookmarks);

        // create a web map
        createWebMap(map);
        
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        map.getLayers().add(graphicsLayer);
        LinkedHashMap<String, String> displayFields = new LinkedHashMap<String, String>();
        displayFields.put("Street", "Street: ");
        displayFields.put("City", "City: ");
        
        MapTip mapTip = new MapTip(displayFields);
        graphicsLayer.setMapTip(mapTip);
        graphicsLayer.setVisible(true);
        
        contentPane.add(map);

        return contentPane;
    }

    /**
     * Initializes the Web Map.
     * @param jMap
     */
    private void createWebMap(JMap jMap) {
      try {
        webMap = new WebMap(MAP_ID);
      } catch (Exception e) {
          e.printStackTrace();
      }

      jMap.addMapEventListener(new MapEventListenerAdapter() {
        @Override
        public void mapReady(MapEvent event) {
          extentBookmarks.addBookmarks(webMap.getBookmarks());
        }
      });

      try {
          webMap.initializeMap(jMap);
      } catch (Exception e) {
          e.printStackTrace();
      }

    }

    /**
     * Creates the application window.
     * @return a window.
     */
    private JFrame createWindow() {
        JFrame window = new JFrame("Dublin Navigator Application");
        window.setBounds(100, 100, 1000, 700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setLayout(new BorderLayout(0, 0));
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                map.dispose();
            }
        });
        return window;
    }

    /**
     * Creates a content pane.
     * @return a content pane.
     */
    private static JLayeredPane createContentPane() {
        JLayeredPane contentPane = new JLayeredPane();
        contentPane.setBounds(100, 100, 1000, 700);
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setVisible(true);
        return contentPane;
    }
}
