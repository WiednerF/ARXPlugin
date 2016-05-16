package org.deidentifier.arx.kettle;
/**
 * ARXPlugin Dialog for Setting up the ARX Step
 * @author Florian Wiedner
 */


import org.deidentifier.arx.kettle.dialoge.ARXDialogFieldTab;
import org.deidentifier.arx.kettle.dialoge.ARXDialogGeneralTab;
import org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;

public class ARXPluginDialog extends org.pentaho.di.ui.trans.step.BaseStepDialog implements org.pentaho.di.trans.step.StepDialogInterface
{
	  private static Class<?> PKG = ARXPluginMeta.class; // for i18n purposes, needed by Translator2!!
	  
	  private ARXPluginMeta input;//The InputStructure
	  
	  
	  private FormLayout formLayout;
	  private CTabFolder wTabFolder;//The Common Tab plugin
	  private String[] fieldNames;
	  private Button fdbShowAnalysis;
	  
	  private ARXPluginDialogInterface[] composites;
	  
	  
	
	
	public ARXPluginDialog(org.eclipse.swt.widgets.Shell parent, Object in, TransMeta transMeta, String sname)
	{
		super(parent, (BaseStepMeta)in, transMeta, sname);
		input = (ARXPluginMeta) in;
	    composites=new ARXPluginDialogInterface[2];
	}

	public String open()
	{
		 	Shell parent = getParent();//Get the Shell for Working
		    Display display = parent.getDisplay();//Get the Working Display Set
		    
			try {
			    RowMetaInterface r;
				r = transMeta.getPrevStepFields( stepname );
			    fieldNames=r.getFieldNames();

			} catch (KettleStepException e2) {
				e2.printStackTrace();
			}		    
		    
		    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
		    
		    props.setLook( shell );
		    setShellImage( shell, input );
		    
		    ModifyListener lsMod = new ModifyListener() {//Listen for Changes
		        public void modifyText( ModifyEvent e ) {
		          input.setChanged();
		        }
		      };
		      changed = input.hasChanged();//If something changed, for Updating purpose
		      formLayout = new FormLayout();
		      formLayout.marginWidth = Const.FORM_MARGIN;
		      formLayout.marginHeight = Const.FORM_MARGIN;

		      shell.setLayout( formLayout );
		      shell.setText( Messages.getString("ARXPluginDialog.Title" ) );
		      int middle = props.getMiddlePct();//The Middle Point
		      int margin = Const.MARGIN;//The Normal Margin
		      
		      Label wlShowAnalysis = new Label (shell, SWT.RIGHT);
		      wlShowAnalysis.setText(Messages.getString("ARXPluginDialog.show"));
		      props.setLook(wlShowAnalysis);
		      FormData fdlShowAnalysis = new FormData();
		      fdlShowAnalysis.left = new FormAttachment( 0, 0 );
		      fdlShowAnalysis.top = new FormAttachment( 0, margin );
		      fdlShowAnalysis.right = new FormAttachment( middle, -margin );
		      wlShowAnalysis.setLayoutData(fdlShowAnalysis);
		      this.fdbShowAnalysis = new Button(shell,SWT.CHECK);
		      this.fdbShowAnalysis.setSelection(true);
		      FormData fdbShowAnalysis = new FormData();
		      fdbShowAnalysis.left = new FormAttachment( middle, 0 );
		      fdbShowAnalysis.top = new FormAttachment( 0, margin );
		      fdbShowAnalysis.right = new FormAttachment( 100,0 );
		      this.fdbShowAnalysis.setLayoutData(fdbShowAnalysis);
		      this.fdbShowAnalysis.addSelectionListener(new SelectionListener(){

				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}

				public void widgetSelected(SelectionEvent arg0) {
					input.setChanged(true);
				}
		    	  
		      });
		      //ADD StepName
		      wlStepname = new Label( shell, SWT.RIGHT );
		      wlStepname.setText( BaseMessages.getString( PKG, "System.Label.StepName" ) );
		      props.setLook( wlStepname );
		      fdlStepname = new FormData();
		      fdlStepname.left = new FormAttachment( 0, 0 );
		      fdlStepname.top = new FormAttachment( wlShowAnalysis, margin );
		      fdlStepname.right = new FormAttachment( middle, -margin );
		      wlStepname.setLayoutData( fdlStepname );
		      wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
		      wStepname.setText( stepname );
		      props.setLook( wStepname );
		      wStepname.addModifyListener( lsMod );
		      fdStepname = new FormData();
		      fdStepname.left = new FormAttachment( middle, 0 );
		      fdStepname.top = new FormAttachment( wlShowAnalysis, margin );
		      fdStepname.right = new FormAttachment( 100, 0 );
		      wStepname.setLayoutData( fdStepname );
		      //END STEPNAME
		      
		     
		      //TABS
		      wTabFolder = new CTabFolder( shell,SWT.BORDER );
		      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
		      wTabFolder.setSimple( false );
		      wTabFolder.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent arg0) {
						composites[wTabFolder.getSelectionIndex()].getData();
					} 
			      });
		  		      
		      
		     this.composites[0]=new ARXDialogGeneralTab(wTabFolder,input,props,fieldNames);
		     this.composites[1]=new ARXDialogFieldTab(wTabFolder,input,props,lsMod,fieldNames,transMeta);
		      //END TABS
		     wTabFolder.setSelection(0);
		      FormData fdTabFolder = new FormData();
		      fdTabFolder.left = new FormAttachment( 0, 0 );
		      fdTabFolder.top = new FormAttachment( wStepname, margin );
		      fdTabFolder.right = new FormAttachment( 100, 0 );
		      fdTabFolder.bottom = new FormAttachment( 100, -50 );
		      wTabFolder.setLayoutData( fdTabFolder );
		      //BUTTON
		      wOK = new Button( shell, SWT.PUSH );
		      wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );

		      wCancel = new Button( shell, SWT.PUSH );
		      wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

		      setButtonPositions( new Button[] { wOK, wCancel }, margin, wTabFolder );
		      
		      lsOK = new Listener() {
		          public void handleEvent( Event e ) {
		            ok();
		          }
		        };
		        wOK.addListener( SWT.Selection, lsOK );
		        lsCancel = new Listener() {
		            public void handleEvent( Event e ) {
		              cancel();
		            }
		          };
		          wCancel.addListener( SWT.Selection, lsCancel );
		          //SHELL CLOSE HANDLE
		          shell.addShellListener( new ShellAdapter() {
		              public void shellClosed( ShellEvent e ) {
		                cancel();
		              }
		            } );
		          
		       setSize();
		       getData();
		      //OPEN AND CONTROL
		      input.setChanged( changed );

		      shell.open();
		      while ( !shell.isDisposed() ) {
		        if ( !display.readAndDispatch() ) {
		          display.sleep();
		        }
		      }
		      return this.stepname;
	}
	private void ok() {
	    if ( Const.isEmpty( wStepname.getText() ) ) {
	      return;
	    }

	    this.stepname = wStepname.getText(); // return value

	    saveInfoInMeta();

	    dispose();
	  }
	
	private void cancel() {
	    stepname = null;

	    input.setChanged( backupChanged );

	    dispose();
	  }
	
	/**
	 * Saves all the Information from the Plugin Dialog
	 */
	private void saveInfoInMeta(){
		for(ARXPluginDialogInterface composite:this.composites){
			composite.saveData();
		}
		this.input.setShowRiskAnalysis(this.fdbShowAnalysis.getSelection());
	}
	
	/**
	 * Writes the Data to the Plugin Dialog
	 */
	private void getData(){
		for(ARXPluginDialogInterface composite:this.composites){
			composite.getData();
		}
		this.fdbShowAnalysis.setSelection(this.input.isShowRiskAnalysis());
	}
	
	
}


