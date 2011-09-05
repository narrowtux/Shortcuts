/*
 * Copyright (C) 2011 Moritz Schmale <narrow.m@gmail.com>
 *
 * NarrowtuxLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */

package com.narrowtux.shortcuts;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.getspout.spoutapi.keyboard.Keyboard;

public class Shortcut {
	private Comparator<Keyboard> keyCompare = new Comparator<Keyboard>() {

		@Override
		public int compare(Keyboard o1, Keyboard o2) {
			if(o1.getKeyCode() < o2.getKeyCode()){
				return -1;
			}
			if(o1.getKeyCode() > o2.getKeyCode()){
				return 1;
			}
			return 0;
		}

	};
	private Set<Keyboard> keys = new TreeSet<Keyboard>(keyCompare);


	public Shortcut(Set<Keyboard> currentlyPressedKeys) {
		setKeys(currentlyPressedKeys);
	}

	public Set<Keyboard> getKeys(){
		return keys;
	}

	public void setKeys(Set<Keyboard> keys){
		this.keys.clear();
		for(Keyboard key:keys){
			this.keys.add(key);
		}
	}

	@Override
	public String toString(){
		String result = "";
		for(Keyboard key:keys){
			if(result.length()>0){
				result+=" + ";
			}
			result+=key.toString().replace("KEY_", "");
		}
		return result;
	}

	@Override
	public int hashCode() {
		return keys.hashCode();
	}

	@Override
	public boolean equals(Object other){
		if(!(other instanceof Shortcut)){
			return false;
		}
		Shortcut sh = (Shortcut) other;
		return sh.keys.equals(keys);
	}
}
