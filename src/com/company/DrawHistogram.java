package com.company;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public class DrawHistogram{
    private Map<Character, Long> data;
    public DrawHistogram(Map<Character,Long> data){
        this.data = data;
        JFrame frame = new JFrame("Histogram");
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(new Graph(data)));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    protected class Graph extends JPanel{
        protected static final int MIN_WIDTH = 10;
        private Map<Character, Long> map ;

        public Graph(Map<Character, Long> map){
            this.map = map;
            int width = map.size()*MIN_WIDTH + 300;
            Dimension minSize = new Dimension(width,360);
            Dimension prefSize = new Dimension(width,720);
            setMinimumSize(minSize);
            setPreferredSize(prefSize);

        }
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            if(map!=null){
                int xOffset = 10;
                int yOffset = 10;
                int width = getWidth()-1-(xOffset*2);
                int height = getHeight()-1-(yOffset*2);
                Graphics2D gd = (Graphics2D)g.create();
                gd.setColor(Color.BLACK);
                gd.drawRect(xOffset,yOffset,width,height);
                int barWidth = Math.max(MIN_WIDTH,
                        (int) Math.floor((float) width
                                / (float) map.size()));
                long maxValue = 0L;
                for(Long val:map.values()){
                    maxValue = Math.max(maxValue,val);
                }
                int xPos = xOffset;
                for(Character key:map.keySet()){
                    long val = map.get(key);
                    long barHeight = Math.round(((float) val
                            / (float) maxValue) * height);
                    gd.setColor(new Color(key,key,key));
                    long yPos = height+yOffset - barHeight;
                    Rectangle2D rect = new Rectangle2D.Float(xPos,yPos,barWidth,barHeight);
                    gd.fill(rect);
                    gd.setColor(Color.DARK_GRAY);
                    gd.draw(rect);
                    xPos += barWidth;
                }
                gd.dispose();
            }
        }
    }

}
