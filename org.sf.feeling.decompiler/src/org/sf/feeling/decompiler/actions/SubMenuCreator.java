/*******************************************************************************
 * Copyright (c) 2016 Chen Chao(cnfree2000@hotmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Chen Chao  - initial API and implementation
 *******************************************************************************/

package org.sf.feeling.decompiler.actions;

import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.sf.feeling.decompiler.JavaDecompilerPlugin;
import org.sf.feeling.decompiler.editor.DecompilerType;
import org.sf.feeling.decompiler.util.UIUtil;

public class SubMenuCreator implements IMenuCreator
{

	private MenuManager dropDownMenuMgr;

	public Menu getMenu( final Menu parent )
	{
		final Menu menu = new Menu( parent );
		fillMenu( menu );
		menu.addMenuListener( new MenuAdapter( ) {

			public void menuShown( MenuEvent e )
			{
				while ( menu.getItemCount( ) > 0 )
				{
					menu.getItem( 0 ).dispose( );
				}
				fillMenu( menu );
			}
		} );
		return menu;
	}

	private void fillMenu( final Menu menu )
	{
		final MenuManager menuMgr = new MenuManager( );


		menuMgr.add( new DecompileWithFernFlowerAction( ) );

		for ( int i = 0; i < DecompilerType.getDecompilerTypes( ).length; i++ )
		{
			menuMgr.add( JavaDecompilerPlugin.getDefault( )
					.getDecompilerDescriptor( DecompilerType.getDecompilerTypes( )[i] )
					.getDecompileAction( ) );
		}

		IContributionItem[] items = menuMgr.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			IContributionItem item = items[i];
			IContributionItem newItem = item;
			if ( item instanceof ActionContributionItem )
			{
				newItem = new ActionContributionItem( ( (ActionContributionItem) item ).getAction( ) );
			}
			newItem.fill( menu, -1 );
		}
	}

	public Menu getMenu( Control parent )
	{
		createDropDownMenuMgr( );
		return dropDownMenuMgr.createContextMenu( parent );
	}

	public void dispose( )
	{
		if ( null != dropDownMenuMgr )
		{
			dropDownMenuMgr.dispose( );
			dropDownMenuMgr = null;
		}
	}

	class PreferenceActionContributionItem extends ActionContributionItem
	{

		public PreferenceActionContributionItem( IAction action )
		{
			super( action );
		}

		public boolean isEnabledAllowed( )
		{
			return true;
		}
	}

	private void createDropDownMenuMgr( )
	{
		if ( dropDownMenuMgr == null )
		{
			dropDownMenuMgr = new MenuManager( );

			dropDownMenuMgr.add( new DecompileWithFernFlowerAction( ) );

			for ( int i = 0; i < DecompilerType.getDecompilerTypes( ).length; i++ )
			{
				dropDownMenuMgr.add( JavaDecompilerPlugin.getDefault( )
						.getDecompilerDescriptor( DecompilerType.getDecompilerTypes( )[i] )
						.getDecompileAction( ) );
			}
			
			dropDownMenuMgr.add( new Separator( ) );

			List list = UIUtil.getExportSelections( );
			if ( list != null )
			{
				dropDownMenuMgr.add( new ExportSourceAction( list ) );
			}
			else
			{
				dropDownMenuMgr.add( new ExportEditorSourceAction( ) );
			}

			dropDownMenuMgr.add( new Separator( ) );
			dropDownMenuMgr.add( new PreferenceActionContributionItem( new DecompilerPeferenceAction( ) ) );
		}
	}
}
