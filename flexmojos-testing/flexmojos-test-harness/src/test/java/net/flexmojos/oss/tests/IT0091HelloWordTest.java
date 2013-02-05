/**
 * Flexmojos is a set of maven goals to allow maven users to compile, optimize and test Flex SWF, Flex SWC, Air SWF and Air SWC.
 * Copyright (C) 2008-2012  Marvin Froeder <marvin@flexmojos.net>
 *
 * This program is free software: you can redistribute it and/or modify
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.flexmojos.oss.tests;

import java.io.File;

import com.marvinformatics.kiss.matchers.file.FileMatchers;
import org.hamcrest.MatcherAssert;
import net.flexmojos.oss.test.FMVerifier;
import org.testng.annotations.Test;

public class IT0091HelloWordTest
    extends AbstractFlexMojosTests
{

    @Test
    public void helloWordTest()
        throws Exception
    {
        FMVerifier v = test( getProject( "intro/hello-world" ), "install", "-DisIt=true" );
        String dir = v.getBasedir();

        File target = new File( dir, "target" );
        File main = new File( target, "hello-world-1.0-SNAPSHOT.swf" );

        assertSeftExit( main, 3539,v );
    }

    @Test
    public void helloWordNoInherit()
        throws Exception
    {
        FMVerifier v = test( getProject( "intro/hello-world-no-inherit" ), "install", "-DisIt=true" );
        String dir = v.getBasedir();

        File target = new File( dir, "target" );
        File main = new File( target, "hello-world-no-inherit-1.0-SNAPSHOT.swf" );

        assertSeftExit( main, 3539, v );
    }

    @Test
    public void asdoc()
        throws Exception
    {
        String dir = test( getProject( "intro/hello-world-no-inherit" ), "flexmojos:asdoc" ).getBasedir();

        File target = new File( dir, "target" );
        File asdoc = new File( target, "asdoc" );

        MatcherAssert.assertThat( asdoc, FileMatchers.exists() );
    }

    @Test
    public void spark()
        throws Exception
    {
        FMVerifier v = test( getProject( "intro/hello-spark" ), "install", "-DisIt=true" );
        String dir = v.getBasedir();

        File target = new File( dir, "target" );
        File main = new File( target, "hello-spark-1.0-SNAPSHOT.swf" );

        assertSeftExit( main, 7727, v );
    }
    
}
