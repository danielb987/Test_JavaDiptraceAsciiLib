/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import examples.javadiptraceasciilib.FlashLight;
import static examples.javadiptraceasciilib.FlashLight.createFlashLight;

/**
 *
 * @author daniel
 */
public class Main {
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		
		FlashLight.createFlashLight(
            "F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics.asc",
            "F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_schematics_new.asc",
            "F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_pcb.asc",
            "F:\\Projekt\\Java\\GitHub\\JavaDiptraceAsciiLib\\src\\examples\\javadiptraceasciilib\\flashlight_pcb_new.asc");
		
		System.exit(0);
/*		
		try {
			new JFrame_MainWindow().init();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(0);
*/		
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(JFrame_MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(JFrame_MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(JFrame_MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(JFrame_MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
//				new javadiptraceasciilib.Example().something();
				new JFrame_MainWindow().init().setVisible(true);
			}
		});
	}
	
}
