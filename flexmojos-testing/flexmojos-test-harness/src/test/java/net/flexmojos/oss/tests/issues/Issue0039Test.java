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

import java.io.File;

import com.marvinformatics.kiss.matchers.file.FileMatchers;
import org.testng.annotations.Test;

public class Issue0039Test
    extends AbstractIssueTest
{

    @Test
    public void issue39()
        throws Exception
    {
        File testDir = getProject( "/issues/issue-0039" );
        test( testDir, "flexmojos:asdoc" );

        assertThat( new File( testDir, "target/asdoc/main.html" ), FileMatchers.exists() );
    }

}
