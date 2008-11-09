/*
 * Flex-mojos is a set of maven plugins to allow maven users to compile, optimize, test and ... Flex SWF, Flex SWC, Air SWF and Air SWC.
 * Copyright (C) 2008 Marvin Herman Froeder
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package info.flexmojos.unitestingsupport.util
{
	import flash.utils.describeType;
	
	public class ClassnameUtil
	{
		public function ClassnameUtil()
		{
		}
		
		/**
		 * Return the fully qualified class name for an Object.
		 * @param obj the Object.
		 * @return the class name.
		 */
		public static function getClassName( obj:Object ):String
		{
			var description:XML = describeType( obj );
			var className:Object = description.@name;
			
			return className[ 0 ];
		}
	}
}