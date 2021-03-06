/*
 * To change this license header, choose License Headers in DiptraceProject Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
/*
import diptrace.DiptraceProject;
import diptrace.tokenizer.DiptraceToken;
import diptrace.tokenizer.DiptraceTokenizer;
import diptrace.tree.DiptraceItem;
*/
import gui.JPanel_DiptraceGraphicsPanel.WhatToDraw;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javadiptraceasciilib.DiptraceProject;
import javadiptraceasciilib.DiptraceGraphics;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javadiptraceasciilib.DiptraceTreeNode;

/**
 *
 * @author daniel
 */
public class JFrame_MainWindow extends javax.swing.JFrame {
	
	DiptraceProject diptraceProject = new DiptraceProject();
	DefaultMutableTreeNode schematicsTreeRootNode = new DefaultMutableTreeNode();
	DefaultTreeModel diptraceSchematicsItemsTreeModel = new DefaultTreeModel(schematicsTreeRootNode);
	DefaultMutableTreeNode pcbTreeRootNode = new DefaultMutableTreeNode();
	DefaultTreeModel diptracePCBItemsTreeModel = new DefaultTreeModel(pcbTreeRootNode);
	
	
	/**
	 * Creates new form JFrame_MainWindow
	 */
	public JFrame_MainWindow() {
		initComponents();
	}
	
	public JFrame_MainWindow init() {
//		readFile("F:\\Projekt\\americaN\\Corleone\\Corleone ställverk bas 1.2.schematics.asc", "F:\\Projekt\\americaN\\Corleone\\Corleone ställverk bas 1.2.pcb.asc");
//		readFile("F:\\Projekt\\americaN\\Corleone\\Corleone test.asc", "F:\\Projekt\\americaN\\Corleone\\Corleone ställverk bas 1.2.pcb.asc");
//		readFile("F:\\Projekt\\americaN\\Corleone\\cap.asc", "F:\\Projekt\\americaN\\Corleone\\empty_pcb.asc");
//		readFile("F:\\Projekt\\americaN\\Corleone\\empty.asc", "F:\\Projekt\\americaN\\Corleone\\empty_pcb.asc");
		
		if (1==1)
			readFile("F:\\Projekt\\Java\\GitHub\\CorleoneStallvark\\CorleoneSwitchboard_Schematics.asc",
				"F:\\Projekt\\Java\\GitHub\\CorleoneStallvark\\CorleoneSwitchboard_PCB.asc");
		else if (1==0)
			readFile("F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics.asc",
//				"F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_pcb.asc");
				"F:\\Projekt\\americaN\\Corleone\\a.asc");
		else {
//			readFile("F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics.asc",
//				"F:\\Projekt\\americaN\\Corleone\\Layers.asc");
//			readFile("F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics.asc",
//				"F:\\Projekt\\americaN\\Corleone\\Layer_Colors.asc");
////			readFile("F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics.asc",
////				"F:\\Projekt\\americaN\\Corleone\\Layer_Colors_2.asc");
			readFile("F:\\Projekt\\americaN\\Corleone\\TestAAA.asc",
				"F:\\Projekt\\americaN\\Corleone\\Layer_Colors_2.asc");
		}
		
//		readFile("F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics_new.asc",
//			"F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_pcb_new.asc");
//		readFile("F:\\Projekt\\americaN\\Corleone\\Three LED.asc", "F:\\Projekt\\americaN\\Corleone\\Corleone ställverk bas 1.2.pcb.asc");
/*		
		DiptraceOperations diptraceOperations = new DiptraceOperations(diptraceProject);
		
		try {
			DiptraceItem part = diptraceOperations.getSchematicsComponentPart("aaa");
			diptraceOperations.duplicateItem(part);
		} catch (NotFoundException ex) {
			ex.printStackTrace();
		}
        
		
		
		
		writeFile("F:\\Projekt\\americaN\\Corleone\\Corleone ställverk bas 1.2.schematics.new.asc", "F:\\Projekt\\americaN\\Corleone\\Corleone ställverk bas 1.2.pcb.new.asc");
*/		
		fillTree(schematicsTreeRootNode, diptraceProject.getSchematicsRoot());
		fillTree(pcbTreeRootNode, diptraceProject.getPCBRoot());
//		jTree_DiptraceItems.collapsePath(path);
		
		jTree_DiptraceSchematicsItems.expandRow(0);
		jTree_DiptraceSchematicsItems.setRootVisible(false);
		jTree_DiptracePCBItems.expandRow(0);
		jTree_DiptracePCBItems.setRootVisible(false);
		
//		TreeModel model = jTree_DiptraceItems.getModel();
		
		return this;
	}
	
	private void fillTree(DefaultMutableTreeNode treeNode, DiptraceTreeNode item) {
		for (DiptraceTreeNode subItem : item.getChildren()) {
//			DefaultMutableTreeNode node = new DefaultMutableTreeNode(subItem.fIdentifier);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(subItem.toString());
			treeNode.add(node);
			fillTree(node, subItem);
		}
	}
	
	private void readFile(final String schematicsFilename, final String pcbFilename) {
		try {
			diptraceProject.readSchematicsAndPCB(schematicsFilename, pcbFilename);
		} catch (IOException ex) {
			Logger.getLogger(JFrame_MainWindow.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void writeFile(final String schematicsFilename, final String pcbFilename) {
		try {
			diptraceProject.writeSchematicsAndPCB(schematicsFilename, pcbFilename);
		} catch (IOException ex) {
			Logger.getLogger(JFrame_MainWindow.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree_DiptraceSchematicsItems = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree_DiptracePCBItems = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new JPanel_DiptraceGraphicsPanel(new DiptraceGraphics(diptraceProject), WhatToDraw.SCHEMATICS);
        jPanelGraphics = new JPanel_DiptraceGraphicsPanel(new DiptraceGraphics(diptraceProject), WhatToDraw.PCB);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTree_DiptraceSchematicsItems.setModel(diptraceSchematicsItemsTreeModel);
        jScrollPane1.setViewportView(jTree_DiptraceSchematicsItems);

        jTree_DiptracePCBItems.setModel(diptracePCBItemsTreeModel);
        jScrollPane3.setViewportView(jTree_DiptracePCBItems);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Schematics", jPanel1);

        javax.swing.GroupLayout jPanelGraphicsLayout = new javax.swing.GroupLayout(jPanelGraphics);
        jPanelGraphics.setLayout(jPanelGraphicsLayout);
        jPanelGraphicsLayout.setHorizontalGroup(
            jPanelGraphicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );
        jPanelGraphicsLayout.setVerticalGroup(
            jPanelGraphicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("PCB", jPanelGraphics);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane3)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelGraphics;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTree jTree_DiptracePCBItems;
    private javax.swing.JTree jTree_DiptraceSchematicsItems;
    // End of variables declaration//GEN-END:variables
	
	
}
