/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
 * 
 * This file is part of TXManager (Telephone eXchange Manager).
 *
 * TXManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TXManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TXManager. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package tx.dx;

import java.io.FileInputStream;
import java.util.Properties;

import tx.common.Command;
import tx.common.CommandManager;
import tx.common.CommandTest;
import tx.common.StreamCommandManagerDump;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxCommandTest extends CommandTest {

	public static void main(String[] args) throws Exception {
		
		CommandManager commandManager = new DxCommandManager();
		
		Properties params = new Properties();
		params.load(new FileInputStream("conf/mt.conf"));
		params.put("dump", new StreamCommandManagerDump("dump/dx.dump"));
		
		Command[] commands = { 
			new Command("RESET"), 
			new Command("ZPLM:SUB=2631000")
		};
		
		new DxCommandTest().executeCommands(commandManager, params, commands);
		
	}

}
