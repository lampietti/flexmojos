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
package net.flexmojos.oss.tests.issues;

import static org.hamcrest.MatcherAssert.assertThat;
import static net.flexmojos.oss.util.PathUtil.file;

import java.io.File;

import com.marvinformatics.kiss.matchers.file.FileMatchers;
import org.codehaus.plexus.util.FileUtils;
import net.flexmojos.oss.test.FMVerifier;
import org.testng.annotations.Test;

public class Flexmojos350Test
    extends AbstractIssueTest
{
    @Test
    public void orgFlexunit()
        throws Exception
    {
        File basedir = getProject( "issues/flexmojos-350" );
        FMVerifier verifier = getVerifier( basedir );

        // TODO remove this once flexunit is released!
        File or = file( FMVerifier.getArtifactPath( "com.adobe.flexunit", "flexunit", "4.0-beta-2", "swc" ) );
        File fk = file( FMVerifier.getArtifactPath( "org.flexunit", "flexunit", "4.1", "swc" ) );

        assertThat( or, FileMatchers.exists() );
        fk.getParentFile().mkdirs();

        FileUtils.copyFile( or, fk );

        verifier.executeGoal( "install" );

        assertThat( new File( basedir, "target/test-classes/TestRunner.swf" ), FileMatchers.exists() );
        assertThat( new File( basedir, "target/surefire-reports/TEST-AnnotatedTest.dummyTest.AnnotatedTest.xml" ),
                    FileMatchers.exists() );
    }
}
